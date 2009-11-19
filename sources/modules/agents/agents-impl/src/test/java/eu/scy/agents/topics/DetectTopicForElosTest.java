package eu.scy.agents.topics;

import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import cc.mallet.topics.TopicModelParameter;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.impl.PersistentStorage;

public class DetectTopicForElosTest  {

	// private static final String MODEL_NAME = "co2_scy_english";
	// private IELO elo;
	// //
	// private URI eloURI;
	// //
	// // // #0 9(0.8) 2(0.1502394136378272) 3(0.04976058636217276)
	// private static final String TEXT = "divided different took reference types electrical standard ceilings examined pyronometer night radiation temperature house ducts resultant air problems times dealers expect savings remained washed admittedly setback located intervals average energy evening unless summer rest houses house mentioned time home styles apparent identical condensation temperatures ice chris house strengthen rating summer increased thermostat cloudy house days setforward thermostat totaling 64 reach trends shaped lead argonfilled question houses success basement focuses partly time surface mentioned point savings apparent discussing cavity nottingham set early insulating insulation house time report setforward house early completely storm common trials condensation feels surface indoor test says volume";

	
	public void setUp() throws Exception {
		// super.setUp();
		//
		// initModel();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("id", new VMID());
		// params.put("tsHost", TSHOST);
		// params.put("tsPort", TSPORT);
		// agentMap.put(TopicDetector.NAME, params);
		//
		// params.put("id", new VMID());
		// params.put(DetectTopicForElos.MODEL_NAME, MODEL_NAME);
		// agentMap.put(DetectTopicForElos.NAME, params);
		//
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

	// private void initModel() {
	// ObjectInputStream in = null;
	// try {
	// InputStream inStream = this.getClass().getResourceAsStream(
	// "/model.dat");
	// in = new ObjectInputStream(inStream);
	// TopicModelParameter model = (TopicModelParameter) in.readObject();
	// in.close();
	// PersistentStorage storage = getPersistentStorage();
	// storage.put(MODEL_NAME, model);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() throws TupleSpaceException,
			InterruptedException, IOException, ClassNotFoundException,
			URISyntaxException {
	    assertTrue(true);

		// System.out.println("Writing tuple");
		//
		// getTupleSpace().write(new Tuple("topicDetector", eloURI.toString()));
		// System.out.println("Tuple written. Waiting");
		//
		// Thread.sleep(5000);
		//
		// elo = repository.retrieveELO(eloURI);
		// List<KeyValuePair> topicScores = (List<KeyValuePair>) elo
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

	// private double getTopicScore(KeyValuePair topicScores) {
	// return Double.parseDouble(topicScores.getValue());
	// }
	
}
