package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

/**
 * Notifies the client about the number of misspellings in an ELO.
 * ("misspellings":String, <URI>:String, <TS>:Long, <NumberOfErrors>:Integer,
 * <User>:String) -> Notifies client.
 * 
 * @author Florian Schulz
 * 
 */
public class MisspellingNotificationAgent extends AbstractThreadedAgent {

	private static final String NOTIFY_ABOUT_MISSPELLINGS_NAME = "NotifyAboutMisspellings";
	private boolean stopped;

	/**
	 * Create a new MisspellingNotificationAgent. The argument <code>map</code>
	 * is used to initialize special parameters.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public MisspellingNotificationAgent(Map<String, Object> map) {
		super(NOTIFY_ABOUT_MISSPELLINGS_NAME, (String) map.get("id"));
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getCommandSpace().waitToTake(getTemplateTuple(),
						AgentProtocol.ALIVE_INTERVAL);
				String uri = (String) trigger.getField(1).getValue();
				Integer numberOfErrors = (Integer) trigger.getField(3)
						.getValue();
				String user = (String) trigger.getField(4).getValue();

//				INotification notification = new Notification();
//				notification.addProperty("errors", "" + numberOfErrors);
//				notification.addProperty("target", "misspellings");
//				notification.addProperty("eloURI", uri);
//				getNotificationSender().send(user, "textpad", notification);
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

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
