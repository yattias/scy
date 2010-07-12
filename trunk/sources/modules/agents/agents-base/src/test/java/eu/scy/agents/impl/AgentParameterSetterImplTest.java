package eu.scy.agents.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.AgentParameterSetter;

public class AgentParameterSetterImplTest {

	private static final String TSHOST = "localhost";
	private static final int TSPORT = 2525;
	private AgentParameterSetter agentParameterSetter;
	private TupleSpace tupleSpace;
	private ThreadedAgentMock agent;

	@BeforeClass
	public static void startTS() {
		if (!Server.isRunning()) {
			Configuration conf = Configuration.getConfiguration();
			conf.setNonSSLPort(TSPORT);
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

	@AfterClass
	public static void stopTS() {
		Server.stopServer();
	}

	@Before
	public void setUp() throws TupleSpaceException, AgentLifecycleException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AgentProtocol.PARAM_AGENT_ID, "ID1");
		agent = new ThreadedAgentMock(map);
		agent.start();

		agentParameterSetter = new AgentParameterSetterImpl(TSHOST, TSPORT);
		tupleSpace = new TupleSpace(TSHOST, TSPORT,
				AgentProtocol.COMMAND_SPACE_NAME);
	}

	@After
	public void tearDown() throws TupleSpaceException, AgentLifecycleException {
		tupleSpace.disconnect();
		agent.stop();
	}

	@Test
	public void testGetParameterAgentnameMissionUserParamname() {
		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME,
						ThreadedAgentMock.MISSION, ThreadedAgentMock.USER,
						ThreadedAgentMock.TEST_PARAMETER));
	}

	@Test
	public void testGetParameterAgentnameMissionParamname() {
		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME,
						ThreadedAgentMock.MISSION,
						ThreadedAgentMock.TEST_PARAMETER));
	}

	@Test
	public void testGetParameterAgentnameParamname() {
		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME,
						ThreadedAgentMock.TEST_PARAMETER));
	}

	@Test
	public void testSetParameterAgentnameMissionUserParamnameValue()
			throws TupleSpaceException, InterruptedException {
		agentParameterSetter.setParameter("TestAgent", "TestMission",
				"TestUser", "TestParam", "TestValue");
		testWrittenTuple("TestAgent", "TestMission", "TestUser", "TestParam",
				"TestValue");
	}

	@Test
	public void testSetParameterAgentnameMissionParamnameValue()
			throws TupleSpaceException, InterruptedException {
		agentParameterSetter.setParameter("TestAgent", "TestMission",
				"TestParam", "TestValue");
		testWrittenTuple("TestAgent", "TestMission", null, "TestParam",
				"TestValue");
	}

	@Test
	public void testSetParameterAgentnameParamnameValue()
			throws TupleSpaceException, InterruptedException {
		agentParameterSetter
				.setParameter("TestAgent", "TestParam", "TestValue");
		testWrittenTuple("TestAgent", null, null, "TestParam", "TestValue");
	}

	private void testWrittenTuple(String agentName, String expectedMission,
			String expectedUser, String expectedParameterName,
			String expectedValue) throws TupleSpaceException,
			InterruptedException {
		// Thread.sleep(1000);
		Tuple tuple = tupleSpace.take(AgentProtocol
				.getParameterSetTupleTemplate(agentName));
		assertNotNull(tuple);
		if (expectedMission == null) {
			assertNull(tuple.getField(2).getValue());
		} else {
			assertEquals(expectedMission, tuple.getField(2).getValue());
		}
		if (expectedUser == null) {
			assertNull(tuple.getField(3).getValue());
		} else {
			assertEquals(expectedUser, tuple.getField(3).getValue());
		}
		assertEquals(expectedParameterName, tuple.getField(4).getValue());
		assertEquals(expectedValue, tuple.getField(5).getValue());
	}
}
