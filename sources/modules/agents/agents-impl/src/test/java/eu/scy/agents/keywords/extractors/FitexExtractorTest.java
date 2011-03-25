/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class FitexExtractorTest extends AbstractExtractorTest {

	@Before
	public void setup() throws Exception {
		loadElo("/SCYDataExample1.xml", "TestFitex", "scy/pds");
		extractor = new FitexExtractor();
		extractor.setMission(MISSION1);
		extractor.setTupleSpace(getCommandSpace());
	}

	@Test
	public void testGetKeywords() {
		List<String> keywords = extractor.getKeywords(elo);
		assertEquals(3, keywords.size());
		assertTrue(hasItems(keywords, "time", "illumination", "temperature"));
	}

}
