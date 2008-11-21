package eu.scy.notification;

import java.util.Vector;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.api.INotificationService;

public class NotificationService implements INotificationService, MessageListener
{
	private Vector<INotificationCallback> callbacks = new Vector<INotificationCallback>();
	private String host = null;
	private String queue = null;
	//JavaMessageService objects:
	private ActiveMQConnectionFactory conFac = null;
	private Connection con = null;
	private Session ses = null;
	private Destination dest = null;
	private MessageConsumer cons = null;
	
	public NotificationService(String host, String queue) {
		this.host = host;
		this.queue = queue;
		createConnection();
	}
	
	/**
	 * create Connection
	 */
	public void createConnection() {
		try {
			conFac = new ActiveMQConnectionFactory(host);
			con = conFac.createConnection();
			con.start();
			ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			dest = ses.createQueue(queue);
			cons = ses.createConsumer(dest);
			cons.setMessageListener(this);
		}
		catch(Exception e) {
			System.out.println("Cannot create connection:");
			e.printStackTrace();
		}
	}
	
	/**
	 * close connection
	 */
	public void closeConneciton() {
		try {
			cons.close();
			ses.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println("Cannot close connection:");
			e.printStackTrace();
		}
	}
	
	/**
	 * this method is called when a message arrives on the specified queue.
	 */
	public void onMessage(Message msg) {
		TextMessage tmsg = (TextMessage) msg;
		try {
//			System.out.println("ns: notifications sent");
			notifyCallbacks((INotification)new Notification(tmsg.getText()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * registers a INotificationCallback object
	 * cp observer pattern
	 * @param callback
	 */
	public void registerCallback(INotificationCallback callback)
	{
		this.callbacks.add(callback);
	}

	/**
	 * deregister INotificationCallbcak object
	 * @param callback
	 * @return 
	 */
	public boolean deregisterCallback(INotificationCallback callback) {
		return callbacks.remove(callback);
	}
	
	/**
	 * notifies all INotificationCallbacks with a new notification
	 * @param notificaiton	new notification
	 */
	public void notifyCallbacks(INotification notification) {
		for(INotificationCallback item : callbacks) {
			item.onNotification(notification);
		}
	}
	


}
