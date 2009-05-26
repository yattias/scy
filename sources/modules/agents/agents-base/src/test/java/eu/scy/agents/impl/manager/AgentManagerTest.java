package eu.scy.agents.impl.manager;

import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.server.Server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.api.IParameter;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.ThreadedAgentMock;

public class AgentManagerTest {

	private static ThreadedAgentMock mockAgent;

	@BeforeClass
	public static void setUp() {
		if (!Server.isRunning()) {
			Server.startServer();
		}

		AgentManager.getInstance().setAgentFactory(new IAgentFactory() {
			@Override
			public IThreadedAgent create(IParameter params) {
				mockAgent = new ThreadedAgentMock();
				return mockAgent;
			}

			@Override
			public String getAgentName() {
				return ThreadedAgentMock.NAME;
			}
		});
	}

	@AfterClass
	public static void tearDown() {
		Server.stopServer();
		AgentManager.getInstance().dispose();
	}

	@Test
	public void testStartStopAgent() throws InterruptedException {
		AgentManager.getInstance().startAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("Agent not started", mockAgent.isRunning());
		AgentManager.getInstance().stopAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("agent not stopped", !mockAgent.isRunning());
	}

	@Test
	public void testSuspendResumeAgent() throws InterruptedException {
		AgentManager.getInstance().suspendAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("Agent not suspended", mockAgent.isSuspended());
		AgentManager.getInstance().resumeAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("agent not resumed", !mockAgent.isSuspended());
	}
}
