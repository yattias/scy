package eu.scy.notification.api;

public interface INotificationSender {
	/**
	 * sends a notification
	 * @param user user to send to
	 * @param tool	tool sending from
	 * @param notification
	 */
	public void send(String user, String tool, INotification notification);

}
