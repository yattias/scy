package eu.scy.agents.groupformation.strategies.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.Mission;
import eu.scy.agents.keywords.extractors.AbstractExtractorTest;

public class CMapFeatureExtractorTest extends AbstractFeatureExtractorTest {

    @Before
    public void setup() throws Exception {
        extractor = new CMapFeatureExtractor();
        referenceElo = loadElo("/eco_reference_concept_map.xml", "TestInterview", "scy/interview");
        elo = loadElo("/ecoExpertMaps/expertMap1.xml", "TestInterview", "scy/interview");
        // extractor.setMission(Mission.MISSION1);
        // loadElo("/eco_reference_concept_map.xml", "TestInterview", "scy/interview");
        // extractor.setMission(Mission.MISSION2);
        // extractor.setTupleSpace(getCommandSpace());
    }

    @Test
    public void testGetKeywords() {
        double[] features = ((CMapFeatureExtractor) extractor).getCMapFeatures("x", "y",
                                                                               referenceElo, elo);
        assertEquals(16, features.length);
    }
}
