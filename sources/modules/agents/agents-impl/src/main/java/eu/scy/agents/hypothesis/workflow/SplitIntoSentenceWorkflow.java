package eu.scy.agents.hypothesis.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import eu.scy.agents.hypothesis.workflow.operators.KeywordsInSentenceSpotter;
import eu.scy.agents.hypothesis.workflow.operators.RegExSentenceSplitter;

public class SplitIntoSentenceWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String SPLIT_INTO_SENTENCES = "SplitIntoSentences";
    private static final String COMPUTE_KEYWORDS_IN_SENTENCE_RATIO = "KeywordsInSentenceRatio";

	public SplitIntoSentenceWorkflow() {
		this(new Properties());
	}

	public SplitIntoSentenceWorkflow(Properties props) {
		super(props);

		addOperatorSpecification(SPLIT_INTO_SENTENCES, RegExSentenceSplitter.class);
        addOperatorSpecification(COMPUTE_KEYWORDS_IN_SENTENCE_RATIO, KeywordsInSentenceSpotter.class);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}
}
