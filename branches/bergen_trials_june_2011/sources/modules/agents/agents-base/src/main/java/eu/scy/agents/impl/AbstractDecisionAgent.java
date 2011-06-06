package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.Base64;
import eu.scy.agents.api.IDecisionAgent;

/**
 * Basic implementation of {@link IDecisionAgent}.
 * 
 * @author Florian Schulz
 * 
 */
public abstract class AbstractDecisionAgent extends AbstractThreadedAgent
		implements IDecisionAgent {

	/**
	 * Create a new {@link AbstractDecisionAgent}.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	protected AbstractDecisionAgent(String name, String id) {
		super(name, id);
	}

	protected String convertBinaryToString(byte[] bytes) {
		return Base64.encodeToString(bytes, true);
	}

	protected void sendNotification(String user, String tool, String mission,
			String session, String... payload) {
		Tuple notificationTuple = createNotificationTuple(user, tool, mission,
				session, payload);
		sendNotificationTuple(notificationTuple);
	}

	private Tuple createNotificationTuple(String user, String tool,
			String mission, String session, String... payload) {
		Tuple notificationTuple = new Tuple("notification", user, tool,
				mission, session);
		for (String pair : payload) {
			notificationTuple.add(pair);
		}
		return notificationTuple;
	}

	private void sendNotificationTuple(Tuple notificationTuple) {
		try {
			getCommandSpace().write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
