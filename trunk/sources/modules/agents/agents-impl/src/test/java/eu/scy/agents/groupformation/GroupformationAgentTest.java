package eu.scy.agents.groupformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.general.UserLocationAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;
import eu.scy.agents.session.SessionAgent;

/** @author fschulz */
public class GroupformationAgentTest extends AbstractTestFixture {

	private String MISSION1 = "mission1";

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
		agentMap.put(RooloAccessorAgent.NAME, params);
		agentMap.put(SessionAgent.NAME, params);
		agentMap.put(GroupFormationAgent.NAME, params);
		startAgentFramework(agentMap);
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}

	@Test
	public void testRun() throws TupleSpaceException {
		getActionSpace().write(login("user1"));
		getActionSpace().write(login("user2"));
		getActionSpace().write(login("user3"));

		getActionSpace()
				.write(lasChangeTuple("user2", "conceptualisatsionConceptMap",
						"some"));
		getActionSpace()
				.write(lasChangeTuple("user1", "conceptualisatsionConceptMap",
						"some"));
		getActionSpace()
				.write(lasChangeTuple("user3", "conceptualisatsionConceptMap",
						"some"));
		getActionSpace()
				.write(lasChangeTuple("user1", "some",
						"conceptualisatsionConceptMap"));
		Tuple response = this.getCommandSpace()
				.waitToTake(
						new Tuple(AgentProtocol.NOTIFICATION, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								Field.createWildCardField()),
						AgentProtocol.ALIVE_INTERVAL * 16);
		assertNotNull("no response received", response);
		String message = (String) response.getField(8).getValue();
		System.out.println(message);
	}

	private Tuple lasChangeTuple(String user, String las, String oldLas) {
		return new Tuple(ActionConstants.ACTION, new VMID().toString(),
				System.currentTimeMillis(), ActionConstants.ACTION_LAS_CHANGED,
				user, "scymapper", MISSION1, "session1",
				"roolo://memory/16/0/eco_reference_map.mapping", // change ELO
																	// URI to
																	// one from
																	// local ELO
																	// store,
																	// like
																	// roolo://memory/16/0/eco_reference_map.mapping
				"newLasId=" + las, "oldLasId=" + oldLas);
	}

	@Test
	public void testcreateUserListString() {
		GroupFormationAgent groupFormationAgent = new GroupFormationAgent(
				new HashMap<String, Object>());

		Set<String> group = new HashSet<String>();
		group.add("user1@scy.collide.info");
		group.add("user2@scy.collide.info");
		group.add("user3@scy.collide.info");
		String createUserListString = groupFormationAgent.createUserListString(
				"user1@scy.collide.info", group);
		assertEquals("user2, user3", createUserListString);

		Set<String> group2 = new HashSet<String>();
		group2.add("user1@scy.collide.info");
		group2.add("user2@scy.collide.info");
		String createUserListString2 = groupFormationAgent
				.createUserListString("user1@scy.collide.info", group2);
		assertEquals("user2", createUserListString2);
	}

	private Tuple login(String user) {
		return new Tuple(ActionConstants.ACTION, new VMID().toString(),
				System.currentTimeMillis(), ActionConstants.ACTION_LOG_IN,
				user, "scy-desktop", MISSION1, "n/a",
				"roolo://memory/16/0/eco_reference_map.mapping",
				"missionSpecification=" + MISSION1, "language=en",
				"missionName=co2");
	}
}
