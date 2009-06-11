package eu.scy.datasync.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.xmpp.packet.Message;

import eu.scy.datasync.api.ISyncMessage;



public class SyncMessageTestCase {
    
    private static final Logger logger = Logger.getLogger(SyncMessageTestCase.class.getName());
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.testtool";
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
    
    
    public SyncMessageTestCase() {
    }


    public static Test suite() { 
        return new JUnit4TestAdapter(SyncMessageTestCase.class); 
    }
    
    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
    }
    
    
    @org.junit.Test
    public void testConvertToXMPPMessage() {
        //test convert from ScyMessage to xmpp message
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        Message xmppMessage = ((SyncMessage) syncMessage).convertToXMPPMessage();
        assertNotNull(xmppMessage);
        logger.debug("" + xmppMessage);
        assertEquals(TEST_CONTENT, xmppMessage.getChildElement("content", SyncMessage.DATA_SYNC_XMPP_NAMESPACE).getText());
        assertEquals(TEST_EVENT, xmppMessage.getChildElement("event", SyncMessage.DATA_SYNC_XMPP_NAMESPACE).getText());
        assertEquals(TEST_TOOL_ID, xmppMessage.getChildElement("toolId", SyncMessage.DATA_SYNC_XMPP_NAMESPACE).getText());
        assertEquals(TEST_TOOL_SESSION_ID, xmppMessage.getChildElement("toolSessionId", SyncMessage.DATA_SYNC_XMPP_NAMESPACE).getText());
        assertEquals(TEST_FROM, xmppMessage.getFrom().toBareJID());
    } 
    
    
    @org.junit.Test
    public void testCreateSyncMessage() {        
        //test convert xmpp message to SyncMessage
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        Message xmppMessage = ((SyncMessage) syncMessage).convertToXMPPMessage();
        assertNotNull(xmppMessage);
        syncMessage = SyncMessage.createSyncMessage(xmppMessage);
        assertNotNull(syncMessage);
        assertEquals(TEST_CONTENT, syncMessage.getContent());
        assertEquals(TEST_EVENT, syncMessage.getEvent());
        assertEquals(TEST_TOOL_ID, syncMessage.getToolId());
        assertEquals(TEST_TOOL_SESSION_ID, syncMessage.getToolSessionId());
        assertEquals(TEST_FROM, syncMessage.getFrom());            
    }
    
}
