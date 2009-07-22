package eu.scy.agents.impl;

import eu.scy.agents.api.ICommunicationAgent;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotificationSender;

public abstract class AbstractCommunicationAgent extends AbstractThreadedAgent
		implements ICommunicationAgent {

	private INotificationSender notificationSender;

	public AbstractCommunicationAgent(String threadName, String id) {
		super(threadName,id);
		notificationSender = new NotificationSender();
	}

	@Override
	public INotificationSender getNotificationSender() {
		return notificationSender;
	}

}
