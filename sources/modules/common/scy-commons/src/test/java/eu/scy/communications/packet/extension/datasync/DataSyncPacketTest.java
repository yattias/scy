package eu.scy.communications.packet.extension.datasync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;

import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.common.configuration.Configuration;

/**
 * Tests the data sync packet extenison.
 * 
 * @author anthonjp
 * 
 */
public class DataSyncPacketTest {

	private final Logger logger = Logger.getLogger(DataSyncPacketTest.class.getName());
	private final String TEST_CONTENT = "This is the content, but there isn't much.";
	private final String TEST_EVENT = "important event";
	private final String TEST_TOOL_ID = "eu.scy.test."+ DataSyncPacketTest.class.getName();
	private final String TEST_TOOL_SESSION_ID = "1234567890";
	private final String TEST_FROM = "passerby@scy.intermedia.uio.no";
	private final String TEST_TO = "passerby@scy.intermedia.uio.no";
	private final String TEST_PERSISTENCE_ID = "123";

    private static Configuration props = Configuration.getInstance();
	
    
	public DataSyncPacketTest() {
	}

	private ISyncMessage getTestSyncMessage() {
		return SyncMessageHelper.createSyncMessageWithDefaultExp(TEST_TOOL_SESSION_ID,
				TEST_TOOL_ID, 
				TEST_FROM,
				TEST_TO,
				TEST_CONTENT, 
				TEST_EVENT,
				TEST_PERSISTENCE_ID);
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
		assertEquals(TEST_TO, dsp.getTo());
		assertEquals(TEST_PERSISTENCE_ID, dsp.getPersistenceId());
		assertEquals(props.getDatasyncMessageDefaultExpiration(), dsp.getExpiration());
		logger.debug(dsp.toXML());
	}

}
