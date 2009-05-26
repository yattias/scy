package eu.scy.agents.api;

import eu.scy.notification.api.INotificationSender;
import roolo.elo.api.IMetadataKey;

public interface ICommunicationAgent<K extends IMetadataKey> extends
		IThreadedAgent {

	/**
	 * Get a notification sender to send the notifications to.
	 * 
	 * @return
	 */
	public INotificationSender getNotificationSender();

}
