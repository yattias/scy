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

public class InterviewToolExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/interviewToolExample1.xml", "TestInterview", "scy/interview");
		extractor = new InterviewToolExtractor();
		extractor.setMission(Mission.MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(3, keywords.size());
		assertTrue(hasItems(keywords, "start", "heating", "house"));
	}

}
