package eu.scy.agents.groupformation.strategies.features;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;

import eu.scy.agents.groupformation.strategies.algorithms.Cluster;
import eu.scy.agents.groupformation.strategies.algorithms.FeatureVector;
import eu.scy.agents.groupformation.strategies.algorithms.KMeansAlgorithm;

public class CMapClusteringTest extends AbstractFeatureExtractorTest {

	private int numberOfClusters = 3;
	private KMeansAlgorithm clusterAlgorithm;

	protected IELO elo1, elo2, elo3, elo4, elo5, elo6, elo7;
	protected ArrayList<IELO> eloList;

	@Before
	public void setup() throws Exception {

		this.clusterAlgorithm = new KMeansAlgorithm(this.numberOfClusters);
		this.extractor = new CMapFeatureExtractor();
		this.referenceElo = this.loadElo("/eco_reference_concept_map.xml", "TestInterview", "scy/interview");
		this.elo1 = this.loadElo("/ecoExpertMaps/expertMap1.xml", "TestInterview", "scy/interview");
		this.elo2 = this.loadElo("/ecoExpertMaps/expertMap1b.xml", "TestInterview", "scy/interview");
		this.elo3 = this.loadElo("/ecoExpertMaps/expertMap2.xml", "TestInterview", "scy/interview");
		this.elo4 = this.loadElo("/ecoExpertMaps/expertMap3.xml", "TestInterview", "scy/interview");
		this.elo5 = this.loadElo("/ecoSimpleMaps/simpleMap1.xml", "TestInterview", "scy/interview");
		this.elo6 = this.loadElo("/ecoSimpleMaps/simpleMap2.xml", "TestInterview", "scy/interview");
		this.elo7 = this.loadElo("/ecoSimpleMaps/simpleMap3.xml", "TestInterview", "scy/interview");

		this.eloList = new ArrayList<IELO>();
		this.eloList.add(this.elo1);
		this.eloList.add(this.elo2);
		this.eloList.add(this.elo3);
		this.eloList.add(this.elo4);
		this.eloList.add(this.elo5);
		this.eloList.add(this.elo6);
		this.eloList.add(this.elo7);
	}

	@Test
	public void testGetKeywords() {
		FeatureVector[] featureVectors = new FeatureVector[this.eloList.size()];
		double[] features = null;
		int userNo = 0;
		String user = "";
		for (Iterator eIt = this.eloList.iterator(); eIt.hasNext();) {
			IELO e = (IELO) eIt.next();
			user = "user" + userNo + 1;
			features = ((CMapFeatureExtractor) this.extractor).getCMapFeatures(user, "mission2", this.referenceElo, e);
			featureVectors[userNo] = new FeatureVector(user, features);
			userNo++;
		}

		this.clusterAlgorithm.setData(featureVectors);
		List<Cluster> clusters = this.clusterAlgorithm.run();
		assertEquals(4, features.length);
	}
}
