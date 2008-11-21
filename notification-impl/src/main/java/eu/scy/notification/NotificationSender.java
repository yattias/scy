package eu.scy.notification;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationSender;


public class NotificationSender implements INotificationSender {

	private String host = null;	//jms host
	private String queue = null; //queue to listen on
	//jms objects:
	private ActiveMQConnectionFactory conFac = null;
	private Connection con = null;
	private Session ses = null;
	private Destination dest = null;
	private MessageProducer prod = null;
	
	/**
	 * constructor.
	 * @param host	jms host. "tcp://example:61616"
	 * @param queue
	 */
	public NotificationSender(String host, String queue) {
		this.host = host;
		this.queue = queue;
		createConnection();
	}
	
	/**
	 * creates a connection to the jms server
	 */
	public void createConnection() {
		try {
			conFac = new ActiveMQConnectionFactory(host);
			con = conFac.createConnection();
			ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			dest = ses.createQueue(queue);
			prod = ses.createProducer(dest);
			prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}
		catch(Exception e) {
			System.out.println("Cannot create connection:");
			e.printStackTrace();
		}
	}
	
	/**
	 * closes the connection
	 */
	public void closeConnection() {
		try {
			prod.close();
			ses.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println("Cannot close connection:");
			e.printStackTrace();
		}
	}
	
	/**
	 * sends a new notification
	 * @param notification the notification to send
	 */
	public void send(INotification notification) {
		try {
			TextMessage tmsg = ses.createTextMessage();
			tmsg.setText(notification.getXML());
			prod.send(tmsg);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
