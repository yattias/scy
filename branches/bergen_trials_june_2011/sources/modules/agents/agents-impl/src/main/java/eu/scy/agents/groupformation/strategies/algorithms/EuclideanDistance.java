package eu.scy.agents.groupformation.strategies.algorithms;

public class EuclideanDistance implements DistanceMeasure {

	@Override
	public double distance(double[] vectorA, double[] vectorB) {
		if (vectorA.length != vectorB.length) {
			throw new IllegalArgumentException(
					"vectors are not of the same size");
		}

		double distance = 0.0;

		for (int i = 0; i < vectorA.length; i++) {
			double difference = vectorA[i] - vectorB[i];
			distance += difference * difference;
		}

		return Math.sqrt(distance);
	}

}
