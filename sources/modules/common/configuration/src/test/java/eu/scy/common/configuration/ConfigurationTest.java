package eu.scy.common.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {
	
	private static final String newSCYHubName = "buhycs";

	public ConfigurationTest() {}
	
	@Before
	public void init() {
		System.getProperties().setProperty("scyconfig.scyhub.name", newSCYHubName);
	}
	
	@org.junit.Test
    public void testConfiguration() {
		assertNotNull(Configuration.getInstance());
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
    
    @Test
	public void testSystemProperties() throws Exception {
		assertEquals(newSCYHubName, Configuration.getInstance().getSCYHubName());
	}
}
