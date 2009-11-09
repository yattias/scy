package eu.scy.actionlogging.logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.commons.smack.SmacketExtension;

public class ActionLogger /* extends ScyBaseDAOHibernate */implements
		IActionLogger {

	private XMPPConnection connection;
	private ActionPacketTransformer transformer;
	private MultiUserChat muc;

	/**
	 * simple constructor for an actionlogger
	 * 
	 * @param user
	 *            user throwing actions (NOT! the tool)
	 */
	public ActionLogger() {
		transformer = new ActionPacketTransformer();
	}

	/**
	 * logs an action
	 * 
	 * @param tool
	 *            the tool throwing the action
	 * @param action
	 *            IAction thrown
	 */
	public void log(IAction action) {
		Message packet = new Message();

		packet.setFrom(connection.getUser());
		packet.setTo("scyhub.scy.collide.info");

		transformer.setObject(action);

		packet.addExtension(new SmacketExtension(transformer));
		connection.sendPacket(packet);

		try {
			Message message = muc.createMessage();
			message.addExtension(new SmacketExtension(transformer));
			muc.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void init(XMPPConnection connection) {
		this.connection = connection;

//		muc = new MultiUserChat(connection, "test@syncsessions.scy.collide.info");
//		
//		try {
//			muc.create("merkel");
//			Form form = new Form(Form.TYPE_FORM);
//			muc.sendConfigurationForm(form);
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//		
//		//add extenison provider
//		SmacketExtensionProvider.registerExtension(new ActionPacketTransformer());
//		
//	    ProviderManager providerManager = ProviderManager.getInstance();
//	    providerManager.addExtensionProvider("x", "jabber:x:action", new SmacketExtensionProvider());
//
//		
//		connection.addPacketListener(new PacketListener() {
//
//			@Override
//			public void processPacket(Packet packet) {
//				System.out.println("Received packet from MUC: " + packet);
//				Message m = (Message) packet;
//				String body = m.getBody();
//				System.out.println("Packet body: " + body);
//				
//				PacketExtension extension = packet.getExtension("x", "jabber:x:action");
//				SmacketExtension se = (SmacketExtension) extension;
//				Action action = (Action) se.getTransformer().getObject();
//				System.out.println("Action log received: " + action.getUser() + " " + action.getTime() + " " + action.getType());
//			}
//			
//		}, new PacketExtensionFilter("x", "jabber:x:action"));
	}

	@Override
	@Deprecated
	public void log(String username, String source, IAction action) {
		log(action);
	}

}
