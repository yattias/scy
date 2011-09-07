package eu.scy.agents.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.EloTypes;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;
import eu.scy.agents.session.SessionAgent;
import roolo.elo.metadata.keys.KeyValuePair;

public class AddKeywordsToMetadataAgentTest extends AbstractTestFixture {

	private IELO copexElo, webResourceElo;

	private String copexEloPath, webResourceEloPath;

	private URI copexEloUri, webResourceEloUri;

	private static final long TIME_IN_MILLIS = 666;

	private static final String UUID1234 = "uuid1234";

	private AddKeywordsToMetadataAgent addMetadataAgent;

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
		this.agentMap.put(ExtractKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
		this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
		this.agentMap.put(OntologyKeywordsAgent.NAME, params);
		this.agentMap.put(RooloAccessorAgent.NAME, params);
		this.agentMap.put(SessionAgent.NAME, params);

		this.startAgentFramework(this.agentMap);

		InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
		String eloContent = this.readFile(inStream);
		inStream.close();
		this.copexElo = this.createNewElo("TestCopex", EloTypes.SCY_XPROC);
		this.copexElo.setContent(new BasicContent(eloContent));
		IMetadata metadata = this.repository.addNewELO(this.copexElo);
		this.copexEloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
		this.copexEloPath = this.copexEloUri.toString();

		inStream = this.getClass().getResourceAsStream("/scyLighterExample.xml");
		eloContent = this.readFile(inStream);
		inStream.close();
		this.webResourceElo = this.createNewElo("TestWebResource", EloTypes.SCY_WEBRESOURCER);
		this.webResourceElo.setContent(new BasicContent(eloContent));
		metadata = this.repository.addNewELO(this.webResourceElo);
		this.webResourceEloUri = (URI) metadata.getMetadataValueContainer(
				this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
		this.webResourceEloPath = this.webResourceEloUri.toString();

		this.addMetadataAgent = new AddKeywordsToMetadataAgent(params);
		this.addMetadataAgent.setRepository(this.repository);
		this.addMetadataAgent.setMetadataTypeManager(this.typeManager);

		System.out.println(this.copexEloUri.toString());
		System.out.println(this.webResourceEloUri.toString());
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

	@SuppressWarnings("unchecked")
	@Test
	public void testRun() throws InterruptedException, TupleSpaceException, IOException {

		this.login("SomeUser", MISSION1, "Design a CO2 friendly house", "en", "co2");

		// this.getActionSpace().write(
		// new Tuple(ActionConstants.ACTION, "ID", 122345L, ActionConstants.ACTION_LOG_IN, UUID1234,
		// "scy-desktop", MISSION1, "n/a", this.copexEloPath, "missionSpecification=" + MISSION1,
		// "language=en", "missionName=Design a CO2 friendly house"));
		//
		Thread.sleep(AgentProtocol.SECOND);
		this.addMetadataAgent.processELOSavedAction(ActionConstants.ACTION_ELO_SAVED, UUID1234, TIME_IN_MILLIS,
				"copex", "co2", "TestSession", this.copexEloPath, EloTypes.SCY_XPROC);

		IELO retrievedELO = this.repository.retrieveELOLastVersion(this.copexEloUri);
		IMetadata metadata = retrievedELO.getMetadata();
		IMetadataKey keywordKey = this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS.getId());
		IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(keywordKey);
		List<KeyValuePair> keywords = (List<KeyValuePair>) metadataValueContainer.getValueList();
		assertEquals(13, keywords.size());

		assertTrue(this.hasKeywords(keywords, "transfers", "atmosphere", "roughly", "stored", "finite", "human",
				"fossil fuels", "balance", "carbon", "fossil", "fuels", "release", "decay"));

		this.addMetadataAgent.processELOSavedAction(ActionConstants.ACTION_ELO_SAVED, UUID1234, TIME_IN_MILLIS,
				"webresourcer", MISSION1, "TestSession", this.webResourceEloPath, EloTypes.SCY_WEBRESOURCER);
		retrievedELO = this.repository.retrieveELOLastVersion(this.webResourceEloUri);
		metadata = retrievedELO.getMetadata();
		keywordKey = this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS.getId());
		metadataValueContainer = metadata.getMetadataValueContainer(keywordKey);
		keywords = (List<KeyValuePair>) metadataValueContainer.getValueList();
		assertEquals(15, keywords.size());
		assertTrue(this.hasKeywords(keywords, "ecological", "development", "expressed", "carbon footprint", "strategy",
				"ecological footprint", "footprint", "private", "organization", "sneaked", "carbon", "capture",
				"undertaking", "known", "assessment"));

	}
}
