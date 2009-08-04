package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

import java.io.File;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.cms.repository.mock.MockRepository;
import roolo.elo.BasicELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.roolo.elo.helper.ELOFiller;

public class SearchForSimilarConceptsAgentTest extends
		ConceptMapAgentsTestFixture {

	private static final String CONCEPT_MAP1 = "<conceptmap>\n"
			+ "  <nodes>\n"
			+ "    <node id=\"25507275\" name=\"total moment\" xpos=\"443\" ypos=\"509\"/>\n"
			+ "    <node id=\"21773414\" name=\"moment child 1\" xpos=\"261\" ypos=\"309\"/>\n"
			+ "    <node id=\"21214799\" name=\"moment child 2\" xpos=\"621\" ypos=\"306\"/>\n"
			+ "    <node id=\"837138\" name=\"moment\" xpos=\"415\" ypos=\"83\"/>\n"
			+ "    <node id=\"25507275\" name=\"total moment\" xpos=\"443\" ypos=\"509\"/>\n"
			+ "    <node id=\"21214799\" name=\"moment child 2\" xpos=\"621\" ypos=\"306\"/>\n"
			+ "  </nodes>\n"
			+ "  <links>\n"
			+ "    <link from=\"21214799\" id=\"ID-1238181943959\" label=\"is a\" to=\"837138\"/>\n"
			+ "    <link from=\"21214799\" id=\"ID-1238181998609\" label=\"+\" to=\"25507275\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238182022119\" label=\"equals\" to=\"21214799\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238181986379\" label=\"+\" to=\"25507275\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238181922662\" label=\"is a\" to=\"837138\"/>\n"
			+ "  </links>\n" + "</conceptmap>\n";

	private static final String CONCEPT_MAP2 = "<conceptmap>\n"
			+ "<nodes>\n"
			+ "<node id=\"2698971\" name=\"force\" xpos=\"35\" ypos=\"129\"/>\n"
			+ "<node id=\"24575663\" name=\"mass\" xpos=\"275\" ypos=\"131\"/>\n"
			+ "<node id=\"31452704\" name=\"moment\" xpos=\"32\" ypos=\"5\"/>\n"
			+ "<node id=\"14074896\" name=\"distance\" xpos=\"271\" ypos=\"45\"/>\n"
			+ "<node id=\"24575663\" name=\"mass\" xpos=\"275\" ypos=\"131\"/>\n"
			+ "<node id=\"2698971\" name=\"force\" xpos=\"35\" ypos=\"129\"/>\n"
			+ "<node id=\"14074896\" name=\"distance\" xpos=\"271\" ypos=\"45\"/>\n"
			+ "<node id=\"31452704\" name=\"moment\" xpos=\"32\" ypos=\"5\"/>\n"
			+ "</nodes>\n"
			+ "<links>\n"
			+ "<link from=\"14074896\" id=\"ID-1238182344922\" label=\"+\" to=\"2698971\"/>\n"
			+ "<link from=\"2698971\" id=\"ID-1238182364029\" label=\"+\" to=\"31452704\"/>\n"
			+ "<link from=\"14074896\" id=\"ID-1238182384867\" label=\"+\" to=\"31452704\"/>\n"
			+ "<link from=\"24575663\" id=\"ID-1238182326498\" label=\"+\" to=\"2698971\"/>\n"
			+ "</links>\n" + "</conceptmap>\n";

	private List<String> elo2NodeLabelList = Arrays.asList(new String[] {
			"total moment", "moment child 1", "moment child 2", "moment", });
	private List<String> elo2LinkLabelList = Arrays.asList(new String[] {
			"is_a", "+", "equals" });
	private List<String> elo3NodeLabelList = Arrays.asList(new String[] {
			"force", "mass", "moment", "distance" });
	private List<String> elo3LinkLabelList = Arrays
			.asList(new String[] { "+" });

	private MockRepository repo;
	private SearchForSimilarConceptsAgent agent;

	private URI eloUri;

	private File eloStoreDir;

	@Override
	@Before
	public void setUp() throws Exception {
		// super.setUp();
		// if (!Server.isRunning()) {
		// Configuration.getConfiguration().setSSLEnabled(false);
		// Server.startServer();
		// }
		//
		// repo = new MockRepository();
		// repo.setMetadataTypeManager(typeManager);
		// repo.setExtensionManager(extensionManager);
		// eloStoreDir = new File("/tmp/test/eloStore");
		// eloStoreDir.mkdirs();
		// // repo.setEloStoreDirectory(eloStoreDir);
		//
		// ELOFiller fillerELO1 = new ELOFiller(elo, typeManager);
		// fillerELO1.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
		// new Contribute("user1", 3L));
		// fillerELO1.fillListValue("nodeLabel", elo1NodeLabelList);
		// fillerELO1.fillListValue("linkLabel", elo1LinkLabelList);
		//
		// repo.addNewELO(elo);
		// eloUri = elo.getUri();// (URI)
		// // elo.getMetadata().getMetadataValueContainer(
		// // typeManager.getMetadataKey("identifier")).getValue();
		//
		// BasicELO conceptMap1ELO = new BasicELO();
		// conceptMap1ELO.setIdentifierKey(typeManager
		// .getMetadataKey("identifier"));
		// ELOFiller fillerELO2 = new ELOFiller(conceptMap1ELO, typeManager);
		// fillerELO2.fillValue("technicalFormat", "scy/scymapping");
		// fillerELO2.fillValue("title", "testELO2");
		// fillerELO2.fillListValue("nodeLabel", elo2NodeLabelList);
		// fillerELO2.fillListValue("linkLabel", elo2LinkLabelList);
		// fillerELO2.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
		// new Contribute("user2", 3L));
		// conceptMap1ELO.setContent(new BasicContent(CONCEPT_MAP1));
		// repo.addNewELO(conceptMap1ELO);
		//
		// BasicELO conceptMap2ELO = new BasicELO();
		// conceptMap2ELO.setIdentifierKey(typeManager
		// .getMetadataKey("identifier"));
		// ELOFiller fillerELO3 = new ELOFiller(conceptMap2ELO, typeManager);
		// fillerELO3.fillValue("technicalFormat", "scy/scymapping");
		// fillerELO3.fillValue("title", "testELO3");
		// fillerELO3.fillListValue("nodeLabel", elo3NodeLabelList);
		// fillerELO3.fillListValue("linkLabel", elo3LinkLabelList);
		// fillerELO3.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(),
		// new Contribute("user3", 3L));
		// conceptMap2ELO.setContent(new BasicContent(CONCEPT_MAP2));
		// repo.addNewELO(conceptMap2ELO);
		// String agentId = new VMID().toString();
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("id", agentId);
		// agent = new SearchForSimilarConceptsAgent(map);
		// agent.setRepository(repo);
		// agent.setMetadataTypeManager(typeManager);
		// agent.start();
	}

	@After
	public void tearDown() throws Exception {
		// Server.stopServer();
		// TestHelper.deleteDirectory(eloStoreDir.getParentFile());
	}

	@Test
	public void testRun() throws TupleSpaceException {
		// TupleSpace ts = new TupleSpace("command");
		// Tuple triggerTuple = new Tuple("scymapper",
		// System.currentTimeMillis(),
		// eloUri.toString());
		// ts.write(triggerTuple);
		// Tuple resultTuple = ts.waitToTake(new Tuple("searchSimilarElosAgent",
		// String.class, String.class, String.class), 1 * 1000);
		// assertNotNull(resultTuple);
		// assertEquals(eloUri.toString(), resultTuple.getField(3).getValue());
		// assertEquals("user1", resultTuple.getField(2).getValue());
		// assertEquals("user2;user3;", resultTuple.getField(1).getValue());
	}

	@Test
	public void testNullELORun() throws TupleSpaceException {
		// TupleSpace ts = new TupleSpace();
		// Tuple triggerTuple = new Tuple("scymapper",
		// System.currentTimeMillis(),
		// "http://something.thatis.not/a/urlpointing_to_something");
		// ts.write(triggerTuple);
		// Tuple resultTuple = ts.waitToTake(new Tuple("searchSimilarElosAgent",
		// String.class, String.class, String.class), 2 * 1000);
		// assertNull(resultTuple);
	}

}
