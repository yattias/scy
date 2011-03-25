package eu.scy.agents.topics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;

public class TopicDetectorTest extends AbstractTestFixture {

	// // #0 9(0.8) 2(0.1502394136378272) 3(0.04976058636217276)
	private static final String TEXT = "divided different took reference types electrical standard ceilings examined pyronometer night radiation temperature house ducts resultant air problems times dealers expect savings remained washed admittedly setback located intervals average energy evening unless summer rest houses house mentioned time home styles apparent identical condensation temperatures ice chris house strengthen rating summer increased thermostat cloudy house days setforward thermostat totaling 64 reach trends shaped lead argonfilled question houses success basement focuses partly time surface mentioned point savings apparent discussing cavity nottingham set early insulating insulation house time report setforward house early completely storm common trials condensation feels surface indoor test says volume";

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
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		agentMap.put(RooloAccessorAgent.class.getName(), params);
		agentMap.put(TopicDetector.NAME, params);
		startAgentFramework(agentMap);
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() throws TupleSpaceException, IOException,
			ClassNotFoundException {
		String queryID = new VMID().toString();
		System.out.println("Writing tuple");
		getCommandSpace().write(
				new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.QUERY,
						queryID, MISSION1, "en", TEXT));
		System.out.println("Tuple written. Waiting for response...");
		Tuple t = getCommandSpace().waitToTake(
				new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.RESPONSE,
						queryID, Field.createWildCardField()),
				AgentProtocol.SECOND * 30);
		assertNotNull("tuple is null", t);
		System.out.println("Response received");
		ObjectInputStream bytesIn = new ObjectInputStream(
				new ByteArrayInputStream((byte[]) t.getField(3).getValue()));
		HashMap<Integer, Double> topicScoresMap = (HashMap<Integer, Double>) bytesIn
				.readObject();

		assertEquals(15, topicScoresMap.size());
		assertEquals("wrong probability for topic 0", 8.617945979695665E-4,
				topicScoresMap.get(0), 0.01);
		assertEquals("wrong probability for topic 1", 0.004857312259509067,
				topicScoresMap.get(1), 0.01);
		assertEquals("wrong probability for topic 4", 0.10590514547817416,
				topicScoresMap.get(4), 0.01);
		assertEquals("wrong probability for topic 5", 0.0033541530235643288,
				topicScoresMap.get(5), 0.01);
		assertEquals("wrong probability for topic 6", 0.0459014358663772,
				topicScoresMap.get(6), 0.01);
		assertEquals("wrong probability for topic 7", 0.0030242939989562866,
				topicScoresMap.get(7), 0.01);
		assertEquals("wrong probability for topic 8", 0.0020630692385972455,
				topicScoresMap.get(8), 0.01);

		assertEquals("wrong probability for topic 2", 0.5755461799746437,
				topicScoresMap.get(2), 0.03);
		assertEquals("wrong probability for topic 3", 0.14900830716040536,
				topicScoresMap.get(3), 0.03);
		assertEquals("wrong probability for topic 9", 9.088862492220839E-4,
				topicScoresMap.get(9), 0.03);

	}
}
