package eu.scy.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Callback.Command;

import java.util.Vector;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.api.INotificationService;

public class NotificationService implements INotificationService, Callback {

	//default settings:
	private String host = "127.0.0.1";
	private int port = 2525;
	private String space = "SCYSpace-notifications";
	//
	private String user = null;
	private Vector<INotificationCallback> callbacks = new Vector<INotificationCallback>();
	private TupleSpace ts = null;
	
	/**
	 * simple constructor. 
	 * @param user	username used as filter
	 */
	public NotificationService(String user) {
		this.user = user;
		this.connect();
	}
	
	/**
	 * 
	 * @param host	SQLSpaces host
	 * @param port	SQLSpaces port
	 * @param space	SQLSpaces space
	 * @param user user name used as filter
	 */
	public NotificationService(String host, int port, String space, String user) {
		this.host = host;
		this.port = port;
		this.space = space;
		this.user = user;
		this.connect();
	}
	
	/**
	 * creates a connection
	 */
	private void connect() {
		try {
			ts = new TupleSpace(host, port, space);
			Tuple mask = new Tuple(user, String.class, String.class, String.class); 
			int seqNo = ts.eventRegister(Command.WRITE, mask, this, false);
//			System.out.println("notificationlogger verbunden");
//			while(true) {}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean deregisterCallback(INotificationCallback callback) {
		return callbacks.remove(callback);
	}

	@Override
	public void notifyCallbacks(INotification notification) {
		for(INotificationCallback item : callbacks) {
			item.onNotification(notification);
		}		
	}

	@Override
	public void registerCallback(INotificationCallback callback) {
		this.callbacks.add(callback);
	}
	
	/**
	 * SQLSpaces method -> see SQLSpaces documentation
	 */
	public void call(Command arg0, int arg1, Tuple x, Tuple arg3) {
		this.notifyCallbacks(new Notification((String)x.getField(3).getValue()));
	}

}
