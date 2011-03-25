/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CopexExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/copexExampleElo.xml", "TestCopex", "scy/copex");
		extractor = new CopexExtractor();
		extractor.setMission(MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(14, keywords.size());
		assertTrue(hasItems(keywords, "transfers", "influence", "atmosphere",
				"roughly", "stored", "finite", "human", "fossil fuels",
				"balance", "carbon", "fossil", "fuels", "release", "decay"));
	}

}
