package eu.scy.datasync;

import static org.junit.Assert.*;



/**
 * Test for CommunicationProperties
 * 
 * @author thomasd
 *
 */
public class CommunicationPropertiesTestCase {

    
    @org.junit.Test
    public void testCreateSyncMessage() {
        CommunicationProperties cp = new CommunicationProperties();
        assertNotNull(cp.props);
        assertEquals("Computers in the future may weigh no less than 1.5 tons", cp.props.getProperty("test.property"));        
    }
}
