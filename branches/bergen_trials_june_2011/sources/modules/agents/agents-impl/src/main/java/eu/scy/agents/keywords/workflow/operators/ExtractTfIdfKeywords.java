package eu.scy.agents.keywords.workflow.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

/**
 * Implements an OBWIOUS operator that
 * <ul>
 * <li><b>Inputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description.</li>
 * </ul>
 * <li><b>Parameters</b></li>
 * <ul>
 * <li>ParameterId(ParameterType): Description including range, requirement, etc.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description</li>
 * </ul>
 * </ul>
 * 
 * @author fschulz_2
 */
public class ExtractTfIdfKeywords extends DocumentOperatorSpecification {

	private static final long serialVersionUID = 1L;

	public ExtractTfIdfKeywords() {
		super();
		addParameterType(KeywordWorkflowConstants.NUMBER_OF_KEYWORDS, Integer.class, false, 10);
	}

	@Override
	protected Container run(Container inputParameters) {

		Document document = (Document) inputParameters.get(ObjectIdentifiers.DOCUMENT);
		Map<String, Double> tfIdf = document.getFeature(Features.TFIDF);
		int numberOfKeywords = (Integer) inputParameters.get(KeywordWorkflowConstants.NUMBER_OF_KEYWORDS);

		Map<String, Set<String>> stemMapping = document.getFeature(StemTokens.STEM_MAPPING);
		ArrayList<Entry<String, Double>> sortedTerms = sortTermsByTfIdf(tfIdf);

		defineFeature();
		Set<String> keywords = getTfIdfKeywords(sortedTerms, numberOfKeywords, stemMapping);
		document.setFeature(KeywordWorkflowConstants.TFIDF_KEYWORDS, keywords);

		Container output = new Container(getOutputSignature());
		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}

	private void defineFeature() {
		if (!Features.getInstance().isFeature(KeywordWorkflowConstants.TFIDF_KEYWORDS)) {
			Features.getInstance().addFeature(KeywordWorkflowConstants.TFIDF_KEYWORDS, Set.class);
		}
	}

	private ArrayList<Entry<String, Double>> sortTermsByTfIdf(Map<String, Double> tfIdf) {
		ArrayList<Entry<String, Double>> sortedTerms = new ArrayList<Entry<String, Double>>();

		sortedTerms.addAll(tfIdf.entrySet());

		Collections.sort(sortedTerms, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return -1 * o1.getValue().compareTo(o2.getValue());
			}
		});
		return sortedTerms;
	}

	private Set<String> getTfIdfKeywords(ArrayList<Entry<String, Double>> sortedTerms, int number,
			Map<String, Set<String>> stemMapping) {
		Set<String> result = new HashSet<String>();
		int i = 0;
		for (Entry<String, Double> entry : sortedTerms) {
			String keyword = entry.getKey();
			if (stemMapping.containsKey(keyword)) {
				keyword = getRepresentativeTerm(keyword, stemMapping);
			}
			result.add(keyword);
			i++;
			if (i == number) {
				break;
			}
		}
		return result;
	}

	private String getRepresentativeTerm(String keyword, Map<String, Set<String>> stemMapping) {
		String representativeTerm = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		Set<String> declinedTokens = stemMapping.get(keyword);
		for (String declinedToken : declinedTokens) {
			if (representativeTerm.length() > declinedToken.length()) {
				representativeTerm = declinedToken;
			}
		}
		return representativeTerm;
	}
}
