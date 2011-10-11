package eu.scy.agents.keywords.extractors;

import eu.scy.agents.Mission;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConceptMapExtractorTest extends AbstractExtractorTest {

    @Before
    public void setup() throws Exception {
        extractor = new ConceptMapExtractor();
        elo = loadElo("/conceptMap1.xml", "TestInterview", "scy/interview");
        extractor.setMission(Mission.MISSION1);
        extractor.setTupleSpace(getCommandSpace());
        extractor.setLanguage("en");
    }

    @Test
    public void testGetKeywords() {
        List<String> keywords = extractor.getKeywords(elo);
        assertEquals(18, keywords.size());
        assertTrue(hasItems(keywords, "wind", "reduces", "inhabitants", "insulation", "number",
                            "effects", "reduces effects", "space", "sun", "temperature", "size",
                            "roof", "inside", "effects reduces", "heating", "energy", "wall",
                            "outside"));
    }

}
