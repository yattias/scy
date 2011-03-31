package eu.scy.agents.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;

public class UserLocationAgentTest extends AbstractTestFixture {

	private static final String MISSION1 = "mission1";

	@BeforeClass
	public static void startTS() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		this.agentMap.put(UserLocationAgent.NAME, params);
		this.startAgentFramework(this.agentMap);
	}

	@Override
	@After
	public void tearDown() {
		try {
			this.stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetLasForUser() throws TupleSpaceException,
			InterruptedException {
		getActionSpace().write(lasChangeTuple("user1", "las1"));
		getActionSpace().write(lasChangeTuple("user2", "las2"));

		getCommandSpace().write(getLasTuple("user1"));
		getActionSpace().write(lasChangeTuple("user2", "las1"));
		Thread.sleep(1000);
		Tuple response = getCommandSpace().waitToTake(
				new Tuple(UserLocationAgent.USER_INFO_REQUEST,
						AgentProtocol.RESPONSE, String.class, String.class),
				AgentProtocol.ALIVE_INTERVAL);
		assertNotNull(response);
		assertEquals("las1", response.getField(3).getValue());
	}

	@Test
	public void testGetUsersInLas() throws TupleSpaceException,
			InterruptedException {
		getActionSpace().write(lasChangeTuple("user1", "las2"));
		getActionSpace().write(lasChangeTuple("user2", "las2"));

		getCommandSpace().write(getUsersInLasTuple("las2"));
		Thread.sleep(1000);
		Tuple response = getCommandSpace().waitToTake(
				new Tuple(UserLocationAgent.USER_INFO_REQUEST,
						AgentProtocol.RESPONSE, String.class, Field
								.createWildCardField()),
				AgentProtocol.ALIVE_INTERVAL);
		assertNotNull(response);
		assertEquals("user2", response.getField(3).getValue());
		assertEquals("user1", response.getField(4).getValue());
	}

	@Test
	public void testGetUsersInMission() throws TupleSpaceException,
			InterruptedException {
		getActionSpace().write(lasChangeTuple("user1", "las2"));
		getActionSpace().write(lasChangeTuple("user2", "las1"));

		getCommandSpace().write(getUsersInMissionTuple());
		Thread.sleep(1000);
		Tuple response = getCommandSpace().waitToTake(
				new Tuple(UserLocationAgent.USER_INFO_REQUEST,
						AgentProtocol.RESPONSE, String.class, Field
								.createWildCardField()),
				AgentProtocol.ALIVE_INTERVAL);
		assertNotNull(response);
		assertEquals("user2", response.getField(3).getValue());
		assertEquals("user1", response.getField(4).getValue());
	}

	private Tuple getUsersInMissionTuple() {
		return new Tuple(UserLocationAgent.USER_INFO_REQUEST,
				AgentProtocol.QUERY, new VMID().toString(), MISSION1,
				UserLocationAgent.METHOD_USERS_IN_MISSION);
	}

	private Tuple getUsersInLasTuple(String las) {
		return new Tuple(UserLocationAgent.USER_INFO_REQUEST,
				AgentProtocol.QUERY, new VMID().toString(), MISSION1,
				UserLocationAgent.METHOD_USERS_IN_LAS, las);
	}

	private Tuple getLasTuple(String user) {
		// ("userInfoRequest","query", <QueryId>:String, <Mission>:String,
		// <Method>:String, <Parameter>:String)
		return new Tuple(UserLocationAgent.USER_INFO_REQUEST,
				AgentProtocol.QUERY, new VMID().toString(), MISSION1,
				UserLocationAgent.METHOD_GET_LAS, user);
	}

	private Tuple lasChangeTuple(String user, String las) {
		return new Tuple(ActionConstants.ACTION, new VMID().toString(), System
				.currentTimeMillis(), ActionConstants.ACTION_LAS_CHANGED, user,
				"scymapper", MISSION1, "session1", "roolo://test/elo1",
				"newLasId=" + las, "oldLasId=oldLas");
	}
}
