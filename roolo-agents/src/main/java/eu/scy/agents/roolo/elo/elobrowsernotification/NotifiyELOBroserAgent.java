package eu.scy.agents.roolo.elo.elobrowsernotification;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.ICommunicationAgent;
import eu.scy.agents.impl.elo.AbstractELOAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotification;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class NotifiyELOBroserAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> implements ICommunicationAgent<K> {

	private NotificationSender sender;

	public NotifiyELOBroserAgent() {
		sender = new NotificationSender("scy.collide.info", 2525,
				"notifications");
	}

	@Override
	public void processElo(T elo) {
		if (elo == null) {
			return;
		}
		INotification notification = new Notification();
		if (elo.getUri() == null) {
			return;
		}
		notification.addProperty("eloUri", elo.getUri().toString());
		notification.addProperty("target", "elobrowser");
		sender.send("roolo", "roolo", notification);
	}

	@Override
	public ToolBrokerAPI<K> getToolBrokerAPI() {
		return new ToolBrokerImpl<K>();
	}

	@Override
	public void run() {
		// agent is only activated on process elo.
	}

}
