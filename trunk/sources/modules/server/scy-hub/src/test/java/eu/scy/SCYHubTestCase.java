package eu.scy;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmpp.packet.Message;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.scyhub.SCYHubComponent;

/**
 * Tests the scyhub.
 * 
 * @author thomasd
 *
 */
public class SCYHubTestCase {

	private final Logger logger = Logger.getLogger(SCYHubTestCase.class.getName());
	
	private final String TEST_CONTENT = "This is the content, but there isn't much.";
	private final String TEST_EVENT = "HEY HEY HEY";
	private final String TEST_TOOL_ID = "eu.scy.test." + SCYHubTestCase.class.getName();
	private final String TEST_TOOL_SESSION_ID = "1234567890";
	private final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
	private final String TEST_TO = "obama@wiki.intermedia.uio.no";
	private final String TEST_PERSISTENCE_ID = "123";

	
	public SCYHubTestCase() {
	}

	
	private ISyncMessage getTestSyncMessage() {
		return SyncMessageHelper.createSyncMessageWithDefaultExp(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM,TEST_TO, TEST_CONTENT, TEST_EVENT, TEST_PERSISTENCE_ID);
	}
	
	private Message getMessage() {
		// test convert from ScyMessage to xmpp message
		ISyncMessage syncMessage = getTestSyncMessage();
		
		Message xmppMessage = SyncMessageHelper.convertToXmppMessage(syncMessage);
		xmppMessage.setTo("to@to");
		xmppMessage.setFrom("from@from");
		return xmppMessage;
	}

	
	@Test
	public void testGetMessage() {
	    assertNotNull(getMessage());	    
	}
	
	
	@org.junit.Test
	public void testDataSyncMessageRouter(){
		Message xmpp = getMessage();
		assertNotNull(xmpp);
		SCYHubComponent scyHubComponent = new SCYHubComponent();
		scyHubComponent.initialize(null, null);

		org.xmpp.packet.Message xmppMessage = new org.xmpp.packet.Message();
		xmppMessage.setTo(xmpp.getTo());
		xmppMessage.setFrom(xmpp.getFrom());
		scyHubComponent.processPacket(xmppMessage);
	}

}
