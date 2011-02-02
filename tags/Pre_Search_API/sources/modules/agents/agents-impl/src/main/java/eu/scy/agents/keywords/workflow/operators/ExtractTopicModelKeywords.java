package eu.scy.agents.keywords.workflow.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
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
 * <li>ParameterId(ParameterType): Description including range, requirement,
 * etc.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description</li>
 * </ul>
 * </ul>
 * 
 * @author fschulz_2
 */
public class ExtractTopicModelKeywords extends DocumentOperatorSpecification {

	private static final long serialVersionUID = 1L;

	public ExtractTopicModelKeywords() {
		super();
		addInputType(KeywordWorkflowConstants.TOPIC_MODEL,
				ParallelTopicModel.class);
		addParameterType(KeywordWorkflowConstants.NUMBER_OF_KEYWORDS,
				Integer.class, false, 20);
	}

	@Override
	protected Container run(Container inputParameters) {
		ParallelTopicModel lda = (ParallelTopicModel) inputParameters
				.get(KeywordWorkflowConstants.TOPIC_MODEL);
		Set<String> topFeaturesBag = getTopFeatures(lda, 0.003);
		Document document = (Document) inputParameters
				.get(ObjectIdentifiers.DOCUMENT);
		int numberOfFeatures = (Integer) inputParameters
				.get(KeywordWorkflowConstants.NUMBER_OF_KEYWORDS);

		Map<String, Set<String>> stemMapping = document
				.getFeature(StemTokens.STEM_MAPPING);
		Map<String, Double> tfIfdMap = document.getFeature(Features.TFIDF);

		List<Entry<String, Double>> sortedTerms = getByTfIdfSortedTerms(tfIfdMap);
		Set<String> tmKeywords = getTopicModelKeywords(sortedTerms,
				stemMapping, topFeaturesBag, numberOfFeatures);

		defineFeature();
		document.setFeature(KeywordWorkflowConstants.TM_KEYWORDS, tmKeywords);

		Container output = new Container(getOutputSignature());
		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}

	private List<Entry<String, Double>> getByTfIdfSortedTerms(
			Map<String, Double> tfIfdMap) {
		List<Entry<String, Double>> sortedTerms = new ArrayList<Entry<String, Double>>(
				tfIfdMap.entrySet());
		Collections.sort(sortedTerms, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return -1 * o1.getValue().compareTo(o2.getValue());
			}
		});
		return sortedTerms;
	}

	private void defineFeature() {
		if (!Features.getInstance().isFeature(
				KeywordWorkflowConstants.TM_KEYWORDS)) {
			Features.getInstance().addFeature(
					KeywordWorkflowConstants.TM_KEYWORDS, Set.class);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<String> getTopFeatures(ParallelTopicModel lda, double threshold) {
		Set<IDSorter>[] topWordsPerTopic = lda.getSortedWords();

		Alphabet alphabet = lda.getAlphabet();
		HashSet<String> topFeaturesBag = new HashSet<String>();

		for (int topic = 0; topic < topWordsPerTopic.length; topic++) {
			Set<IDSorter> topWords = topWordsPerTopic[topic];
			for (IDSorter sorter : topWords) {
				if (sorter.getWeight() > threshold) {
					String term = (String) alphabet
							.lookupObject(sorter.getID());
					topFeaturesBag.add(term);
				}

			}
		}
		return topFeaturesBag;
	}

	private Set<String> getTopicModelKeywords(
			List<Entry<String, Double>> sortedTerms,
			Map<String, Set<String>> stemMapping, Set<String> topFeaturesBag,
			int number) {
		Set<String> keywords = new HashSet<String>();
		int i = 0;
		for (Entry<String, Double> entry : sortedTerms) {
			String term = entry.getKey();
			if (stemMapping.containsKey(term)) {
				term = getRepresentativeTerm(stemMapping, term);
			}
			if (topFeaturesBag.contains(term)) {
				keywords.add(term);

			}
			i++;
			if (i == number) {
				break;
			}
		}
		return keywords;
	}

	private String getRepresentativeTerm(Map<String, Set<String>> stemMapping,
			String keyword) {
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
