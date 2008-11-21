package eu.scy.notification.api;

public interface INotificationService
{
	/**
	 * NotificationService should create the connection to the 
	 * server running the logging queue and notify registered 
	 * ICallbacks if a new message arrives.
	 * The idea is pretty equal to the Observer pattern.
	 * 
	 * The class implementing this interface takes care of the whole 
	 * connectivity and works as a listener.
	 */
	
	/**
	 * registers a class that implements the interface 'ICallback'
	 * this should work equivalent to the Observer pattern
	 * @param callback	class that implements ICallback
	 */
	public void registerCallback(INotificationCallback callback);
	
	/**
	 * deregisters an object
	 * @param callback object to deregister
	 * @return	feedback if object got removed
	 */
	public boolean deregisterCallback(INotificationCallback callback);
	
	/**
	 * notifies registered ICallback objects
	 * @param notification	INotification 
	 */
	public void notifyCallbacks(INotification notification);
}
