package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

/**
 * Notifies the client about the users found that produced similar concept maps
 * and are potential collaboration partners.
 * 
 * @author Florian Schulz
 * 
 */
public class SearchForSimilarConceptNotificationAgent extends
		AbstractThreadedAgent {

	private static final String SEARCH_FOR_SIMILAR_CONCEPT_NOTIFICATION_AGENT = "SearchForSimilarConceptNotificationAgent";
	private boolean stopped;

	/**
	 * Create a new SearchForSimilarConceptNotificationAgent filtering agent.
	 * The argument <code>map</code> is used to initialize special parameters.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public SearchForSimilarConceptNotificationAgent(Map<String, Object> map) {
		super(SEARCH_FOR_SIMILAR_CONCEPT_NOTIFICATION_AGENT, (String) map
				.get("id"));
	}

	@Override
	protected void doRun() {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getCommandSpace().waitToTake(
						new Tuple("searchSimilarElosAgent", String.class,
								String.class, String.class),
						AgentProtocol.ALIVE_INTERVAL);
				if (trigger == null) {
					continue;
				}

				String relatedUserList = (String) trigger.getField(1)
						.getValue();
				String user = (String) trigger.getField(2).getValue();
				String eloURI = (String) trigger.getField(3).getValue();

				INotification notification = new Notification();
				notification.addProperty("users", relatedUserList);
				notification.addProperty("target", "awareness");
				notification.addProperty("eloUri", eloURI);

//				getNotificationSender().send(user, "searchSimilarElosAgent",
//						notification);
//				System.err.println();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
		stopped = true;
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

	@Override
	protected void doStop() {
		// nothing to do
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}
}
