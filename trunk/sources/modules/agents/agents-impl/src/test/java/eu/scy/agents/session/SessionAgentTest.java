package eu.scy.agents.session;

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
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;
import eu.scy.agents.session.SessionAgent;

public class SessionAgentTest extends AbstractTestFixture {

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
		agentMap.put(SessionAgent.NAME, params);
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
		Tuple tuple = new Tuple(ActionConstants.ACTION, new VMID().toString(),
				System.currentTimeMillis(), ActionConstants.ACTION_LOG_IN,
				"harald", "tool", "mission1", "session", "eloUri",
				"language=de", "missionSpecification=" + MISSION1,
				"missionName=co2");
		getActionSpace().write(tuple);
		Tuple tuple2 = new Tuple(ActionConstants.ACTION, new VMID().toString(),
				System.currentTimeMillis(), ActionConstants.ACTION_LOG_IN,
				"sophie", "tool", "mission1", "session", "eloUri",
				"language=de", "missionSpecification=" + MISSION1,
				"missionName=co2");
		getActionSpace().write(tuple2);
	}
}
