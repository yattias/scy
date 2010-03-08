package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.system.feature.atomic.ProvideTermFrequency;
import de.fhg.iais.kd.tm.obwious.operator.system.feature.modelbased.ProvideTfIdf;
import eu.scy.agents.keywords.workflow.operators.ExtractTopicModelKeywords;
import eu.scy.agents.keywords.workflow.operators.ImprovedTokenize;
import eu.scy.agents.keywords.workflow.operators.RemoveStopWords;
import eu.scy.agents.keywords.workflow.operators.StemTokens;

public class ExtractTopicModelKeywordsWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String PROVIDE_TERM_FREQUENCY = "ProvideTermFrequency";
	private static final String PROVIDE_TOKENS = "ProvideTokens";
	private static final String TFIDF = "TfIdf";
	private static final String STEM_TOKENS = "StemTokens";
	private static final String EXTRACT_TOPIC_MODEL_KEYWORDS = "ExtractTfIdfKeywords";
	private static final String REMOVE_STOPWORDS = "RemoveStopwords";

	public ExtractTopicModelKeywordsWorkflow() {
		this(new Properties());
	}

	public ExtractTopicModelKeywordsWorkflow(Properties props) {
		super(props);

		addOperatorSpecification(PROVIDE_TOKENS, ImprovedTokenize.class);
		setInputParameter(PROVIDE_TOKENS, "toLower", true);
		setInputParameter(PROVIDE_TOKENS, "removePunctuation", true);
		addOperatorSpecification(REMOVE_STOPWORDS, RemoveStopWords.class);
		// URL url = this.getClass().getResource("/english_stopWords.txt");
		// try {
		// setInputParameter(REMOVE_STOPWORDS, ParameterIdentifiers.FILENAME, new File(url.toURI()).getAbsolutePath());
		// } catch (URISyntaxException e) {
		// e.printStackTrace();
		// }
		// setInputParameter(REMOVE_STOPWORDS, "delimiter", "\n");

		addOperatorSpecification(STEM_TOKENS, StemTokens.class);
		setInputParameter(STEM_TOKENS, StemTokens.CREATE_MAPPING, true);
		addOperatorSpecification(PROVIDE_TERM_FREQUENCY, ProvideTermFrequency.class);

		addOperatorSpecification(TFIDF, ProvideTfIdf.class);
		addOperatorSpecification(EXTRACT_TOPIC_MODEL_KEYWORDS, ExtractTopicModelKeywords.class);

		addNamespaceLink(TFIDF, KeywordConstants.DOCUMENT_FREQUENCY);
		addNamespaceLink(EXTRACT_TOPIC_MODEL_KEYWORDS, Features.DOCUMENTFREQUENCY,
				KeywordConstants.DOCUMENT_FREQUENCY_MODEL);

		addNamespaceLink(TFIDF, KeywordConstants.DOCUMENT_FREQUENCY, KeywordConstants.DOCUMENT_FREQUENCY_MODEL);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}
}
