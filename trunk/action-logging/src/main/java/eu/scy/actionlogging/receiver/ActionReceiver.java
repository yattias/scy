package eu.scy.actionlogging.receiver;

import java.util.Vector;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionCallback;
import eu.scy.actionlogging.api.IActionReceiver;
import eu.scy.actionlogging.logger.Action;

public class ActionReceiver implements IActionReceiver, MessageListener
{
    private Vector<IActionCallback> callbacks = new Vector<IActionCallback>();
    private String host = null;
    private String queue = null;
    //JavaMessageService objects:
    private ActiveMQConnectionFactory conFac = null;
    private Connection con = null;
    private Session ses = null;
    private Destination dest = null;
    private MessageConsumer cons = null;
    
    public ActionReceiver(String host, String queue) {
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
     * notifies all registered IActionCallbacks
     */
    public void notifyCallbacks(IAction action) {
        for(IActionCallback callback : callbacks) {
            callback.onAction(action);
        }
    }
    
    /**
     * registers a new IActionCallback
     */
    public void registerCallback(IActionCallback callback) {
        callbacks.add(callback);
    }
    
    /**
     * deregisters an IActionCallback
     */
    public boolean deregisterCallback(IActionCallback callback) {
        return callbacks.remove(callback);
    }
    
    /**
     * JMS MessageListener implementation
     */
    public void onMessage(Message msg) {
        TextMessage tmsg = (TextMessage) msg;
        try {
            notifyCallbacks(new Action(tmsg.getText()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
