package eu.scy.agents.groupformation.strategies.algorithms;

import static org.junit.Assert.*;

import org.junit.Test;

public class EuclideanDistanceTest {

	@Test
	public void testDistance() {
		EuclideanDistance distance = new EuclideanDistance();
		assertEquals(0, distance.distance(new double[] { 1.0 },
				new double[] { 1.0 }), 0.0000001);

		assertEquals(1.414213562, distance.distance(new double[] { 1.0, 0.0 },
				new double[] { 0.0, 1.0 }), 0.0000001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThrowsIfArgumentsAreNotOfSameSize() {
		EuclideanDistance distance = new EuclideanDistance();
		assertEquals(0, distance.distance(new double[] { 1.0 }, new double[] {
				1.0, 1.0 }), 0.0000001);
	}

}
