package eu.scy.agents.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersistentStorageTest {

	private PersistentStorage storage;

	@BeforeClass
	public static void startTSServer() {
		// if (!Server.isRunning()) {
		// Configuration.getConfiguration().setSSLEnabled(false);
		// Configuration.getConfiguration().setWebEnabled(false);
		// Configuration.getConfiguration().setWebServicesEnabled(false);
		// Configuration.getConfiguration().setRemoteAdminEnabled(false);
		// Server.startServer();
		// }
	}

	@AfterClass
	public static void shutdownServer() {
		Server.stopServer();
	}

	@Before
	public void setUp() {
		storage = new PersistentStorage();
	}

	@Test
	public void testPutGetSimpleType() {
		storage.put("key1", "value1");
		assertEquals(storage.get("key1"), "value1");
	}

	@Test
	public void testPutGetComplexType() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("test", 12345);
		storage.put("key1", map);
		Object result = storage.get("key1");
		assertTrue(result instanceof HashMap);
		HashMap<String, Integer> resultMap = (HashMap<String, Integer>) result;
		assertEquals("Wrong content", 12345, map.get("test"), 0.000001);
	}
}
