package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import eu.scy.agents.keywords.workflow.operators.ExtractTopicModelKeywords;

public class ExtractTopicModelKeywordsWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String EXTRACT_TOPIC_MODEL_KEYWORDS = "ExtractTfIdfKeywords";

	private static final String PREPROCESSING = "Preprocessing";

	public ExtractTopicModelKeywordsWorkflow() {
		this(new Properties());
	}

	public ExtractTopicModelKeywordsWorkflow(Properties props) {
		super(props);

		addOperatorSpecification(PREPROCESSING, Preprocessing.class);

		addOperatorSpecification(EXTRACT_TOPIC_MODEL_KEYWORDS, ExtractTopicModelKeywords.class);
		addNamespaceLink(EXTRACT_TOPIC_MODEL_KEYWORDS, Features.DOCUMENTFREQUENCY,
				KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}
}
