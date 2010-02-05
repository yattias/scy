package eu.scy.agents.roolo.elo.elobrowsernotification;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Notifies the eloBrowser that an elo has been saved. ("notifyEloBrowser":String, <ELOUri>:String) -> Notification at
 * the client side ()
 * 
 * @author Florian Schulz
 */
public class NotifyEloBrowserAgent extends AbstractThreadedAgent {

	/**
	 * Name of the agent.
	 */
	public static final String NOTIFY_ELO_BROWSER_AGENT_NAME = "NotifyEloBrowserAgent";
	private boolean stopped;

	/**
	 * Create a new NotifyEloBrowserAgent filtering agent. The argument <code>map</code> is used to initialize special
	 * parameters. Never used here.
	 * 
	 * @param map Parameters needed to initialize the agent.
	 */
	public NotifyEloBrowserAgent(Map<String, Object> map) {
		super(NOTIFY_ELO_BROWSER_AGENT_NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID));
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getCommandSpace().waitToTake(getTemplateTuple());

				String eloUri = (String) trigger.getField(1).getValue();

				// Notification notification = new Notification();
				// notification.addProperty("eloUri", eloUri);
				// notification.addProperty("target", "elobrowser");
				// getNotificationSender().send("roolo", "roolo", notification);
			} catch (TupleSpaceException e) {
				stop();
			}
		}
		stopped = true;
	}

	private Tuple getTemplateTuple() {
		return new Tuple("notifyEloBrowser", String.class);
	}

	@Override
	protected void doStop() {
		// do nothing;
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
