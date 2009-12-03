package eu.scy.common.configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ConfigurationTest {

	private final String testString = "Computers in the future may weigh no less than 1.5 tons";

	public ConfigurationTest() {}
	
	@org.junit.Test
    public void testConfiguration() {
		assertTrue(Configuration.getInstance() != null);
	}

	@org.junit.Test
	public void testString() {
		assertEquals(testString, Configuration.getInstance().getTestString());
	}
	
    @org.junit.Test
    public void testSQLSpacesPort() {
    	Integer port = null;
    	try {
    		port = Configuration.getInstance().getSqlSpacesServerPort();
    	} catch (NumberFormatException ex) {
    		// nothing
    	}
    	assertNotNull(port);
    }
}
