package eu.scy.communications.message.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.xmpp.packet.Message;

import eu.scy.communications.message.ISyncMessage;


/**
 * Test sync message
 * 
 * @author anthonjp
 *
 */
public class SyncMessageTestCase {
    
    private static final Logger logger = Logger.getLogger(SyncMessageTestCase.class.getName());
    
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + SyncMessageTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby";
    private static final String TEST_PERSISTENCE_ID = "1239999999";
    
    
    public SyncMessageTestCase() {
    }

    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessageHelper.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, TEST_PERSISTENCE_ID, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
    }
    
    
    @org.junit.Test
    public void testSyncMessageTranslation() {
        //test convert xmpp message to SyncMessage
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
   
        Message xmppMessage = SyncMessageHelper.convertToXmppMessage(syncMessage);
        assertNotNull(xmppMessage);
        
        System.out.println(xmppMessage.toXML());
        
        ISyncMessage syncMessageTranslate = SyncMessageHelper.createSyncMessage(xmppMessage);
        assertNotNull(syncMessageTranslate);
        assertEquals(TEST_CONTENT, syncMessageTranslate.getContent());
        assertEquals(TEST_EVENT, syncMessageTranslate.getEvent());
        assertEquals(TEST_TOOL_ID, syncMessageTranslate.getToolId());
        assertEquals(TEST_TOOL_SESSION_ID, syncMessageTranslate.getToolSessionId());
        assertEquals(TEST_FROM, syncMessageTranslate.getFrom());            
    }
    
}
