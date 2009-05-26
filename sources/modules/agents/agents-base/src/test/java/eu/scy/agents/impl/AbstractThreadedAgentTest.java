package eu.scy.agents.impl;

import static org.junit.Assert.assertEquals;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractThreadedAgentTest {

	private ThreadedAgentMock agent;

	@Before
	public void setUp() {
		if (!Server.isRunning()) {
			Server.startServer();
		}
		agent = new ThreadedAgentMock();

	}

	@After
	public void tearDown() {
		Server.stopServer();
	}

	@Test
	public void testRun() {
		agent.run();
		assertEquals(0, agent.getRunCount());

		agent.start();
		agent.run();
		assertEquals(1, agent.getRunCount());

		agent.suspend();
		agent.run();
		assertEquals(1, agent.getRunCount());

		agent.resume();
		agent.run();
		assertEquals(2, agent.getRunCount());

		agent.stop();
		agent.run();
		assertEquals(2, agent.getRunCount());
	}

	@Test
	public void testAlive() {

	}
}
