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

public class WebresourceExtractorTest extends AbstractExtractorTest {

    @Before
    public void setup() throws Exception {
        elo = loadElo("/scyLighterExample.xml", "TestWebresource", "scy/webresource");
        extractor = new WebresourceExtractor();
        extractor.setMission(Mission.MISSION1);
        extractor.setTupleSpace(getCommandSpace());
    }

    @Test
    public void testGetKeywords() {
        List<String> keywords = extractor.getKeywords(elo);
        assertEquals(15, keywords.size());
        assertTrue(hasItems(keywords, "ecological", "development", "expressed",
                "carbon footprint", "strategy", "ecological footprint", "footprint",
                "private", "organization", "sneaked", "carbon", "capture", "undertaking",
                "known", "assessment"));
    }

}
