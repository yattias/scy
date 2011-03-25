/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RichTextExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/richTextExampleElo.xml", "TestCopex", "scy/copex");
		extractor = new RichTextExtractor();
		extractor.setMission(MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(10, keywords.size());
		assertTrue(hasItems(keywords, "par", "plus", "test", "triple", "toute",
				"pour", "influence", "part", "double", "comment", "un"));
	}
}
