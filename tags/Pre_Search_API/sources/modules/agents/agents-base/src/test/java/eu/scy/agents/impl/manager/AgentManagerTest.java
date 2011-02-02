package eu.scy.agents.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import java.rmi.dgc.VMID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ThreadedAgentMock;

/**
 * This class provides some tests to check if the {@link AgentManager} is
 * working correctly. It uses the conventions of the JUnit Framework.
 * 
 * @author Florian Schulz
 * @author Jan Engler
 */
public class AgentManagerTest {

	private static AgentManager agentManager;

	private static final int TS_PORT = 2525;

	private static final String TS_HOST = "localhost";
	// private static final String TS_HOST = "scy.collide.info";

	// If there is a working server on TS_HOST or not
	private static final boolean STANDALONE = true;

	/**
	 * This method is called before the tests start. It starts a TupleSpace-
	 * {@link Server} if the tests running in standalone-mode.
	 */
	@BeforeClass
	public static void startTSServer() {
		if (!Server.isRunning()) {
			Configuration conf = Configuration.getConfiguration();
			conf.setNonSSLPort(TS_PORT);
			conf.setSSLEnabled(false);
			conf.setDbType(Database.HSQL);
			conf.setWebEnabled(false);
			// conf.setWebServicesEnabled(false);
			conf.setRemoteAdminEnabled(false);
			conf.setLocal(false);
			conf.setShellEnabled(false);
			Server.startServer();
		}
	}

	@Before
	public void setUp() {
		agentManager = new AgentManager(TS_HOST, TS_PORT);
	}

	/**
	 * This method is called after the tests are finished. It stops the
	 * TupleSpace-{@link Server} if running in standalone-mode.
	 */
	@AfterClass
	public static void tearDown() {
		if (agentManager != null) {
			agentManager.cleanUp();
		}
		if (STANDALONE) {
			Server.stopServer();
		}
	}

	@After
	public void tearDownAfterTest() {
		if (agentManager != null) {
			agentManager.cleanUp();
		}

	}

	/**
	 * This test tries to start an Agent ({@link ThreadedAgentMock}) using the
	 * {@link AgentManager}.<br />
	 * After waiting five seconds it tries to stop this agent using the
	 * {@link AgentManager} again.
	 * 
	 * @throws InterruptedException
	 *             Is thrown if something went wrong during {@code
	 *             Thread.sleep()}.
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during {@code
	 *             AgentManager.startAgent()} or {@code
	 *             AgentManager.stopAgent()}.
	 */
	@SuppressWarnings("deprecation")
	@Ignore
	@Test(timeout = 5000)
	public void testStartStopAgent() throws InterruptedException,
			AgentLifecycleException {

		IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		Thread.sleep(1000);
		assertTrue("Agent not started", agent.isRunning());
		agentManager.stopAgent(agent);
		Thread.sleep(1000);
		// assertTrue("agent not stopped", !agent.isRunning());
		assertTrue("SuperAgent not stopped", !agent.isRunning());
		assertEquals(0, agentManager.getAgentsIdMap().size());
	}

	/**
	 * This test tries to identify an agent using the {@link AgentManager} to
	 * start the agent. <br />
	 * After started the agent an {@link Tuple} with an identify request is sent
	 * to the agent.
	 * 
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during {@code
	 *             AgentManager.startAgent()} or {@code
	 *             AgentManager.stopAgent()}.
	 * @throws InterruptedException
	 *             Is thrown if something went wrong during {@code
	 *             Thread.sleep()}.
	 * @throws TupleSpaceException
	 *             Is thrown if something went wrong inside the
	 *             {@link TupleSpace}.
	 */
	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testIdentify() throws AgentLifecycleException,
			InterruptedException, TupleSpaceException {

		IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		Thread.sleep(1000);
		assertTrue("Agent not started", agent.isRunning());
		TupleSpace ts = new TupleSpace(new User("IdentifyTester"), TS_HOST,
				TS_PORT, AgentProtocol.COMMAND_SPACE_NAME);
		VMID vmid = new VMID();
		String queryId = vmid.toString();
		Tuple requestIdent = (AgentProtocol.getIdentifyTuple(agent.getId(),
				agent.getName(), vmid));
		requestIdent.setExpiration(3 * 1000);
		ts.write(requestIdent);
		// After a short while there should be a response of the agent
		Tuple tuple = ts.waitToTake(new Tuple(AgentProtocol.RESPONSE, queryId,
				agent.getId(), agent.getName(), AgentProtocol.MESSAGE_IDENTIFY,
				Field.createWildCardField()));
		assertTrue("No ident-tuple taken...", tuple != null);
		agentManager.stopAgent(agent);
		Thread.sleep(1000);
		assertTrue("agent not stopped", !agent.isRunning());
		assertEquals(0, agentManager.getAgentsIdMap().size());
	}

