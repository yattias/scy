package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.impl.AbstractCommunicationAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

public class SearchForSimilarConceptNotificationAgent extends
		AbstractCommunicationAgent {

	private static final String SEARCH_FOR_SIMILAR_CONCEPT_NOTIFICATION_AGENT = "SearchForSimilarConceptNotificationAgent";
	private boolean stopped;

	public SearchForSimilarConceptNotificationAgent() {
		super(SEARCH_FOR_SIMILAR_CONCEPT_NOTIFICATION_AGENT);
	}

	@Override
	protected void doRun() {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getTupleSpace().waitToTake(
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

				getNotificationSender().send(user, "searchSimilarElosAgent",
						notification);
				System.err.println();
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
}
