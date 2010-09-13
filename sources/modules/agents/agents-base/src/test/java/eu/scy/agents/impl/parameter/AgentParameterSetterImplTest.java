package eu.scy.agents.impl.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ThreadedAgentMock;

public class AgentParameterSetterImplTest {

	private static final String TEST_AGENT = "TestAgent";
	private static final String TSHOST = "localhost";
	private static final int TSPORT = 2525;
	private AgentParameterAPI agentParameterSetter;
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

		agentParameterSetter = new AgentParameterAPIImpl(TSHOST, TSPORT);
		tupleSpace = new TupleSpace(TSHOST, TSPORT,
				AgentProtocol.COMMAND_SPACE_NAME);
	}

	@After
	public void tearDown() throws TupleSpaceException, AgentLifecycleException {
		tupleSpace.takeAll(new Tuple());
		tupleSpace.disconnect();
		agent.stop();
	}

	@Test
	public void testGetParameterAgentnameMissionUserParamname() {
		AgentParameter parameter = new AgentParameter();
		parameter.setMission(ThreadedAgentMock.MISSION);
		parameter.setUser(ThreadedAgentMock.USER);
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);

		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME, parameter));
	}

	@Test
	public void testGetParameterAgentnameMissionParamname() {
		AgentParameter parameter = new AgentParameter();
		parameter.setMission(ThreadedAgentMock.MISSION);
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);

		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME, parameter));
	}

	@Test
	public void testGetParameterAgentnameParamname() {
		AgentParameter parameter = new AgentParameter();
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);

		assertEquals(ThreadedAgentMock.TEST_VALUE, agentParameterSetter
				.getParameter(ThreadedAgentMock.NAME, parameter));
	}

	@Test
	public void testSetParameterAgentnameMissionUserParamnameValue()
			throws TupleSpaceException, InterruptedException {
		AgentParameter parameter = new AgentParameter();
		parameter.setMission(ThreadedAgentMock.MISSION);
		parameter.setUser(ThreadedAgentMock.USER);
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);
		parameter.setParameterValue(ThreadedAgentMock.TEST_VALUE);

		agentParameterSetter.setParameter(TEST_AGENT, parameter);
		testWrittenTuple(TEST_AGENT, ThreadedAgentMock.MISSION,
				ThreadedAgentMock.USER, ThreadedAgentMock.TEST_PARAMETER,
				ThreadedAgentMock.TEST_VALUE);
	}

	@Test
	public void testSetParameterAgentnameMissionParamnameValue()
			throws TupleSpaceException, InterruptedException {
		AgentParameter parameter = new AgentParameter();
		parameter.setMission(ThreadedAgentMock.MISSION);
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);
		parameter.setParameterValue(ThreadedAgentMock.TEST_VALUE);

		agentParameterSetter.setParameter(TEST_AGENT, parameter);
		testWrittenTuple(TEST_AGENT, ThreadedAgentMock.MISSION, null,
				ThreadedAgentMock.TEST_PARAMETER, ThreadedAgentMock.TEST_VALUE);
	}

	@Test
	public void testSetParameterAgentnameParamnameValue()
			throws TupleSpaceException, InterruptedException {
		AgentParameter parameter = new AgentParameter();
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);
		parameter.setParameterValue(ThreadedAgentMock.TEST_VALUE);

		agentParameterSetter.setParameter(TEST_AGENT, parameter);
		testWrittenTuple(TEST_AGENT, null, null,
				ThreadedAgentMock.TEST_PARAMETER, ThreadedAgentMock.TEST_VALUE);
	}

	@Test
	public void testGetAndSetParameter() throws TupleSpaceException,
			InterruptedException {
		AgentParameter parameter = new AgentParameter();
		parameter.setMission(ThreadedAgentMock.MISSION);
		parameter.setParameterName(ThreadedAgentMock.TEST_PARAMETER);
		parameter.setParameterValue("Test2");

		agentParameterSetter.setParameter(agent.getName(), parameter);
		Thread.sleep(1000);
		assertEquals("Test2", agentParameterSetter.getParameter(
				agent.getName(), parameter));

	}

	private void testWrittenTuple(String agentName, String expectedMission,
			String expectedUser, String expectedParameterName,
			String expectedValue) throws TupleSpaceException,
			InterruptedException {
		Thread.sleep(1000);
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
		assertTrue(true);
	}

	@Test
	public void testGetParameterList() {
		List<String> parameter = agentParameterSetter.listAgentParameter(agent
				.getName());
		assertEquals(1, parameter.size());
		assertEquals(ThreadedAgentMock.TEST_PARAMETER, parameter.get(0));
	}

	@Ignore
	@Test
	public void testWriteRaw() throws TupleSpaceException {
		Tuple t = AgentProtocol.getParameterSetTupleTemplate(agent.getName());
		t.getField(2).setValue("Test");
		t.getField(3).setValue("Test");
		t.getField(4).setValue("Test");
		t.getField(5).setValue("Test");
		tupleSpace.write(t);
	}
}
