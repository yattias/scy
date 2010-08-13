package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;

public class ConceptMapExtractorTest extends AbstractTestFixture {

	public static final String CONCEPT_MAP = "<conceptmap>\n"
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
	private IELO elo;
	private ConceptMapExtractor conceptMapExtractor;

	@Before
	public void setup() {
		elo = createNewElo("TestConceptMap", "scy/mapping");
		elo.setContent(new BasicContent(CONCEPT_MAP));
		conceptMapExtractor = new ConceptMapExtractor();
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = conceptMapExtractor.getKeywords(elo);
		assertEquals(4, keywords.size());
		assertTrue(hasItems(keywords, "total moment", "moment child 1",
				"moment child 2", "moment"));
	}

	private boolean hasItems(List<String> keywords, String... values) {
		for (String value : values) {
			if (!keywords.contains(value)) {
				return false;
			}
		}
		return true;
	}

}
