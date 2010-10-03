package eu.scy.common.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {
	
	private static final String newSCYHubName = "buhycs";
    private static final String freakingSQLHost = "looocalHosty";

	public ConfigurationTest() {}
	
	@Before
	public void init() {
		System.getProperties().setProperty("scyconfig.scyhub.name", newSCYHubName);
		System.getProperties().setProperty("sqlspaces.db.mysql.host", freakingSQLHost);
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

    @org.junit.Test
    public void testGetMysqlHost() {
        assertEquals(freakingSQLHost, Configuration.getInstance().getSQLSpacesDBHost());
    }
    
    @Test
	public void testSystemProperties() throws Exception {
		assertEquals(newSCYHubName, Configuration.getInstance().getSCYHubName());
	}
}
