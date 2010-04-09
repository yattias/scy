package eu.scy.server.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Callback.Command;

import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.common.configuration.Configuration;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationPacketTransformer;
import eu.scy.notification.api.INotification;
import eu.scy.scyhub.SCYHubModule;

public class Notificator extends SCYHubModule{

	private static final Logger logger = Logger.getLogger(Notificator.class);
	

	private TupleSpace commandSpace;

    private String COMMAND_SPACE = "command";

    private static final String NOTIFICATION = "notification";

    public Notificator(Component scyhub) {
    	super(scyhub, new NotificationPacketTransformer());
    	try {
    		init();
    	} catch (TupleSpaceException e) {
    		e.printStackTrace();
    	}
    }

    private void init() throws TupleSpaceException {
    	String host = Configuration.getInstance().getSQLSpacesServerHost();
    	int port = Configuration.getInstance().getSQLSpacesServerPort();
    	
    	commandSpace = new TupleSpace(new User(Notificator.class.getSimpleName()), host, port, false, false, COMMAND_SPACE);
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple(NOTIFICATION, Field.createWildCardField());
        commandSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false);
        
        logger.debug("Notificator initialised on " + host + ":" + port);
    }

    class SessionCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        	logger.debug("Notificator received notification from tuplespace!");
            processNotification(afterTuple);
        }

    }

    private synchronized void processNotification(Tuple notificationTuple) {
        // "notification":String, <User>:String, <Receiver>:String, <Sender>:String, <Mission>:String, <Session>:String, <Key=Value>:String*)
        String uniqueID =(String) notificationTuple.getField(1).getValue();
        String userId = (String) notificationTuple.getField(2).getValue();
        String toolId = (String) notificationTuple.getField(3).getValue();
        String sender = (String) notificationTuple.getField(4).getValue();
        String mission = (String) notificationTuple.getField(5).getValue();
        String session = (String) notificationTuple.getField(6).getValue();
        INotification notification = new Notification();
        notification.setUniqueID(uniqueID);
        notification.setUserId(userId);
        notification.setSender(sender);
        notification.setToolId(toolId);
        notification.setTimestamp(notificationTuple.getCreationTimestamp());
        notification.setMission(mission);
        notification.setSession(session);
        for (int i = 7; i < notificationTuple.getNumberOfFields(); i++) {
            String keyValue = (String) notificationTuple.getField(i).getValue();
            int index = keyValue.indexOf('=');
            String key = keyValue.substring(0, index);
            String value = keyValue.substring(index + 1, keyValue.length());
            notification.addProperty(key, value);
        }

        // Create a message to send the notification with receiver
        Message message = new Message();
        message.setTo(userId);
        message.setFrom(Configuration.getInstance().get("scyhub.name")+"."+Configuration.getInstance().get("openfire.host"));
        // Attach the extension
        transformer.setObject(notification);
        message.addExtension(new WhacketExtension(transformer));
        // Send the message to receiver
        send(message);   
        logger.debug("Sent notification to " + userId);
    }

	@Override
	protected void process(Packet packet, WhacketExtension extension) {
		// TODO noting to process as we will only send (at the moment)
	}

	@Override
	public void shutdown() {
		try {
			commandSpace.disconnect();
			logger.debug("Shutdown Notificator");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
