package eu.scy.agents.general;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class UserLocationInfoTest implements Runnable {

	private UserLocationInfo userLocationInfo;
	private int numThreads = 100;

	@Before
	public void setUp() {
		userLocationInfo = new UserLocationInfo();
	}

	@Ignore
	@Test
	public void testMultipleThreads() {
		for (int i = 0; i < numThreads; i++) {
			Thread t = new Thread(this, "TestThread" + i);
			try {
				t.start();
			} catch (Exception e) {
				Assert.fail(e.getMessage());
			}
		}
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		userLocationInfo.addELO("elo1");
		userLocationInfo.addELO("elo2");
		userLocationInfo.setLas(threadName + "las");
		userLocationInfo.addTool("tool1");
		userLocationInfo.addTool("tool2");

		Set<String> openElos = userLocationInfo.getOpenElos();
		assertTrue(openElos.contains("elo1"));
		assertTrue(openElos.contains("elo2"));
		Set<String> openTools = userLocationInfo.getOpenTools();
		assertTrue(openTools.contains("tool1"));
		assertTrue(openTools.contains("tool2"));
		assertEquals(threadName + "las", userLocationInfo.getLas());

		userLocationInfo.removeTool("tool1");
		userLocationInfo.setLas(threadName + "las2");
		userLocationInfo.removeELO("elo2");

		Set<String> openElos2 = userLocationInfo.getOpenElos();
		assertTrue(openElos2.contains("elo1"));
		Set<String> openTools2 = userLocationInfo.getOpenTools();
		assertTrue(openTools2.contains("tool2"));
		assertEquals(threadName + "las2", userLocationInfo.getLas());
	}
}
