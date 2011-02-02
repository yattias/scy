package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class TextForTopicSaved extends AbstractELOSavedAgent {

	public static final String NAME = TextForTopicSaved.class.getName();

	public TextForTopicSaved(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	private boolean isValidType(String eloType) {
		if ("scy/text".equals(eloType)) {
			return true;
		}
		return false;
	}

	private void writeELOSavedTuple(String eloUri) {
		// System.out.println("writing tuple");
		try {
			getCommandSpace().write(new Tuple(TopicAgents.TOPIC_DETECTOR, eloUri));
		} catch (TupleSpaceException e) {
			// System.out.println("error");
			e.printStackTrace();
		}
		// System.out.println("tuple written");
	}

	@Override
  public void processELOSavedAction(String actionId, String user, long timeInMillis, String tool,
                                    String mission, String session, String eloUri, String eloType) {
		// System.out.println(eloUri);
		// System.out.println(eloType);
		if (isValidType(eloType)) {
			writeELOSavedTuple(eloUri);
		}

	}
}
