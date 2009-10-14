package eu.scy.agents.topics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import cc.mallet.topics.TopicModelParameter;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.roolo.elo.helper.ELOFiller;

public class TopicDetectorTest extends AbstractTestFixture {

	private TopicDetector topicDetectorAgent;

	private IELO elo;

	// #0 9(0.8) 2(0.1502394136378272) 3(0.04976058636217276)
	private static final String TEXT = "divided different took reference types electrical standard ceilings examined pyronometer night radiation temperature house ducts resultant air problems times dealers expect savings remained washed admittedly setback located intervals average energy evening unless summer rest houses house mentioned time home styles apparent identical condensation temperatures ice chris house strengthen rating summer increased thermostat cloudy house days setforward thermostat totaling 64°f reach trends shaped lead argonfilled question houses success basement focuses partly time surface mentioned point savings apparent discussing cavity nottingham set early insulating insulation house time report setforward house early completely storm common trials condensation feels surface indoor test says volume";

	@BeforeClass
	public static void beforeAll() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void afterAll() {
		stopTupleSpaceServer();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		initModel();

		topicDetectorAgent = new TopicDetector("co2_scy_english");
		topicDetectorAgent.setMetadataTypeManager(typeManager);

		elo = createNewElo();
		ELOFiller eloFiller = new ELOFiller(elo, typeManager);
		eloFiller.fillValue(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
				"scy/text");
		elo.setContent(new BasicContent(TEXT.getBytes()));
	}

	private void initModel() {
		ObjectInputStream in = null;
		try {
			InputStream inStream = this.getClass().getResourceAsStream(
					"/model.dat");
			in = new ObjectInputStream(inStream);
			TopicModelParameter model = (TopicModelParameter) in.readObject();
			in.close();
			PersistentStorage storage = new PersistentStorage();
			storage.put("co2_scy_english", model);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() {
		topicDetectorAgent.processElo(elo);

		List<String> topicScores = (List<String>) elo
				.getMetadata()
				.getMetadataValueContainer(
						typeManager
								.getMetadataKey(TopicDetector.KEY_TOPIC_SCORES))
				.getValueList();
		assertEquals(10, topicScores.size());
		assertEquals(0.0017892133644281931, getTopicScore(topicScores.get(0)),
				0.005);
		assertEquals(0.002575589897297382, getTopicScore(topicScores.get(1)),
				0.005);
		assertEquals(0.00227468953178241, getTopicScore(topicScores.get(4)),
				0.005);
		assertEquals(0.0016823702862740107, getTopicScore(topicScores.get(5)),
				0.005);
		assertEquals(0.001945861865589766, getTopicScore(topicScores.get(6)),
				0.005);
		assertEquals(0.002655118753113757, getTopicScore(topicScores.get(7)),
				0.005);
		assertEquals(0.0019143937827241963, getTopicScore(topicScores.get(8)),
				0.005);

		assertEquals(0.1603676990866432, getTopicScore(topicScores.get(2)),
				0.03);
		assertEquals(0.05110776312074621, getTopicScore(topicScores.get(3)),
				0.03);
		assertEquals(0.7885358343528651, getTopicScore(topicScores.get(9)),
				0.03);
	}

	// public static void main(String[] args) throws IOException,
	// ClassNotFoundException {
	// InputStream inStream = TopicDetectorTest.class
	// .getResourceAsStream("/lda_model.dat");
	// ObjectInputStream in = new ObjectInputStream(inStream);
	// LDAHyper ldaModel = (LDAHyper) in.readObject();
	// in.close();
	// TopicModelParameter tmParameter = new TopicModelParameter(ldaModel);
	// tmParameter.write(new File("model.dat"));
	// }

	private double getTopicScore(String topicScores) {
		String score = topicScores.substring(topicScores.indexOf(':') + 1);
		return Double.parseDouble(score);
	}
}
