package eu.scy.agents.hypothesis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
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
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTfIdfKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.OntologyKeywordsAgent;

public class HypothesisEvaluationChainTest extends AbstractTestFixture {

	private static final String ELO_TYPE = "scy/xproc";
	private static final String MISSION = "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification";

	private IELO elo, smallElo;

	private static final long TIME_IN_MILLIS = 666;

	private static final String UUID1234 = "uuid1234";

	private String eloPath, smallEloPath;

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

		// this.initTopicModel();
		// this.initDfModel();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		this.agentMap.put(ExtractKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
		this.agentMap.put(OntologyKeywordsAgent.NAME, params);
		this.agentMap.put(HypothesisEvaluationAgent.NAME, params);
		this.agentMap.put(HypothesisDecisionMakerAgent.NAME, params);

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

		inStream = this.getClass().getResourceAsStream("/copexExampleElo2.xml");
		eloContent = readFile(inStream);
		smallElo = createNewElo("TestCopex2", ELO_TYPE);
		smallElo.setContent(new BasicContent(eloContent));
		metadata = repository.addNewELO(smallElo);
		eloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager
						.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER))
				.getValue();
		this.smallEloPath = eloUri.toString();

		System.out.println(eloUri.toString());
	}

	@Override
	@After
	public void tearDown() {
		try {
			this.stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRun() throws InterruptedException, TupleSpaceException,
			IOException {
		Tuple response = writeTupleGetResponse(this.eloPath);
		assertNotNull("no response received", response);
		String message = (String) response.getField(7).getValue();
		assertEquals(message, "message=ok");
		// assertEquals(message, "message=too few keywords or text too long");
		response = writeTupleGetResponse(this.smallEloPath);
		assertNotNull("no response received", response);
		message = (String) response.getField(7).getValue();
		assertEquals(message, "message=too few keywords or text too long");
	}

	private Tuple writeTupleGetResponse(String eloPath)
			throws TupleSpaceException {
		Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS,
				AgentProtocol.ACTION_ELO_SAVED, "testUser", "copex", MISSION,
				"TestSession", eloPath, "elo_type=" + ELO_TYPE, "elo_uri="
						+ eloPath);
		getActionSpace().write(tuple);

		Tuple responseTuple = new Tuple(AgentProtocol.NOTIFICATION,
				String.class, String.class, String.class, String.class,
				String.class, String.class, Field.createWildCardField());

		Tuple response = this.getCommandSpace().waitToTake(responseTuple,
				AgentProtocol.MINUTE * 6);
		return response;
	}
}
