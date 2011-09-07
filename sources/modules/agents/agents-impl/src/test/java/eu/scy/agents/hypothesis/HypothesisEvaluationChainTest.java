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
import java.util.Locale;
import java.util.ResourceBundle;

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
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.OntologyKeywordsAgent;
import eu.scy.agents.session.SessionAgent;

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
		this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
		this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
		this.agentMap.put(OntologyKeywordsAgent.NAME, params);
		this.agentMap.put(HypothesisEvaluationAgent.NAME, params);
		this.agentMap.put(HypothesisDecisionMakerAgent.NAME, params);
		this.agentMap.put(SessionAgent.NAME, params);

		this.startAgentFramework(this.agentMap);

		InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
		String eloContent = this.readFile(inStream);
		this.elo = this.createNewElo("TestCopex", ELO_TYPE);
		this.elo.setContent(new BasicContent(eloContent));
		IMetadata metadata = this.repository.addNewELO(this.elo);
		URI eloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
		this.eloPath = eloUri.toString();

		inStream = this.getClass().getResourceAsStream("/copexExampleElo2.xml");
		eloContent = this.readFile(inStream);
		this.smallElo = this.createNewElo("TestCopex2", ELO_TYPE);
		this.smallElo.setContent(new BasicContent(eloContent));
		metadata = this.repository.addNewELO(this.smallElo);
		eloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
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
	public void testRun() throws InterruptedException, TupleSpaceException, IOException {

		ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale("en"));
		this.login("testUser", MISSION1, Mission.MISSION1.getName(), "en", "co2");

		Tuple response = this.writeTupleGetResponse(this.eloPath);
		assertNotNull("no response received", response);
		String message = (String) response.getField(7).getValue();
		String expMsg = "message=" + messages.getString("HYPO_OK");
		assertEquals(message, expMsg);
		// assertEquals(message, "message=too few keywords or text too long");
		response = this.writeTupleGetResponse(this.smallEloPath);
		assertNotNull("no response received", response);
		message = (String) response.getField(7).getValue();
		expMsg = "message=" + messages.getString("HYPO_TOO_FEW_KEYWORDS");
		assertEquals(message, expMsg);
	}

	private Tuple writeTupleGetResponse(String eloPath) throws TupleSpaceException {
		Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS, ActionConstants.ACTION_ELO_SAVED, "testUser",
				"copex", MISSION, "TestSession", eloPath, "elo_type=" + ELO_TYPE, "elo_uri=" + eloPath);
		this.getActionSpace().write(tuple);

		Tuple responseTuple = new Tuple(AgentProtocol.NOTIFICATION, String.class, String.class, String.class,
				String.class, String.class, String.class, Field.createWildCardField());

		Tuple response = this.getCommandSpace().waitToTake(responseTuple, AgentProtocol.MINUTE * 6);
		return response;
	}
}
