package eu.scy.agents;

import static org.junit.Assert.assertEquals;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.MissionUriMapping;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;

public class MissionUriMappingTest extends AbstractTestFixture {

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

		startAgentFramework(agentMap);
	}

	@Override
    @After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}

	@Test
	public void testCreation() {
		MissionUriMapping missionUriMapping = new MissionUriMapping(
				getCommandSpace());
		assertEquals("mission1", missionUriMapping.getMission(MISSION1));
	}
}
