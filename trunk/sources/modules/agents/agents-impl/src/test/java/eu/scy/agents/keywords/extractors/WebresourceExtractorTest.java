/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WebresourceExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/scyLighterExample.xml", "TestWebresource", "scy/webresource");
		extractor = new WebresourceExtractor();
		extractor.setMission(MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(15, keywords.size());
		assertTrue(hasItems(keywords, "ecological", "expressed",
				"carbon footprint", "strategy", "ecological footprint",
				"measured", "footprint", "private", "organization", "sneaked",
				"carbon", "capture", "undertaking", "known", "assessment"));
	}

}
