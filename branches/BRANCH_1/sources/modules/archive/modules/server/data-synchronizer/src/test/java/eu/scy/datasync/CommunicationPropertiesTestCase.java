package eu.scy.datasync;

import static org.junit.Assert.*;
import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;



/**
 * Test for CommunicationProperties
 * 
 * @author thomasd
 *
 */
public class CommunicationPropertiesTestCase {

    
    @org.junit.Test
    public void testCreateSyncMessage() {
        Configuration conf = Configuration.getInstance();
        assertNotNull(conf);
        assertEquals("Computers in the future may weigh no less than 1.5 tons", conf.getTestString());        
    }
}
