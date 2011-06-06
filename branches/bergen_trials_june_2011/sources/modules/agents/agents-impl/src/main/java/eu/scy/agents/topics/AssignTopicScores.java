package eu.scy.agents.topics;

import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.system.feature.modelbased.ProvideTopicScoresFromText;

public class AssignTopicScores extends Workflow {

	private static final long serialVersionUID = 1L;

	private static final String PROVIDE_TOPIC_SCORES = "ProvideTopicScores";

	public AssignTopicScores() {
		addOperatorSpecification(PROVIDE_TOPIC_SCORES,
				ProvideTopicScoresFromText.class);
		addNamespaceLink(PROVIDE_TOPIC_SCORES, ObjectIdentifiers.DOCUMENT);
		addNamespaceLink(PROVIDE_TOPIC_SCORES, ObjectIdentifiers.MODEL);

		addDefaultOutputLinks();

		verify();
	}

}
