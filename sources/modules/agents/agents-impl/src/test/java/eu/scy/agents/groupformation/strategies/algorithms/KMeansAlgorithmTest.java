package eu.scy.agents.groupformation.strategies.algorithms;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class KMeansAlgorithmTest extends KMeansAlgorithm {

	public KMeansAlgorithmTest() {
		super(2);
	}

	@Before
	public void setup() {
		FeatureVector[] data = FeatureVector
				.createFeatureVectors(new double[][] { { 1, 0 }, { 0, 1 },
						{ 1, 1 }, { 0, 0 }, { 0.25, 0 } });
		setData(data);
		setRandom(new Random(0));
	}

	@Test
	public void testInit() {
		init();

		Cluster[] clusters = getCluster();
		assertEquals(2, clusters.length);
		assertEquals(1, clusters[0].size());
		assertEquals(1, clusters[1].size());
		for (Cluster cluster : clusters) {
			cluster.calculateCenter();
		}

		assertArrayEquals(new double[] { 1.0, 0.0 }, clusters[0].getCenter(),
				0.000001);
		assertArrayEquals(new double[] { 0.0, 0.0 }, clusters[1].getCenter(),
				0.000001);
	}

	@Test
	public void testRun() {
		List<Cluster> clusters = run();
		assertEquals(2, clusters.size());

		assertEquals(2, clusters.get(0).size());
		assertEquals("0", clusters.get(0).getMembers()[0].getId());
		assertEquals("2", clusters.get(0).getMembers()[1].getId());
		assertEquals(3, clusters.get(1).size());
		assertEquals("1", clusters.get(1).getMembers()[0].getId());
		assertEquals("3", clusters.get(1).getMembers()[1].getId());
		assertEquals("4", clusters.get(1).getMembers()[2].getId());

	}

    @Test
	public void testSameFeatures() {
        FeatureVector[] data = FeatureVector
				.createFeatureVectors(new double[][] { { 1, 0 }, { 1, 0 },
						{ 1, 0 }, { 1, 0 }, { 1, 0 } });
		setData(data);

		List<Cluster> clusters = run();
		assertEquals(2, clusters.size());

		assertEquals(5, clusters.get(0).size());
		assertEquals("0", clusters.get(0).getMembers()[0].getId());
		assertEquals("1", clusters.get(0).getMembers()[1].getId());
		assertEquals("2", clusters.get(0).getMembers()[2].getId());
		assertEquals("3", clusters.get(0).getMembers()[3].getId());
		assertEquals("4", clusters.get(0).getMembers()[4].getId());
        assertEquals(0, clusters.get(1).size());

	}
}
