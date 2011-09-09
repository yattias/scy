/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import eu.scy.agents.Mission;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitexExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		elo = loadElo("/SCYDataExample1.xml", "TestFitex", "scy/pds");
		extractor = new FitexExtractor();
		extractor.setMission(Mission.MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(3, keywords.size());
		assertTrue(hasItems(keywords, "time", "illumination", "temperature"));
	}

}
