package eu.scy.agents.hypothesis.workflow.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.util.Utilities;

/**
 * computes a histogram of number of keywords per sentence:
 * 
 * 
 * @author Jörg Kindermann
 */
public class ComputeKeywordsInSentenceHistogram extends OperatorSpecification {

	private final static Logger logger = Logger
			.getLogger(ComputeKeywordsInSentenceHistogram.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Should documents really be updated. true, false
	 */
	public static final String UPDATE = "update";

	/**
	 * Construct new Operator that spots keywords in sentences and computes a
	 * keyword-in-sentence ratio.
	 */
	public ComputeKeywordsInSentenceHistogram() {
		super();
		this.addParameterType(UPDATE, JavaClasses.BOOLEAN, false, Boolean.TRUE);
		this.addInputType(ObjectIdentifiers.DOCUMENT, Document.class);
		this.addOutputType(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM,
				HashMap.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Container run(Container inputParameters) {

		Document document = (Document) inputParameters
				.getObject(ObjectIdentifiers.DOCUMENT);
		if (!Features.getInstance().isFeature(
				KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM)) {
			Features.getInstance().addFeature(
					KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM, Map.class);
		}
		ArrayList<String> sentences = (ArrayList<String>) document
				.getFeature(Features.SENTENCES);
		ArrayList<String> keywords = (ArrayList<String>) document
				.getFeature(Features.WORDS);

		Iterator<String> it = sentences.iterator();
		HashMap<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		while (it.hasNext()) {
			String sentString = (String) it.next();
			String[] sentence = Utilities.tokenize(sentString);
			int count = 0;
			for (int i = 0; i < sentence.length; i++) {
				String term = sentence[i];
				if (keywords.contains(term)) {
					count += 1;
				}
			}
			if (histogram.containsKey(count)) {
				histogram.put(count, histogram.get(count) + 1);
			} else {
				histogram.put(count, 1);
			}
		}
		logger.info(histogram.toString());
		document.setFeature(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM,
				histogram);
		Container output = new Container(getOutputSignature());
		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}

}
