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

public class TopicDetectorTest extends AbstractTestFixture {

	// // #0 9(0.8) 2(0.1502394136378272) 3(0.04976058636217276)
	private static final String TEXT = "divided different took reference types electrical standard ceilings examined pyronometer night radiation temperature house ducts resultant air problems times dealers expect savings remained washed admittedly setback located intervals average energy evening unless summer rest houses house mentioned time home styles apparent identical condensation temperatures ice chris house strengthen rating summer increased thermostat cloudy house days setforward thermostat totaling 64 reach trends shaped lead argonfilled question houses success basement focuses partly time surface mentioned point savings apparent discussing cavity nottingham set early insulating insulation house time report setforward house early completely storm common trials condensation feels surface indoor test says volume";

	@BeforeClass
	public static void startTS() {
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		startTupleSpaceServer();
		super.setUp();

		initTopicModel();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
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
	public void testProcessElo() throws TupleSpaceException, IOException, ClassNotFoundException {
		String queryID = new VMID().toString();
		System.out.println("Writing tuple");
		getCommandSpace().write(
				new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.QUERY, queryID, "co2_scy_english", TEXT));
		System.out.println("Tuple written. Waiting for response...");
		Tuple t = getCommandSpace().waitToTake(
				new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.RESPONSE, queryID, Field.createWildCardField()),
				5000);
		System.out.println("Response received");
		assertNotNull("tuple is null", t);
		ObjectInputStream bytesIn = new ObjectInputStream(new ByteArrayInputStream((byte[]) t.getField(3).getValue()));
		HashMap<Integer, Double> topicScoresMap = (HashMap<Integer, Double>) bytesIn.readObject();

		assertEquals(10, topicScoresMap.size());
		assertEquals("wrong probability for topic 0", 0.0017892133644281931, topicScoresMap.get(0), 0.01);
		assertEquals("wrong probability for topic 1", 0.002575589897297382, topicScoresMap.get(1), 0.01);
		assertEquals("wrong probability for topic 4", 0.00227468953178241, topicScoresMap.get(4), 0.01);
		assertEquals("wrong probability for topic 5", 0.0016823702862740107, topicScoresMap.get(5), 0.01);
		assertEquals("wrong probability for topic 6", 0.001945861865589766, topicScoresMap.get(6), 0.01);
		assertEquals("wrong probability for topic 7", 0.002655118753113757, topicScoresMap.get(7), 0.01);
		assertEquals("wrong probability for topic 8", 0.0019143937827241963, topicScoresMap.get(8), 0.01);

		assertEquals("wrong probability for topic 2", 0.1603676990866432, topicScoresMap.get(2), 0.03);
		assertEquals("wrong probability for topic 3", 0.05110776312074621, topicScoresMap.get(3), 0.03);
		assertEquals("wrong probability for topic 9", 0.7885358343528651, topicScoresMap.get(9), 0.03);

	}
}
