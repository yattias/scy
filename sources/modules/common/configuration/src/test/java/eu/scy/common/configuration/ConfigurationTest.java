package eu.scy.common.configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

	public ConfigurationTest() {}
	
	@org.junit.Test
    public void testConfiguration() {
		assertTrue(Configuration.getInstance() != null);
	}

    @org.junit.Test
    public void testSQLSpacesPort() {
    	Integer port = null;
    	try {
    		port = Configuration.getInstance().getSQLSpacesServerPort();
    	} catch (NumberFormatException ex) {
    		// nothing
    	}
    	assertNotNull(port);
    }
}
