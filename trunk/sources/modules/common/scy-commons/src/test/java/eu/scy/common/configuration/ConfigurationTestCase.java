package eu.scy.common.configuration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class ConfigurationTestCase {

	private final String testString = "Computers in the future may weigh no less than 1.5 tons";
	
	public ConfigurationTestCase() {}
	
	@org.junit.Test
    public void testConfiguration() {
		assertTrue(Configuration.getInstance().getTestString() != null);
		assertEquals(testString, Configuration.getInstance().getTestString());
	}

    @org.junit.Test
    public void testMessageDefaultExpiration() {
        assertEquals(Configuration.getInstance().getDatasyncMessageDefaultExpiration(), 3600000);

    }
}
