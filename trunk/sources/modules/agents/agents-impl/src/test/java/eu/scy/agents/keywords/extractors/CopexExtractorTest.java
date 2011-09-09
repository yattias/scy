/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.Mission;

public class CopexExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		elo = loadElo("/copexExampleElo.xml", "TestCopex", "scy/copex");
		extractor = new CopexExtractor();
		extractor.setMission(Mission.MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(13, keywords.size());
		assertTrue(hasItems(keywords, "transfers", "atmosphere",
				"roughly", "stored", "finite", "human", "fossil fuels",
				"balance", "carbon", "fossil", "fuels", "release", "decay"));
	}

}
