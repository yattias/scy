package eu.scy.agents.groupformation.strategies.algorithms;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClusterTest {

	private static final double EPSILON = 0.00001;
	private Cluster cluster;

	@Before
	public void setup() {
		cluster = new Cluster();
	}

	@Test
	public void testCalculateCenter2D() {
		cluster.addFeatureVector(new FeatureVector("0", new double[] { 1.0, 0.0 }));
		cluster.addFeatureVector(new FeatureVector("1", new double[] { 0.0, 1.0 }));
		cluster.calculateCenter();

		double[] center = cluster.getCenter();
		assertEquals(2, center.length);
		assertEquals(0.5, center[0], EPSILON);
		assertEquals(0.5, center[1], EPSILON);
	}

	@Test
	public void testCalculateCenter3D() {
		cluster.addFeatureVector(new FeatureVector("0",
				new double[] { 1.0, 0.0, 0.0 }));
		cluster.addFeatureVector(new FeatureVector("1",
				new double[] { 0.0, 0.0, 1.0 }));
		cluster.addFeatureVector(new FeatureVector("2",
				new double[] { 0.0, 1.0, 0.0 }));
		cluster.calculateCenter();

		double[] center = cluster.getCenter();
		assertEquals(3, center.length);
		assertEquals(0.3333333333, center[0], EPSILON);
		assertEquals(0.3333333333, center[1], EPSILON);
		assertEquals(0.3333333333, center[2], EPSILON);
	}
}
