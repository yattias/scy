package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractCommunicationAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

/**
 * ("misspellings":String, <URI>:String, <TS>:Long, <NumberOfErrors>:Integer,
 * <User>:String) -> Notifies client.
 * 
 * @author fschulz_2
 * 
 */
public class MisspellingNotificationAgent extends AbstractCommunicationAgent {

	private static final String NOTIFY_ABOUT_MISSPELLINGS_NAME = "NotifyAboutMisspellings";
	private boolean stopped;

	public MisspellingNotificationAgent() {
		super(NOTIFY_ABOUT_MISSPELLINGS_NAME);
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getTupleSpace().waitToTake(getTemplateTuple());
				String uri = (String) trigger.getField(1).getValue();
				Integer numberOfErrors = (Integer) trigger.getField(3)
						.getValue();
				String user = (String) trigger.getField(4).getValue();

				INotification notification = new Notification();
				notification.addProperty("errors", "" + numberOfErrors);
				notification.addProperty("target", "misspellings");
				notification.addProperty("eloURI", uri);
				getNotificationSender().send(user, "textpad", notification);
			} catch (TupleSpaceException e) {
				stop();
			}
		}
		stopped = true;
	}

	private Tuple getTemplateTuple() {
		return new Tuple("misspellings", String.class, Long.class,
				Integer.class, String.class);
	}

	@Override
	protected void doStop() {
		// nothing to do
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

}
