package eu.scy.agents.impl.manager;

import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.ThreadedAgentMock;

public class AgentManagerTest {

	private static AbstractThreadedAgent mockAgent;
	private static AgentManager agentManager;

	@BeforeClass
	public static void setUp() {
		if (!Server.isRunning()) {
			Configuration.getConfiguration().setSSLEnabled(false);
			Server.startServer();
		}

		agentManager = new AgentManager("localhost", 2525);
	}

	@AfterClass
	public static void tearDown() {
		agentManager.dispose();
		Server.stopServer();
	}

	@Test
	public void testStartStopAgent() throws InterruptedException, AgentLifecycleException {
		String id=agentManager.startAgent(ThreadedAgentMock.NAME, null);
		// agentManager.startAgent(ThreadedAgentMock.NAME);
		Thread.sleep(5000);

		assertTrue("Agent not started", mockAgent.isRunning());
		agentManager.stopAgent(ThreadedAgentMock.NAME, id);

		Thread.sleep(1000);

		assertTrue("agent not stopped", !mockAgent.isRunning());
	}
	// @Test
	// public void testSuspendResumeAgent() throws InterruptedException {
	// agentManager.suspendAgent(ThreadedAgentMock.NAME);
	//
	// Thread.sleep(1000);
	//
	// assertTrue("Agent not suspended", mockAgent.isSuspended());
	// agentManager.resumeAgent(ThreadedAgentMock.NAME);
	//
	// Thread.sleep(1000);
	//
	// assertTrue("agent not resumed", !mockAgent.isSuspended());
	// }
}
