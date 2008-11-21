package main.java.eu.scy.actionlogging.logger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import main.java.eu.scy.actionlogging.api.IAction;
import main.java.eu.scy.actionlogging.api.IActionLogger;

public class ActionLogger implements IActionLogger
{
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
    public ActionLogger(String host, String queue) {
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
     * sends a serialized action to the activemq server specified in host
     */
    public void log(IAction action)
    {
        try {
            TextMessage tmsg = ses.createTextMessage();
            tmsg.setText(action.getXML());
            prod.send(tmsg);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
