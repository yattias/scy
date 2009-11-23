package eu.scy.agents.api;

import eu.scy.notification.api.INotificationSender;

/**
 * An interface for communication agents. It allows to get an instance of a
 * notification sender to speak to the client/scylab.
 * 
 * @author Florian Schulz
 * 
 */
public interface ICommunicationAgent extends IThreadedAgent {

	/**
	 * Get a notification sender to send the notifications to.
	 * 
	 * @return An instance of a notification sender.
	 */
	public INotificationSender getNotificationSender();

}
