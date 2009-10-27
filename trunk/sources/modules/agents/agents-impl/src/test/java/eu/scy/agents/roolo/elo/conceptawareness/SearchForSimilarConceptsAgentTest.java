package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.roolo.elo.helper.ELOFiller;

public class SearchForSimilarConceptsAgentTest extends AbstractTestFixture {

	private IELO elo;

	@BeforeClass
	public static void startServer() {
		//startTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		elo = createNewElo("TestELO", "scy/scymapping");

		ELOFiller fillerELO1 = new ELOFiller(elo, typeManager);
		fillerELO1.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
				new Contribute("user1", 3L));
		fillerELO1.fillListValue("nodeLabel",
				ConceptMapAgentsTestFixture.elo1NodeLabelList);
		fillerELO1.fillListValue("linkLabel",
				ConceptMapAgentsTestFixture.elo1LinkLabelList);

		IMetadata metadata = repository.addNewELO(elo);
		elo.setMetadata(metadata);

		IELO conceptMap1ELO = createNewElo("testELO2", "scy/scymapping");
		ELOFiller fillerELO2 = new ELOFiller(conceptMap1ELO, typeManager);
		fillerELO2.fillListValue("nodeLabel",
				ConceptMapAgentsTestFixture.elo2NodeLabelList);
		fillerELO2.fillListValue("linkLabel",
				ConceptMapAgentsTestFixture.elo2LinkLabelList);
		fillerELO2.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
				new Contribute("user2", 3L));
		conceptMap1ELO.setContent(new BasicContent(
				ConceptMapAgentsTestFixture.CONCEPT_MAP1));
		repository.addNewELO(conceptMap1ELO);

		IELO conceptMap2ELO = createNewElo("testELO3", "scy/scymapping");
		ELOFiller fillerELO3 = new ELOFiller(conceptMap2ELO, typeManager);
		fillerELO3.fillListValue("nodeLabel",
				ConceptMapAgentsTestFixture.elo3NodeLabelList);
		fillerELO3.fillListValue("linkLabel",
				ConceptMapAgentsTestFixture.elo3LinkLabelList);
		fillerELO3.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
				new Contribute("user3", 3L));
		conceptMap2ELO.setContent(new BasicContent(
				ConceptMapAgentsTestFixture.CONCEPT_MAP2));
		repository.addNewELO(conceptMap2ELO);

		String agentId = new VMID().toString();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", agentId);
		params.put("tsHost", TSHOST);
		params.put("tsPort", TSPORT);
		agentMap
				.put(
						"eu.scy.agents.roolo.elo.conceptawareness.SearchForSimilarConceptsAgent",
						params);
		startAgentFramework(agentMap);
	}

	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
	}

	@AfterClass
	public static void afterAll() throws Exception {
		//stopTupleSpaceServer();
		// TestHelper.deleteDirectory(eloStoreDir.getParentFile());
	}

	@Test
	public void testRun() throws TupleSpaceException {
		Tuple triggerTuple = new Tuple("scymapper", System.currentTimeMillis(),
				elo.getUri().toString());
		getTupleSpace().write(triggerTuple);
		Tuple resultTuple = getTupleSpace().waitToTake(
				new Tuple("searchSimilarElosAgent", String.class, String.class,
						String.class), 2 * 1000);
		assertNotNull(resultTuple);
		assertEquals(elo.getUri().toString(), resultTuple.getField(3)
				.getValue());
		assertEquals("user1", resultTuple.getField(2).getValue());
		assertEquals("user2;user3;", resultTuple.getField(1).getValue());
	}

	@Test
	public void testNullELORun() throws TupleSpaceException {
		Tuple triggerTuple = new Tuple("scymapper", System.currentTimeMillis(),
				"http://something.thatis.not/a/urlpointing_to_something");
		System.err.println("Writing tuple");
		getTupleSpace().write(triggerTuple);
		System.err.println("Waiting for tuple");
		Tuple resultTuple = getTupleSpace().waitToTake(
				new Tuple("searchSimilarElosAgent", String.class, String.class,
						String.class), 2 * 1000);
		System.err.println("Got tuple");
		assertNull(resultTuple);
	}

}
