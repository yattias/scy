package eu.scy.datasync.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.DataSyncPacketExtension;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.datasync.impl.SyncMessageTestCase;

/**
 * Tests the data sync packet extenison.
 * 
 * @author anthonjp
 *
 */
public class DataSyncPacketTest {

	private final Logger logger = Logger.getLogger(SyncMessageTestCase.class
			.getName());
	private final String TEST_CONTENT = "This is the content, but there isn't much.";
	private final String TEST_EVENT = "important event";
	private final String TEST_TOOL_ID = "eu.scy.test."
			+ SyncMessageTestCase.class.getName();
	private final String TEST_TOOL_SESSION_ID = "1234567890";
	private final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
	private final String TEST_PERSISTENCE_ID = "123";

	public DataSyncPacketTest() {
	}

	private ISyncMessage getTestSyncMessage() {
		return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID,
				TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, TEST_PERSISTENCE_ID,
				SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
	}

	@org.junit.Test
	public void convertPojoToPacketExtension() {
		// test convert from ScyMessage to xmpp message
		ISyncMessage syncMessage = getTestSyncMessage();
		assertNotNull(syncMessage);
		
		DataSyncPacketExtension dsp = new DataSyncPacketExtension(syncMessage);
		assertNotNull(dsp);
		assertEquals(TEST_CONTENT, dsp.getContent());
		assertEquals(TEST_EVENT, dsp.getEvent());
		assertEquals(TEST_TOOL_ID, dsp.getToolId());
		assertEquals(TEST_TOOL_SESSION_ID, dsp.getToolSessionId());
		assertEquals(TEST_FROM, dsp.getFrom());
		assertEquals(TEST_PERSISTENCE_ID, dsp.getPersistenceId());
		assertEquals(SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME, dsp
				.getExpiration());
	}

}
