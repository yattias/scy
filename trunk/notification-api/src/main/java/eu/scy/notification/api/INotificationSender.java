package main.java.eu.scy.notification.api;

public interface INotificationSender {
	/**
	 * sends a notification
	 * @param notification
	 */
	public void send(INotification notification);

}
