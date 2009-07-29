package eu.scy.agents.impl;

import eu.scy.agents.api.ICommunicationAgent;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotificationSender;

/**
 * The implementation of {@link ICommunicationAgent}.
 * 
 * @author Florian Schulz
 * 
 */
public abstract class AbstractCommunicationAgent extends AbstractThreadedAgent
		implements ICommunicationAgent {

	private INotificationSender notificationSender;

	/**
	 * Create a new {@link AbstractCommunicationAgent} with <code>name</code>
	 * and <code>id</code>.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	public AbstractCommunicationAgent(String name, String id) {
		super(name, id);
		notificationSender = new NotificationSender();
	}

	@Override
	public INotificationSender getNotificationSender() {
		return notificationSender;
	}

}
