package eu.scy.agents.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class AbstractThreadedAgentTest implements Callback {

	private ThreadedAgentMock agent;
	private boolean firstAliveWritten = false;
	private boolean updated = false;
	private boolean deleted = false;

	@Before
	public void setUp() {
		if (!Server.isRunning()) {
			Server.startServer();
		}
		agent = new ThreadedAgentMock();

	}

	@After
	public void tearDown() {
		agent.kill();
		agent = null;
	}

	@AfterClass
	public static void stopServer() {
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

	/*
	 * One of the great non deterministic tests. But it works 99% of the time.
	 */
	@Test
	public void testAliveUpdates() throws TupleSpaceException,
			InterruptedException {
		TupleSpace ts = new TupleSpace();
		ts.eventRegister(Command.WRITE, AgentProtocol.ALIVE_TUPLE_TEMPLATE,
				this, false);
		ts.eventRegister(Command.UPDATE, AgentProtocol.ALIVE_TUPLE_TEMPLATE,
				this, false);
		ts.eventRegister(Command.DELETE, AgentProtocol.ALIVE_TUPLE_TEMPLATE,
				this, false);

		agent.start();
		Thread.sleep(AgentProtocol.ALIVE_INTERVAL);
		// assertTrue("First alive tuple not written", firstAliveWritten);
		Thread.sleep(AgentProtocol.ALIVE_INTERVAL * 3);
		assertTrue("Not Updated Alive Tuple", updated);
		agent.kill();
		Thread.sleep(AgentProtocol.ALIVE_INTERVAL * 3);
		assertTrue("Agent not killed", deleted);
	}

	@Override
	public void call(Command command, int seq, Tuple after, Tuple before) {
		if (Command.WRITE == command) {
			if (after.getField(2).getValue().equals(ThreadedAgentMock.NAME)) {
				firstAliveWritten = true;
			}
		}
		if (Command.UPDATE == command) {
			if (after.getField(2).getValue().equals(ThreadedAgentMock.NAME)) {
				updated = true;
			}
		}
		if (Command.DELETE == command) {
			if (before.getField(2).getValue().equals(ThreadedAgentMock.NAME)) {
				deleted = true;
			}
		}

	}
}
