package eu.scy.agents.hypothesis.workflow.operators;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;
import de.fhg.iais.kd.tm.obwious.util.Assert;
import eu.scy.agents.util.Utilities;

/**
 * Takes the feature specified in parameter baseText (default:text), which is a
 * single string and generates a feature tokens, which is represented as a list.
 * The delimiters are regular expressions which have to be applied in a special
 * order
 * 
 * @author Jörg Kindermann
 */
public class RegExSentenceSplitter extends DocumentOperatorSpecification {

	/**
	 * Should documents really be updated. true, false
	 */
	public static final String UPDATE = "update";

	/**
	 * Remove Numbers from texts. true, false
	 */
	public static final String BASE_TEXT = "baseText";

	/**
	 * regular expressions needed to tokenize text and split into sentences
	 */
	public static final String ALPHA_PATTERN = "a-zA-ZäöüßáéíúóúàèìòùãõâêîôûåäëïöüçñøýÿÄÖÜÁÉÍÚÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇÑØÝ";

	public static final String URL_PATTERN = "(http|https)://[-_\\.\\~" + ALPHA_PATTERN
			+ "0-9/=&\\?]+";

	public static final String EMAIL_PATTERN = "[-" + ALPHA_PATTERN + "0-9\\.]+@[-" + ALPHA_PATTERN
			+ "0-9\\.]+";

	public static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])[\\./](0?[1-9]|1[012])[\\./]((19|20)\\d\\d)";

	public static final String MONTH_PATTERN = "(0?[1-9]|[12][0-9]|3[01])[\\./] *([A-Z][a-z]+)([\\./]*((19|20)\\d\\d))*";

	public static final String TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

	public static final String NUMBER_PATTERN = "[\\-\\+]*[0-9]+([,\\.][0-9]+)*";

	public static final String WORD_PATTERN = "[" + ALPHA_PATTERN + "°\\'\\`²]+";

	public static final String PHRASE_PATTERN = WORD_PATTERN + "([-&]" + WORD_PATTERN + ")*";

	public static final String PUNCT_PATTERN = "-\\!\"/\\?\\\\\\*\\+\\~_:\\.,;=#";

	public static final String BRACKET_PATTERN = "]\\(\\)\\[\\}\\{><";

	public static final String SENTENCE_DELIMITER = "(W[\\.:\\!;\\?]W|\\)[\\.:\\!;\\?]W|N[\\.:\\!;\\?]W|W[\\.:\\!;\\?]N|\"[\\.:\\!;\\?]W|\\.\\.\\.)";

	public static final long serialVersionUID = 1L;

	/**
	 * Construct new Operator that tokenizes Text to sentences.
	 */
	public RegExSentenceSplitter() {
		super();
		this.addParameterType(BASE_TEXT, JavaClasses.STRING, false, Features.TEXT);
		this.addParameterType(UPDATE, JavaClasses.BOOLEAN, false, Boolean.FALSE);
	}

	@Override
	public Container run(Container inputParameters) {

		Document document = (Document) inputParameters.getObject(ObjectIdentifiers.DOCUMENT);
		// logger.info("Tokenizing document to sentence tokens" +
		// document.getId());

		String baseText = (String) inputParameters.getObject(BASE_TEXT);
		Boolean update = (Boolean) inputParameters.getObject(UPDATE);

		Container output = new Container(getOutputSignature());

		// Define feature
		if (!Features.getInstance().isFeature(Features.SENTENCES)) {
			Features.getInstance().addFeature(Features.SENTENCES, JavaClasses.LIST);
		} else {
			Assert.areSame(Features.getInstance().getType(Features.SENTENCES), JavaClasses.LIST);
		}

		// Compute feature
		if (!document.hasFeature(Features.SENTENCES) || update) {

			// Required features
			Assert.isTrue(Features.getInstance().isFeature(baseText));
			Assert.isTrue(document.hasFeature(baseText));

			String text = (String) document.getFeature(baseText);

			text = text.replaceAll(URL_PATTERN, " W ");
			text = text.replaceAll(EMAIL_PATTERN, " W ");
			text = text.replaceAll(DATE_PATTERN, " W ");
			text = text.replaceAll(MONTH_PATTERN, " W ");
			text = text.replaceAll(TIME_PATTERN, " W ");
			text = text.replaceAll(NUMBER_PATTERN, " N ");
			text = text.replaceAll("([" + PUNCT_PATTERN + "])+", " $1 ");
			text = text.replaceAll("([" + BRACKET_PATTERN + "])+", " $1 ");

			text = text.trim();

			String xx = text;
			xx = xx.replaceAll(PHRASE_PATTERN, " W ");
			xx = xx.trim();

			String[] s = Utilities.tokenize(text);
			String[] x = Utilities.tokenize(xx);

			List<String> sentences = new ArrayList<String>();

			if (x.length < 3) {
				sentences.add(text);
			} else {
				// construct pattern sequences to be matched against sentence
				// separator patterns.
				String[] zz = new String[x.length - 2];
				for (int i = 0; i < zz.length; i++) {
					zz[i] = x[i] + x[i + 1] + x[i + 2];
				}

				ArrayList<Integer> idx = new ArrayList<Integer>();
				for (int i = 0; i < zz.length; i++) {
					if (zz[i].matches(SENTENCE_DELIMITER)) {
						idx.add(i + 1);
					}
				}
				// we need a special treatment of the last sentence, because its
				// separator will not match the SENTENCE_DELIMITER. Therefore we
				// add the index of the last term in text:
				idx.add(zz.length);
				int idxStart = 0;
				for (int i = 0; i < idx.size(); i++) {
					int idxEnd = idx.get(i);
					String sent = "";
					for (int j = idxStart; j <= idxEnd; j++) {
						sent = sent + " " + s[j];
					}
					sentences.add(sent);
					idxStart = idxEnd + 1;
				}
				if (sentences.isEmpty()) {
					sentences.add(text);
				}
			}
			document.setFeature(Features.SENTENCES, sentences);
		}

		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}

}
