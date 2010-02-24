package eu.scy.agents.topics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.UUID;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class TextForTopicSavedTest extends AbstractTestFixture {

	public String eloUri = "roolo://memory/1/testElo.scytext";

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
		agentMap.put(TextForTopicSaved.NAME, params);

		startAgentFramework(agentMap);
	}

	@Test
	public void testProcessElo() throws TupleSpaceException {
		getActionSpace().write(
				getTestActionTuple(eloUri, "scy/text", System.currentTimeMillis(), UUID.randomUUID().toString()));

		Tuple tuple = getCommandSpace().waitToTake(new Tuple(TopicAgents.TOPIC_DETECTOR, String.class),
				AgentProtocol.ALIVE_INTERVAL * 3);

		assertNotNull("no tuple sent", tuple);
		assertEquals("Uri not the same", eloUri, tuple.getField(1).getValue());
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}
}
