package main.java.eu.scy.notification.api;

public interface INotificationCallback
{
	/**
	 * method called by NotificationService if a Notification arrives
	 * @param notification	Notification received by NotificationService
	 */
	public void onNotification(INotification notification);
}
