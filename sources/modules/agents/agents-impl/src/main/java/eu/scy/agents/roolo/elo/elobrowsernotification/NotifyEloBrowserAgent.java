package eu.scy.agents.roolo.elo.elobrowsernotification;

import java.util.Map;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractCommunicationAgent;
import eu.scy.notification.Notification;

/**
 * Notifies the eloBrowser that an elo has been saved.
 * 
 * ("notifyEloBrowser":String, <ELOUri>:String) -> Notification at the client
 * side ()
 * 
 * @author fschulz_2
 * 
 */
public class NotifyEloBrowserAgent extends AbstractCommunicationAgent {

	public static final String NOTIFY_ELO_BROWSER_AGENT_NAME = "NotifyEloBrowserAgent";
	private boolean stopped;

	public NotifyEloBrowserAgent(Map<String, Object> map) {
		super(NOTIFY_ELO_BROWSER_AGENT_NAME, (String) map.get("id"));
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getTupleSpace().waitToTake(getTemplateTuple());

				String eloUri = (String) trigger.getField(1).getValue();

				Notification notification = new Notification();
				notification.addProperty("eloUri", eloUri);
				notification.addProperty("target", "elobrowser");
				getNotificationSender().send("roolo", "roolo", notification);
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
    protected Tuple getIdentifyTuple() {
        // TODO Auto-generated method stub
        return null;
    }

}
