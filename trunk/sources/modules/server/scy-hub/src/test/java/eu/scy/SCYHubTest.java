package eu.scy;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.xmpp.packet.Message;

import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.extension.DataSyncPacketExtension;
import eu.scy.datasync.impl.SyncMessage;
import eu.scy.datasync.impl.SyncMessageTestCase;
import eu.scy.scyhub.SCYHubComponent;

public class SCYHubTest {

	private final Logger logger = Logger.getLogger(SyncMessageTestCase.class
			.getName());
	private final String TEST_CONTENT = "This is the content, but there isn't much.";
	private final String TEST_EVENT = "HEY HEY HEY";
	private final String TEST_TOOL_ID = "eu.scy.test."
			+ SyncMessageTestCase.class.getName();
	private final String TEST_TOOL_SESSION_ID = "1234567890";
	private final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
	private final String TEST_PERSISTENCE_ID = "123";

	public SCYHubTest() {
	}

	private ISyncMessage getTestSyncMessage() {
		return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID,
				TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, TEST_PERSISTENCE_ID,
				SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
	}

	public Message getMessage() {
		// test convert from ScyMessage to xmpp message
		ISyncMessage syncMessage = getTestSyncMessage();
		assertNotNull(syncMessage);
		
		Message message = ((SyncMessage)syncMessage).convertToXMPPMessage();
		message.setTo("to@to");
		message.setFrom("from@from");
		return message;
	}
	
	@org.junit.Test
	public void testDataSyncMessageRouter(){
		Message mess = this.getMessage();
		assertNotNull(mess);
		SCYHubComponent scyHubComponent = new SCYHubComponent();
		scyHubComponent.initialize(null, null);
		System.out.println( "test message: " + mess.toXML());
		scyHubComponent.processPacket(mess);
	}
	
	
	@Before
	public void setUp() throws Exception {
	}

	
	
	
	
	@After
	public void tearDown() throws Exception {
	}

}
