package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;

public class TopicDetectorTest extends AbstractTestFixture {

	// private TopicDetector topicDetectorAgent;

	// private IELO elo;
	//
	// private URI eloURI;
	//
	// // #0 9(0.8) 2(0.1502394136378272) 3(0.04976058636217276)
	// private static final String TEXT =
	// "divided different took reference types electrical standard ceilings examined pyronometer night radiation temperature house ducts resultant air problems times dealers expect savings remained washed admittedly setback located intervals average energy evening unless summer rest houses house mentioned time home styles apparent identical condensation temperatures ice chris house strengthen rating summer increased thermostat cloudy house days setforward thermostat totaling 64 reach trends shaped lead argonfilled question houses success basement focuses partly time surface mentioned point savings apparent discussing cavity nottingham set early insulating insulation house time report setforward house early completely storm common trials condensation feels surface indoor test says volume";

	@Before
	public void setUp() throws Exception {
		// super.setUp();
		//
		// initModel();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("id", new VMID());
		// params.put("tsHost", TSHOST);
		// params.put("tsPort", TSPORT);
		// params.put(TopicDetector.MODEL_NAME, "co2_scy_english");
		// agentMap.put("eu.scy.agents.topics.TopicDetector", params);
		// startAgentFramework(agentMap);
		//
		// elo = createNewElo("testELO", "scy/text");
		// elo.setContent(new BasicContent(TEXT.getBytes()));
		// IMetadata data = repository.addNewELO(elo);
		// eloURI = (URI) data.getMetadataValueContainer(
		// typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER
		// .getId())).getValue();
		// System.out.println("EloURI " + eloURI);
	}

	@After
	public void tearDown() throws AgentLifecycleException {
		// stopAgentFrameWork();
	}

	// private void initModel() {
	// ObjectInputStream in = null;
	// try {
	// InputStream inStream = this.getClass().getResourceAsStream(
	// "/model.dat");
	// in = new ObjectInputStream(inStream);
	// TopicModelParameter model = (TopicModelParameter) in.readObject();
	// in.close();
	// PersistentStorage storage = getPersistentStorage();
	// storage.put("co2_scy_english", model);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	//@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() throws TupleSpaceException,
			InterruptedException, IOException, ClassNotFoundException,
			URISyntaxException {

		// getTupleSpace().write(new Tuple("topicDetector", eloURI.toString()));
		//
		// Tuple t = getTupleSpace().waitToTake(
		// new Tuple("topicDetector", String.class, Field
		// .createWildCardField()), 5000);
		//
		// assertNotNull("tuple is null", t);
		// ObjectInputStream bytesIn = new ObjectInputStream(
		// new ByteArrayInputStream((byte[]) t.getField(2).getValue()));
		// HashMap<Integer, Double> topicScoresMap = (HashMap<Integer, Double>)
		// bytesIn
		// .readObject();
		//
		// assertEquals(10, topicScoresMap.size());
		// assertEquals("wrong probability for topic 0", 0.0017892133644281931,
		// topicScoresMap.get(0), 0.01);
		// assertEquals("wrong probability for topic 1", 0.002575589897297382,
		// topicScoresMap.get(1), 0.01);
		// assertEquals("wrong probability for topic 4", 0.00227468953178241,
		// topicScoresMap.get(4), 0.01);
		// assertEquals("wrong probability for topic 5", 0.0016823702862740107,
		// topicScoresMap.get(5), 0.01);
		// assertEquals("wrong probability for topic 6", 0.001945861865589766,
		// topicScoresMap.get(6), 0.01);
		// assertEquals("wrong probability for topic 7", 0.002655118753113757,
		// topicScoresMap.get(7), 0.01);
		// assertEquals("wrong probability for topic 8", 0.0019143937827241963,
		// topicScoresMap.get(8), 0.01);
		//
		// assertEquals("wrong probability for topic 2", 0.1603676990866432,
		// topicScoresMap.get(2), 0.03);
		// assertEquals("wrong probability for topic 3", 0.05110776312074621,
		// topicScoresMap.get(3), 0.03);
		// assertEquals("wrong probability for topic 9", 0.7885358343528651,
		// topicScoresMap.get(9), 0.03);
		//
		// elo = repository
		// .retrieveELO(new URI((String) t.getField(1).getValue()));
		// List<String> topicScores = (List<String>) elo
		// .getMetadata()
		// .getMetadataValueContainer(
		// typeManager
		// .getMetadataKey(TopicDetector.KEY_TOPIC_SCORES))
		// .getValueList();
		// assertEquals(10, topicScores.size());
		// assertEquals("wrong probability for topic 0", 0.0017892133644281931,
		// getTopicScore(topicScores.get(0)), 0.01);
		// assertEquals("wrong probability for topic 1", 0.002575589897297382,
		// getTopicScore(topicScores.get(1)), 0.01);
		// assertEquals("wrong probability for topic 4", 0.00227468953178241,
		// getTopicScore(topicScores.get(4)), 0.01);
		// assertEquals("wrong probability for topic 5", 0.0016823702862740107,
		// getTopicScore(topicScores.get(5)), 0.01);
		// assertEquals("wrong probability for topic 6", 0.001945861865589766,
		// getTopicScore(topicScores.get(6)), 0.01);
		// assertEquals("wrong probability for topic 7", 0.002655118753113757,
		// getTopicScore(topicScores.get(7)), 0.01);
		// assertEquals("wrong probability for topic 8", 0.0019143937827241963,
		// getTopicScore(topicScores.get(8)), 0.01);
		//
		// assertEquals("wrong probability for topic 2", 0.1603676990866432,
		// getTopicScore(topicScores.get(2)), 0.03);
		// assertEquals("wrong probability for topic 3", 0.05110776312074621,
		// getTopicScore(topicScores.get(3)), 0.03);
		// assertEquals("wrong probability for topic 9", 0.7885358343528651,
		// getTopicScore(topicScores.get(9)), 0.03);

	}

	// private double getTopicScore(String topicScores) {
	// String score = topicScores.substring(topicScores.indexOf(':') + 1);
	// return Double.parseDouble(score);
	// }
}
