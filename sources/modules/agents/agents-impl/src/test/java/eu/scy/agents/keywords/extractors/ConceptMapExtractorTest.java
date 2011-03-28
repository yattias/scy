package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ConceptMapExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/conceptMap1.xml", "TestInterview", "scy/interview");
		extractor = new ConceptMapExtractor();
		extractor.setMission(MISSION1);
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