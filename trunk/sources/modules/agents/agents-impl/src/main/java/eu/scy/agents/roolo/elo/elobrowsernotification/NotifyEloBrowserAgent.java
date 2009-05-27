package eu.scy.agents.roolo.elo.elobrowsernotification;

import info.collide.sqlspaces.commons.Tuple;
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

	public NotifyEloBrowserAgent() {
		super(NOTIFY_ELO_BROWSER_AGENT_NAME);
	}

	@Override
	protected void doRun(Tuple trigger) {
		String eloUri = (String) trigger.getField(1).getValue();

		Notification notification = new Notification();
		notification.addProperty("eloUri", eloUri);
		notification.addProperty("target", "elobrowser");
		getNotificationSender().send("roolo", "roolo", notification);
	}

	@Override
	protected Tuple getTemplateTuple() {
		return new Tuple("notifyEloBrowser", String.class);
	}

}
