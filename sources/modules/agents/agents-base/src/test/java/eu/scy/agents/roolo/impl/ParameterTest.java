/**
 * 
 */
package eu.scy.agents.roolo.impl;

import static org.junit.Assert.assertEquals;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.api.IParameter;
import eu.scy.agents.impl.Parameter;

/**
 * @author fschulz
 */
public class ParameterTest {

	private IParameter parameters;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parameters = new Parameter();
	}

	/**
	 * Test method for
	 * {@link eu.scy.agents.roolo.impl.PersistentStorage#get(java.lang.String)}.
	 */
	@Test
	public void testGetPut() {
		parameters.set("Test1", 5);
		Integer five = parameters.get("Test1");
		assertEquals(5, five.intValue());

		parameters.set("Test2", "Testasd adsa dsa");
		String string = parameters.get("Test2");
		assertEquals("Testasd adsa dsa", string);
	}

}
