package eu.scy.agents.keywords.workflow.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.germanStemmer;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.ParameterIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;
import de.fhg.iais.kd.tm.obwious.util.Assert;

/**
 * Stems tokens of a document.
 * <ul>
 * <li><b>Inputs</b></li>
 * <ul>
 * <li>Document(Document): The input document.</li>
 * </ul>
 * <li><b>Parameters</b></li>
 * <ul>
 * <li>Language(String): The language of the document. Needed to select the right stemming algorithm. Allowed values are
 * German and English TODO: extend list, use locale</li>
 * <li>update(Boolean): If update is true, the tokens are overwritten with the stemmed tokens. Otherwise the stemmed
 * tokens are saved in a feature named StemmedTokens.</li>
 * <li>CreateMapping(Boolean): Store a mapping of stemmed token to all original tokens in the document. Feature can be
 * accessed via StemMapping.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>Document(Document): The enriched document.</li>
 * </ul>
 * </ul>
 * 
 * @author Florian Schulz
 */
public class StemTokens extends DocumentOperatorSpecification {

	private static final long serialVersionUID = 7325107375813081220L;

	public static final String LANGUAGE = "Language";
	public static final String GERMAN = "German";
	public static final String ENGLISH = "English";
	public static final String STEMMED_TOKENS = "StemmedTokens";
	public static final String CREATE_MAPPING = "CreateMapping";
	public static final String STEM_MAPPING = "StemMapping";

	/**
	 * Create new Operator that stemms tokens.
	 */
	public StemTokens() {
		this.addParameterType(LANGUAGE, String.class, false, ENGLISH, Arrays.asList(new String[] { GERMAN, ENGLISH }));
		this.addParameterType(ParameterIdentifiers.UPDATE, Boolean.class, false, true);
		this.addParameterType(CREATE_MAPPING, Boolean.class, false, false);
	}

	@Override
	protected Container run(Container inputParameters) {
		Container result = new Container(this.getOutputSignature());
		Map<String, Set<String>> stemMapping = new HashMap<String, Set<String>>();

		String language = (String) inputParameters.get(LANGUAGE);
		boolean update = (Boolean) inputParameters.get(ParameterIdentifiers.UPDATE);
		Document doc = (Document) inputParameters.get(ObjectIdentifiers.DOCUMENT);
		boolean createMapping = (Boolean) inputParameters.get(CREATE_MAPPING);

		Assert.isTrue(doc.hasFeature(Features.TOKENS));

		SnowballStemmer stemmer = this.createStemmer(language);

		List<String> tokens = doc.getFeature(Features.TOKENS);

		List<String> stemmedTokens = new ArrayList<String>();
		for (String token : tokens) {
			String stemmedToken = this.stemToken(stemmer, token);
			stemmedTokens.add(stemmedToken);

			if (createMapping) {
				this.addTokenToMapping(stemMapping, token, stemmedToken);
			}
		}

		this.setStemmedTokensAsDocumentFeature(update, doc, stemmedTokens);

		this.setStemMappingAsDocumentFeature(stemMapping, doc, createMapping);

		result.setObject(ObjectIdentifiers.DOCUMENT, doc);
		return result;
	}

	private void setStemMappingAsDocumentFeature(Map<String, Set<String>> stemMapping, Document doc,
			boolean createMapping) {
		if (createMapping) {
			if (!Features.getInstance().isFeature(STEM_MAPPING)) {
				Features.getInstance().addFeature(STEM_MAPPING, Map.class);
			}
			doc.setFeature(STEM_MAPPING, stemMapping);
		}
	}

	private void setStemmedTokensAsDocumentFeature(boolean update, Document doc, List<String> stemmedTokens) {
		if (update) {
			doc.setFeature(Features.TOKENS, stemmedTokens);
		} else {
			if (!Features.getInstance().isFeature(STEMMED_TOKENS)) {
				Features.getInstance().addFeature(STEMMED_TOKENS, List.class);
			}
			doc.setFeature(STEMMED_TOKENS, stemmedTokens);
		}
	}

	private String stemToken(SnowballStemmer stemmer, String token) {
		stemmer.setCurrent(token);
		stemmer.stem();
		String stemmedToken = stemmer.getCurrent();
		return stemmedToken;
	}

	private void addTokenToMapping(Map<String, Set<String>> stemMapping, String token, String stemmedToken) {
		Set<String> declinedTokens = stemMapping.get(stemmedToken);
		if (declinedTokens == null) {
			declinedTokens = new TreeSet<String>();
			stemMapping.put(stemmedToken, declinedTokens);
		}
		declinedTokens.add(token);
	}

	private SnowballStemmer createStemmer(String language) {
		SnowballStemmer stemmer = null;
		if (ENGLISH.equals(language)) {
			stemmer = new englishStemmer();
		}
		if (GERMAN.equals(language)) {
			stemmer = new germanStemmer();
		}
		return stemmer;
	}
}
