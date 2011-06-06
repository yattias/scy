package eu.scy.agents.topics;

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

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.metadata.keys.KeyValuePair;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class RetrieveEloForGivenTopicTest extends AbstractTestFixture {

	private static final int SEARCHED_TOPIC = 2;
	private VMID agentId;
	private IELO topicElo1;
	private IELO topicElo2;
	private IELO topicElo3;

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

		initElos();

		agentId = new VMID();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, agentId);
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		agentMap.put(RetrieveEloForGivenTopic.NAME, params);
		startAgentFramework(agentMap);
	}

	private void initElos() {
		topicElo1 = createNewElo("topicElo1", "scy/text");
		IMetadataValueContainer container1 = topicElo1.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES));
		for (int i = 0; i < 10; i++) {
			KeyValuePair topicEntry = new KeyValuePair();
			topicEntry.setKey("" + i);
			topicEntry.setValue("" + 0.1);
			container1.addValue(topicEntry);
		}

		topicElo2 = createNewElo("topicElo2", "scy/text");
		IMetadataValueContainer container2 = topicElo2.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES));
		for (int i = 0; i < 10; i++) {
			if (i == SEARCHED_TOPIC) {
				KeyValuePair topicEntry = new KeyValuePair();
				topicEntry.setKey("" + i);
				topicEntry.setValue("" + 0.001);
				container2.addValue(topicEntry);
			} else {
				KeyValuePair topicEntry = new KeyValuePair();
				topicEntry.setKey("" + i);
				topicEntry.setValue("" + 0.1);
				container2.addValue(topicEntry);
			}
		}

		topicElo3 = createNewElo("topicElo3", "scy/text");
		IMetadataValueContainer container3 = topicElo3.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES));
		for (int i = 0; i < 10; i++) {
			if (i == SEARCHED_TOPIC) {
				KeyValuePair topicEntry = new KeyValuePair();
				topicEntry.setKey("" + i);
				topicEntry.setValue("" + 0.6);
				container3.addValue(topicEntry);
			} else {
				KeyValuePair topicEntry = new KeyValuePair();
				topicEntry.setKey("" + i);
				topicEntry.setValue("" + 0.04444444444444444);
				container3.addValue(topicEntry);
			}
		}

		repository.addNewELO(topicElo1);
		repository.addNewELO(topicElo2);
		repository.addNewELO(topicElo3);
	}

	@Test
	public void testSearchForTopicElos() throws TupleSpaceException {
		String queryId = new VMID().toString();
		getCommandSpace().write(new Tuple("getTopicElos", AgentProtocol.QUERY, queryId, SEARCHED_TOPIC, 0.1));
		Tuple response = getCommandSpace().waitToTake(
				new Tuple("getTopicElos", AgentProtocol.RESPONSE, queryId, Integer.class, Field.createWildCardField()),
				5000);
		assertNotNull("response empty", response);
		int number = (Integer) response.getField(3).getValue();
		assertEquals(2, number);
		String uri1 = (String) response.getField(4).getValue();
		assertEquals("roolo://memory/1/0/topicElo1.scytext", uri1);
		String uri2 = (String) response.getField(5).getValue();
		assertEquals("roolo://memory/3/0/topicElo3.scytext", uri2);
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}
}
