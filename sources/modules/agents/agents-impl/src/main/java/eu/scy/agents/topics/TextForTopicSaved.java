package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.ActionLogConstants;
import eu.scy.agents.impl.AbstractELOSavedAgent;

public class TextForTopicSaved extends AbstractELOSavedAgent {

	public static final String NAME = TextForTopicSaved.class.getName();

	public TextForTopicSaved(Map<String, Object> params) {
		super(NAME, (String) params.get("id"));
		if (params.containsKey("tsHost")) {
			host = (String) params.get("tsHost");
		}
		if (params.containsKey("tsPort")) {
			port = (Integer) params.get("tsPort");
		}
	}

	private boolean isValidType(String eloType) {
		if ("scy/text".equals(eloType)) {
			return true;
		}
		return false;
	}

	@Override
	protected void processELOSavedAction(IAction action) {
		String eloUri = action.getAttribute(ActionLogConstants.ELO_URI);
		String eloType = action.getAttribute(ActionLogConstants.ELO_TYPE);
		System.out.println(eloUri);
		System.out.println(eloType);
		if (isValidType(eloType)) {
			writeELOSavedTuple(eloUri);
		}
	}

	private void writeELOSavedTuple(String eloUri) {
		System.out.println("writing tuple");
		try {
			getCommandSpace().write(new Tuple(TopicAgents.TOPIC_DETECTOR, eloUri));
		} catch (TupleSpaceException e) {
			System.out.println("error");
			e.printStackTrace();
		}
		System.out.println("tuple written");
	}
}
