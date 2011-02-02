package eu.scy.notification.api;

public interface INotifiable
{
	/**
	 * method called by NotificationService if a Notification arrives
	 * @param notification	Notification received by NotificationService
	 */
	public boolean processNotification(INotification notification);
}
