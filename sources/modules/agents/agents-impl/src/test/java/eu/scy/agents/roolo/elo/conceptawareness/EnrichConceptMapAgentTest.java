package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.elo.BasicELO;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.content.BasicContent;

public class EnrichConceptMapAgentTest {

	private static final String CONCEPT_MAP_CONTENT = "<conceptmap>\n"
			+ "  <nodes>\n"
			+ "    <node id=\"28208946\" name=\"total moment\" xpos=\"304\" ypos=\"681\"/>\n"
			+ "    <node id=\"6051961\" name=\"moment child 1\" xpos=\"203\" ypos=\"517\"/>\n"
			+ "    <node id=\"13768021\" name=\"force\" xpos=\"298\" ypos=\"184\"/>\n"
			+ "    <node id=\"19971724\" name=\"moment child 2\" xpos=\"362\" ypos=\"516\"/>\n"
			+ "    <node id=\"18791494\" name=\"mass\" xpos=\"296\" ypos=\"24\"/>\n"
			+ "    <node id=\"29873045\" name=\"moment\" xpos=\"299\" ypos=\"358\"/>\n"
			+ "    <node id=\"28208946\" name=\"total moment\" xpos=\"304\" ypos=\"681\"/>\n"
			+ "    <node id=\"13768021\" name=\"force\" xpos=\"298\" ypos=\"184\"/>\n"
			+ "    <node id=\"18791494\" name=\"mass\" xpos=\"296\" ypos=\"24\"/>\n"
			+ "    <node id=\"29873045\" name=\"moment\" xpos=\"299\" ypos=\"358\"/>\n"
			+ "    <node id=\"11136287\" name=\"balance state\" xpos=\"617\" ypos=\"682\"/>\n"
			+ "    <node id=\"19971724\" name=\"moment child 2\" xpos=\"362\" ypos=\"516\"/>\n"
			+ "    <node id=\"6051961\" name=\"moment child 1\" xpos=\"203\" ypos=\"517\"/>\n"
			+ "    <node id=\"11136287\" name=\"balance state\" xpos=\"617\" ypos=\"682\"/>\n"
			+ "    <node id=\"20639637\" name=\"distance\" xpos=\"624\" ypos=\"206\"/>\n"
			+ "    <node id=\"20639637\" name=\"distance\" xpos=\"624\" ypos=\"206\"/>\n"
			+ "  </nodes>\n"
			+ "  <links>\n"
			+ "    <link from=\"19971724\" id=\"ID-1238181132839\" label=\"-\" to=\"28208946\" />\n"
			+ "    <link from=\"20639637\" id=\"ID-1238180980021\" label=\"+\" to=\"29873045\" />\n"
			+ "    <link from=\"19971724\" id=\"ID-1238181084173\" label=\"is a\" to=\"29873045\" />\n"
			+ "    <link from=\"6051961\" id=\"ID-1238181065678\" label=\"is a\" to=\"29873045\" />\n"
			+ "    <link from=\"20639637\" id=\"ID-1238180921348\" label=\"+\" to=\"13768021\" />\n"
			+ "    <link from=\"18791494\" id=\"ID-1238180900951\" label=\"+\" to=\"13768021\" />\n"
			+ "    <link from=\"13768021\" id=\"ID-1238180965087\" label=\"+\" to=\"29873045\" />\n"
			+ "    <link from=\"13768021\" id=\"ID-1238181206589\" label=\"13768021-13768021\" to=\"13768021\" />\n"
			+ "    <link from=\"28208946\" id=\"ID-1238181177527\" label=\"Determines\" to=\"11136287\" />\n"
			+ "    <link from=\"6051961\" id=\"ID-1238181123288\" label=\"+\" to=\"28208946\" />\n"
			+ "  </links>\n" + "</conceptmap>";
	private EnrichConceptMapAgent<IELO<IMetadataKey>, IMetadataKey> agent;
	private IELO<IMetadataKey> elo;
	private IMetadataTypeManager<IMetadataKey> typeManager;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test-config.xml");

		typeManager = (IMetadataTypeManager<IMetadataKey>) applicationContext
				.getBean("metadataTypeManager");

		elo = new BasicELO<IMetadataKey>();
		IMetadataValueContainer typeContainer = elo.getMetadata()
				.getMetadataValueContainer(typeManager.getMetadataKey("type"));
		typeContainer.setValue("scy/scymapping");

		elo.setContent(new BasicContent(CONCEPT_MAP_CONTENT));

		agent = new EnrichConceptMapAgent<IELO<IMetadataKey>, IMetadataKey>();
		agent.setMetadataTypeManager(typeManager);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() {
		agent.processElo(elo);
		IMetadataValueContainer nodeLabelsContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager.getMetadataKey("nodeLabel"));
		List<String> nodeLabels = (List<String>) nodeLabelsContainer
				.getValueList();
		assertEquals(8, nodeLabels.size());
		assertEquals("total moment", nodeLabels.get(0));
		assertEquals("moment child 1", nodeLabels.get(1));
		assertEquals("force", nodeLabels.get(2));
		assertEquals("moment child 2", nodeLabels.get(3));
		assertEquals("mass", nodeLabels.get(4));
		assertEquals("moment", nodeLabels.get(5));
		assertEquals("balance state", nodeLabels.get(6));
		assertEquals("distance", nodeLabels.get(7));

		IMetadataValueContainer linkLabelsContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager.getMetadataKey("linkLabel"));
		List<String> linkLabels = (List<String>) linkLabelsContainer
				.getValueList();
		assertEquals(5, linkLabels.size());
		assertEquals("-", linkLabels.get(0));
		assertEquals("+", linkLabels.get(1));
		assertEquals("is a", linkLabels.get(2));
		assertEquals("13768021-13768021", linkLabels.get(3));
		assertEquals("Determines", linkLabels.get(4));
	}
}
