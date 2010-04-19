package eu.scy.notification.api;

public interface INotifiable
{
	/**
	 * method called by NotificationService if a Notification arrives
	 * @param notification	Notification received by NotificationService
	 */
	public void processNotification(INotification notification);
}
