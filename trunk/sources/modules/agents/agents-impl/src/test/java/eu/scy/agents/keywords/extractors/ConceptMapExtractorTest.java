package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.Mission;

public class ConceptMapExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		extractor = new ConceptMapExtractor();
      loadElo("/conceptMap1.xml", "TestInterview", "scy/interview");
      extractor.setMission(Mission.MISSION1);
//        loadElo("/eco_reference_concept_map.xml", "TestInterview", "scy/interview");
//        extractor.setMission(Mission.MISSION2);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(16, keywords.size());
		assertTrue(hasItems(keywords, "wind", "reduces", "inhabitants",
				"insulation", "effects", "number", "space", "sun",
				"temperature", "size", "roof", "inside", "heating", "energy",
				"wall", "outside"));
	}

}
