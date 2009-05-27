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
	private static AgentManager agentManager;

	@BeforeClass
	public static void setUp() {
		if (!Server.isRunning()) {
			Server.startServer();
		}

		agentManager = new AgentManager();
		agentManager.setAgentFactory(new IAgentFactory() {
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
		agentManager.dispose();
		Server.stopServer();
	}

	@Test
	public void testStartStopAgent() throws InterruptedException {
		agentManager.startAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("Agent not started", mockAgent.isRunning());
		agentManager.stopAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("agent not stopped", !mockAgent.isRunning());
	}

	@Test
	public void testSuspendResumeAgent() throws InterruptedException {
		agentManager.suspendAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("Agent not suspended", mockAgent.isSuspended());
		agentManager.resumeAgent(ThreadedAgentMock.NAME);

		Thread.sleep(5000);

		assertTrue("agent not resumed", !mockAgent.isSuspended());
	}
}
