package eu.scy.server.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Callback.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Notificator(Component scyhub) {
		super(scyhub, new NotificationPacketTransformer());
		try {
			init();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private TupleSpace commandSpace;

    private String COMMAND_SPACE = "command";

    private ArrayList<Integer> callbacks;

    private static final String NOTIFICATION = "notification";

    private void init() throws TupleSpaceException {
//    	String host = Configuration.getInstance().getSqlSpacesServerHost();
    	String host = "134.91.34.217";
    	int port = Configuration.getInstance().getSqlSpacesServerPort();
    	
    	commandSpace = new TupleSpace(new User(Notificator.class.getName()), host, port, false, false, COMMAND_SPACE);
        callbacks = new ArrayList<Integer>();
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple(NOTIFICATION, Field.createWildCardField());
        callbacks.add(commandSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false));
    }

    class SessionCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
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
        Map<String, String> props = new HashMap<String, String>();
        for (int i = 7; i < notificationTuple.getNumberOfFields(); i++) {
            String keyValue = (String) notificationTuple.getField(i).getValue();
            int index = keyValue.indexOf('=');
            String key = keyValue.substring(0, index);
            String value = keyValue.substring(index + 1, keyValue.length());
            props.put(key, value);

        }
        INotification notification = new Notification(uniqueID, userId, sender, toolId, notificationTuple.getCreationTimestamp(), mission, session, props);

        // Create a message to send the notification with receiver
        Message message = new Message();
        message.setTo(userId);
        message.setFrom("scyhub.scy.collide.info");
        // Attach the extension
        transformer.setObject(notification);
        message.addExtension(new WhacketExtension(transformer));
        // Send the message to receiver
        send(message);        
    }

	@Override
	protected void process(Packet packet, WhacketExtension extension) {
		// TODO noting to process as we will only send (at the moment)
	}
}
