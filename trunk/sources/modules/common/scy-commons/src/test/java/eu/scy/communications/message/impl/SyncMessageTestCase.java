package eu.scy.communications.message.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;



public class SyncMessageTestCase {
    
    private static final Logger logger = Logger.getLogger(SyncMessageTestCase.class.getName());
    
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + SyncMessageTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
    private static final String TEST_PERSISTENCE_ID = "1239999999";
    
    
    public SyncMessageTestCase() {
    }

    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
    }
    
    
    @org.junit.Test
    public void testCreateSyncMessage() {
        //test convert xmpp message to SyncMessage
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        Message xmppMessage = ((SyncMessage) syncMessage).convertToXMPPMessage();
        
        assertNotNull(xmppMessage);
        System.out.println(xmppMessage.toXML());
        syncMessage = SyncMessage.createSyncMessage(xmppMessage);
        assertNotNull(syncMessage);
        assertEquals(TEST_CONTENT, syncMessage.getContent());
        assertEquals(TEST_EVENT, syncMessage.getEvent());
        assertEquals(TEST_TOOL_ID, syncMessage.getToolId());
        assertEquals(TEST_TOOL_SESSION_ID, syncMessage.getToolSessionId());
        assertEquals(TEST_FROM, syncMessage.getFrom());            
    }
    
}
