package eu.scy.communications.message.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.hibernate.validator.AssertTrue;
import org.jivesoftware.smack.packet.Message;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;



public class DataSyncPacketExtensionTestCase {
    
    private static final Logger logger = Logger.getLogger(DataSyncPacketExtensionTestCase.class.getName());
    
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_TOOL_ID = "eu.scy.test." + DataSyncPacketExtensionTestCase.class.getName();
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_PERSISTENCE_ID = "1239999999";
    
    
    public DataSyncPacketExtensionTestCase() {
    }

    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
    }
    
    
    @org.junit.Test
    public void testDataSyncPacketExtensionToXml() {
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        DataSyncPacketExtension dspe = new DataSyncPacketExtension(syncMessage);
        assertNotNull(dspe);
        String packetExtensionXml = dspe.toXML();
        assertEquals(true, packetExtensionXml.contains(TEST_EVENT));
    }
    
    
    @org.junit.Test
    public void testConvertFromXmppPacketExtension() {        
        org.xmpp.packet.PacketExtension xmppPacketExtension = new org.xmpp.packet.PacketExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
        assertNotNull(xmppPacketExtension);

        Element peRoot = xmppPacketExtension.getElement();
        peRoot.addElement(DataSyncPacketExtension.TOOL_SESSION_ID).addText(TEST_TOOL_SESSION_ID);
        peRoot.addElement(DataSyncPacketExtension.TOOL_ID).addText(TEST_TOOL_ID);
        peRoot.addElement(DataSyncPacketExtension.FROM).addText(TEST_FROM);
        peRoot.addElement(DataSyncPacketExtension.CONTENT).addText(TEST_CONTENT);
        peRoot.addElement(DataSyncPacketExtension.EVENT).addText(TEST_EVENT);
        peRoot.addElement(DataSyncPacketExtension.PERSISTENCE_ID).addText("");
        peRoot.addElement(DataSyncPacketExtension.EXPIRATION).addText(String.valueOf(SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME));
        
        assertNotNull(peRoot);
        assertEquals(true, peRoot.asXML().contains(TEST_CONTENT));
        DataSyncPacketExtension dspe = DataSyncPacketExtension.convertFromXmppPacketExtension(xmppPacketExtension);
        assertNotNull(dspe);
        assertEquals(true, dspe.toXML().contains(TEST_CONTENT));
    }
    
    
    @org.junit.Test
    public void testConvertToXmppPacketExtension() { 
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        DataSyncPacketExtension dspe = new DataSyncPacketExtension(syncMessage);
        assertNotNull(dspe);
        org.xmpp.packet.PacketExtension pe = dspe.convertToXmppPacketExtension();
        assertNotNull(pe);
        logger.debug("pe: " + pe);
        assertEquals(true, pe.getElement().asXML().concat("TEST_TOOL_SESSION_ID"));
    }

    
    @org.junit.Test
    public void testDataSyncPacketExtensionToPojo() {        
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
        DataSyncPacketExtension dspe = new DataSyncPacketExtension(syncMessage);
        assertNotNull(dspe);
        syncMessage = null;
        syncMessage = dspe.toPojo();
        assertNotNull(syncMessage);        
        assertEquals(TEST_TOOL_SESSION_ID, syncMessage.getToolSessionId());
        assertEquals(TEST_TOOL_ID, syncMessage.getToolId());
        assertEquals(TEST_FROM, syncMessage.getFrom());            
        assertEquals(TEST_CONTENT, syncMessage.getContent());
        assertEquals(TEST_EVENT, syncMessage.getEvent());
        assertEquals(SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME, syncMessage.getExpiration());
    }

}
