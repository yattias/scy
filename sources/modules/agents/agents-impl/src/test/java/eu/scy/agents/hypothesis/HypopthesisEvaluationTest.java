package eu.scy.agents.hypothesis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTfIdfKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.OntologyKeywordsAgent;

public class HypopthesisEvaluationTest extends AbstractTestFixture {

	private static final String ELO_TYPE = "scy/xproc";

	private IELO elo;

//	String[] expectedKeywords = new String[] { "ingredients", "nontoxic",
//			"binder", "solvent", "labels", "toxic", "chemical", "voc", "paint",
//			"health", "natural", "pigment" };

	private static final long TIME_IN_MILLIS = 666;

	private static final String UUID1234 = "uuid1234";

	private String eloPath;

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

		initDfModel();
		initTopicModel();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		this.agentMap.put(ExtractKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
		this.agentMap.put(HypothesisEvaluationAgent.NAME, params);
		this.agentMap.put(OntologyKeywordsAgent.NAME, params);
		this.startAgentFramework(this.agentMap);

		InputStream inStream = this.getClass().getResourceAsStream(
				"/copexExampleElo.xml");
		String eloContent = readFile(inStream);
		elo = createNewElo("TestCopex", ELO_TYPE);
		elo.setContent(new BasicContent(eloContent));
		IMetadata metadata = repository.addNewELO(elo);
		URI eloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager
						.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER))
				.getValue();
		this.eloPath = eloUri.toString();
	}

	@Override
	@After
	public void tearDown() {
		try {
			if (getPersistentStorage() != null) {
				removeDFModel();
				removeTopicModel();
			}
			this.stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRun() throws TupleSpaceException, IOException,
			ClassNotFoundException {
		Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS,
				AgentProtocol.ACTION_ELO_SAVED, "testUser", "SomeTool",
				"SomeMission", "TestSession", eloPath, "elo_type=" + ELO_TYPE,
                "elo_uri=" + eloPath);
		getActionSpace().write(tuple);

		Tuple response = this.getCommandSpace().waitToTake(
				new Tuple(HypothesisEvaluationAgent.EVAL, String.class,
						String.class, String.class, String.class, String.class,
						Field.createWildCardField()),
				AgentProtocol.ALIVE_INTERVAL * 6);
		assertNotNull("no response received", response);
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(
				(byte[]) response.getField(6).getValue());
		ObjectInputStream objectIn = new ObjectInputStream(bytesIn);
		HashMap<Integer, Integer> histogram = (HashMap<Integer, Integer>) objectIn
				.readObject();
		String string = histogram.toString();
		assertEquals("{0=1, 2=1, 3=1, 4=1}", string);

	}
}
