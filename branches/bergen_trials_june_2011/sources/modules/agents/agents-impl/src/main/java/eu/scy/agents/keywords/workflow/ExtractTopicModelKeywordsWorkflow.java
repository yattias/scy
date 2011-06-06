package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import eu.scy.agents.keywords.workflow.operators.ExtractTopicModelKeywords;
import eu.scy.agents.util.TMParameters;

public class ExtractTopicModelKeywordsWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String EXTRACT_TOPIC_MODEL_KEYWORDS = "ExtractTfIdfKeywords";

	private static final String PREPROCESSING = "Preprocessing";

	public ExtractTopicModelKeywordsWorkflow() {
		this(new Properties());
	}

	public ExtractTopicModelKeywordsWorkflow(Properties props) {
		super(props);

	    props.put(TMParameters.REMOVE_HTML_TAGS, "RemoveHTMLTags");
	    props.put(TMParameters.PROVIDE_TERM_FREQUENCY, "ProvideTermFrequency");
	    props.put(TMParameters.PROVIDE_TOKENS, "ProvideTokens");
	    props.put(TMParameters.STEM_TOKENS, "StemTokens");
	    props.put(TMParameters.REMOVE_STOPWORDS, "RemoveStopwords");
	    Preprocessing preprocessor = new Preprocessing(props);
	    addOperatorSpecification(PREPROCESSING, preprocessor);

		addOperatorSpecification(EXTRACT_TOPIC_MODEL_KEYWORDS, ExtractTopicModelKeywords.class);
		addNamespaceLink(EXTRACT_TOPIC_MODEL_KEYWORDS, Features.DOCUMENTFREQUENCY,
				KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}
}
