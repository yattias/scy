package eu.scy.agents.groupformation;

import static org.junit.Assert.assertEquals;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.general.UserLocationAgent;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.*;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
		agentMap.put(UserLocationAgent.NAME, params);
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
		System.out.println();
	}

	private Tuple lasChangeTuple(String user, String las, String oldLas) {
		return new Tuple(AgentProtocol.ACTION, new VMID().toString(),
				System.currentTimeMillis(), AgentProtocol.ACTION_LAS_CHANGED,
				user, "scymapper", MISSION1, "session1", "roolo://test/elo1",
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
}
