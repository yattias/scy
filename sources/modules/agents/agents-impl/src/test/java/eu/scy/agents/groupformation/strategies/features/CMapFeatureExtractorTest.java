package eu.scy.agents.groupformation.strategies.features;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CMapFeatureExtractorTest extends AbstractFeatureExtractorTest {

	@Before
	public void setup() throws Exception {
		this.extractor = new CMapFeatureExtractor();
		this.referenceElo = this.loadElo("/eco_reference_concept_map.scymapper.xml", "TestInterview", "scy/interview");
		this.elo = this.loadElo("/ecoExpertMaps/expertMap1.scymapper.xml", "TestInterview", "scy/interview");
	}

	@Test
	public void testGetFeatures() {
		double[] features = ((CMapFeatureExtractor) this.extractor).getFeatures("x", "y", this.referenceElo,
				this.elo);
		assertEquals(4, features.length);
		assertArrayEquals(new double[] { 37.0, 20.0, 0.0, 19.0 }, features, 0.000001);

	}
}
