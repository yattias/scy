package eu.scy;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.DataSyncPacketExtension;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.scyhub.SCYHubComponent;


public class SCYHubTestCase {

	private final Logger logger = Logger.getLogger(SCYHubTestCase.class.getName());
	
	private final String TEST_CONTENT = "This is the content, but there isn't much.";
	private final String TEST_EVENT = "HEY HEY HEY";
	private final String TEST_TOOL_ID = "eu.scy.test." + SCYHubTestCase.class.getName();
	private final String TEST_TOOL_SESSION_ID = "1234567890";
	private final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
	private final String TEST_PERSISTENCE_ID = "123";

	
	public SCYHubTestCase() {
	}

	
	private ISyncMessage getTestSyncMessage() {
		return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID,
				TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, TEST_PERSISTENCE_ID,
				SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
	}
	

	private Message getMessage() {
		// test convert from ScyMessage to xmpp message
		ISyncMessage syncMessage = getTestSyncMessage();
		assertNotNull(syncMessage);
		
		Message message = ((SyncMessage) syncMessage).convertToXMPPMessage();
		message.setTo("to@to");
		message.setFrom("from@from");
		return message;
	}
	
	
//TODO: fix this test, we have a confliect between xmpp (serverside) and jive packets (
//	@org.junit.Test
//	public void testDataSyncMessageRouter(){
//		Message jiveMessage = getMessage();
//		assertNotNull(jiveMessage);
//		SCYHubComponent scyHubComponent = new SCYHubComponent();
//		scyHubComponent.initialize(null, null);
//
//		org.xmpp.packet.Message xmppMessage = new org.xmpp.packet.Message();
//		xmppMessage.setTo(jiveMessage.getTo());
//		xmppMessage.setFrom(jiveMessage.getFrom());
//		xmppMessage.addExtension(jiveMessage.getExtension(DataSyncPacketExtension.NAMESPACE, DataSyncPacketExtension.ELEMENT_NAME));
//		scyHubComponent.processPacket((Packet) xmppMessage);
//	}

}