	/**
	 * This test tries to test the {@code ALIVE} functionality of an agent using
	 * the {@link AgentManager} to start the agent. <br />
	 * After started the agent this test awaits an {@link Tuple} with an {@code
	 * ALIVE} response of the agent.
	 * 
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during {@code
	 *             AgentManager.startAgent()} or {@code
	 *             AgentManager.stopAgent()}.
	 * @throws InterruptedException
	 *             Is thrown if something went wrong during {@code
	 *             Thread.sleep()}.
	 * @throws TupleSpaceException
	 *             Is thrown if something went wrong inside the
	 *             {@link TupleSpace}.
	 */
	@SuppressWarnings("deprecation")
	@Ignore
	@Test(timeout = 50000)
	public void testAlive() throws AgentLifecycleException,
			InterruptedException, TupleSpaceException {
		IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		Thread.sleep(1000);
		assertTrue("Agent not started", agent.isRunning());
		TupleSpace ts = new TupleSpace(new User("AliveReader"), TS_HOST,
				TS_PORT, AgentProtocol.COMMAND_SPACE_NAME);
		Tuple t = new Tuple(AgentProtocol.COMMAND_LINE, String.class, agent
				.getId(), agent.getName(), AgentProtocol.ALIVE);
		Tuple aliveTuple = ts.read(t);
		// There should be an alive-tuple
		assertTrue("No alive-tuple taken...", aliveTuple != null);
		agentManager.stopAgent(agent);
		Thread.sleep(1000);
		assertTrue("agent not stopped", !agent.isRunning());
		assertEquals(0, agentManager.getAgentsIdMap().size());
	}

	/**
	 * This test tries to kill an agent using the {@link AgentManager} to start
	 * and kill the agent. <br />
	 * After started and killed the agent this test awaits an {@link Tuple} with
	 * an {@code ALIVE} response of the agent and assert that this tuple does
	 * not exist.
	 * 
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during {@code
	 *             AgentManager.startAgent()} or {@code
	 *             AgentManager.killAgent()}.
	 * @throws InterruptedException
	 *             Is thrown if something went wrong during {@code
	 *             Thread.sleep()}.
	 * @throws TupleSpaceException
	 *             Is thrown if something went wrong inside the
	 *             {@link TupleSpace}.
	 */
	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testKill() throws AgentLifecycleException,
			InterruptedException, TupleSpaceException {
		IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		Thread.sleep(1000);
		assertTrue("Agent not started", agent.isRunning());
		agentManager.killAgent(agent.getId());
		TupleSpace ts = new TupleSpace(new User("AliveReader"), TS_HOST,
				TS_PORT, AgentProtocol.COMMAND_SPACE_NAME);
		Tuple t = new Tuple(AgentProtocol.COMMAND_LINE, String.class, agent
				.getId(), agent.getName(), AgentProtocol.ALIVE);
		Thread.sleep(AgentProtocol.ALIVE_INTERVAL * 2);
		Tuple aliveTuple = ts.read(t);
		// The alive-tuple should not be there...
		assertTrue("Agent not killed", aliveTuple == null);
		assertEquals(0, agentManager.getAgentsIdMap().size());
	}

	/**
	 * This test looks if the maps of the currently working agents contain right
	 * values.
	 * 
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during {@code
	 *             AgentManager.startAgent()} or {@code
	 *             AgentManager.stopAgent()}.
	 * @throws InterruptedException
	 *             Is thrown if something went wrong during {@code
	 *             Thread.sleep()}.
	 */
	@SuppressWarnings("deprecation")
	@Ignore("takes too long")
	@Test
	public void testAgentMaps() throws AgentLifecycleException,
			InterruptedException {

		// TODO Array
		IThreadedAgent agent1 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent1 not started", agent1.isRunning());
		IThreadedAgent agent2 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent2 not started", agent2.isRunning());
		IThreadedAgent agent3 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent3 not started", agent3.isRunning());
		IThreadedAgent agent4 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent4 not started", agent4.isRunning());
		IThreadedAgent agent5 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent5 not started", agent5.isRunning());
		IThreadedAgent agent6 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent6 not started", agent6.isRunning());
		IThreadedAgent agent7 = agentManager.startAgent(ThreadedAgentMock.NAME,
				null);
		assertTrue("Agent7 not started", agent7.isRunning());
		// Assume that 7 agents are registered inside the agentManager
		assertEquals(7, agentManager.getAgentsIdMap().size());
		Thread.sleep(1000);
		agentManager.stopAgent(agent1);
		agentManager.stopAgent(agent2);
		agentManager.stopAgent(agent3);
		agentManager.stopAgent(agent4);
		agentManager.stopAgent(agent5);
		agentManager.stopAgent(agent6);
		agentManager.stopAgent(agent7);
		Thread.sleep(1000);
		// After the agents are stopped there should be 0 agents inside the
		// getAgentsIdMap-Map and 7 agents inside getOldAgentsMap-Map.
		assertEquals(0, agentManager.getAgentsIdMap().size());
		assertEquals(7, agentManager.getOldAgentsMap().size());
		Thread.sleep(AgentProtocol.ALIVE_INTERVAL * 3);
		// After the last alive-tuple is expired the maps should be empty
		assertEquals(0, agentManager.getAgentsIdMap().size());
		assertEquals(0, agentManager.getOldAgentsMap().size());

	}
	// @Test
	// public void testMassiveCommunication() throws Exception {
	//
	// ThreadedAgentMock[] array = new ThreadedAgentMock[50];
	// for (int i = 0; i < array.length; i++) {
	// Thread.sleep(50);
	// array[i] = (ThreadedAgentMock)
	// agentManager.startAgent(ThreadedAgentMock.NAME, null);
	// }
	// Thread.sleep(10000);
	// for (int i = 0; i < array.length; i++) {
	// Thread.sleep(50);
	// agentManager.stopAgent(array[i]);
	// }
	//
	// }

}
