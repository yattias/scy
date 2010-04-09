package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import eu.scy.agents.keywords.workflow.operators.ExtractTfIdfKeywords;

public class ExtractTfIdfKeywordsWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String EXTRACT_TFIDF_KEYWORDS = "ExtractTopicModelKeywords";

	private static final String PREPROCESSING = "Preprocessing";

	public ExtractTfIdfKeywordsWorkflow() {
		this(new Properties());
	}

	public ExtractTfIdfKeywordsWorkflow(Properties props) {
		super(props);

		addOperatorSpecification(PREPROCESSING, Preprocessing.class);

		addOperatorSpecification(EXTRACT_TFIDF_KEYWORDS, ExtractTfIdfKeywords.class);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}
}
