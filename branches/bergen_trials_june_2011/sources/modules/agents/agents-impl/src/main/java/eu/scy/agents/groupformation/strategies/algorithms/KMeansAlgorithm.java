package eu.scy.agents.groupformation.strategies.algorithms;

import java.util.List;
import java.util.Random;

import edu.emory.mathcs.backport.java.util.Arrays;

public class KMeansAlgorithm {

	private DistanceMeasure distance;

	private Cluster[] clusters;

	private FeatureVector[] data;

	private Random rand;

	public KMeansAlgorithm(int numberOfClusters) {
		clusters = new Cluster[numberOfClusters];
		distance = new EuclideanDistance();
		rand = new Random();
	}

	public Cluster[] getCluster() {
		return clusters;
	}

	public int getNumberOfClusters() {
		return clusters.length;
	}

	public void setNumberOfClusters(int numberOfClusters) {
		clusters = new Cluster[numberOfClusters];
	}

	public void setData(FeatureVector[] featureVectors) {
		this.data = new FeatureVector[featureVectors.length];
		for (int i = 0; i < featureVectors.length; i++) {
			this.data[i] = featureVectors[i];
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cluster> run() {
		init();
		int iterations = 0;
		while (!terminated()) {
			recalculateCentersAndClearClusters();

			for (FeatureVector featureVector : data) {
				double minimumDistance = Double.MAX_VALUE;

				int clusterIndex = -1;

				for (int cluster = 0; cluster < clusters.length; cluster++) {
					double distance = this.distance.distance(featureVector
							.getVector(), clusters[cluster].getCenter());
					if (distance < minimumDistance) {
						minimumDistance = distance;
						clusterIndex = cluster;
					}
				}
				clusters[clusterIndex].addFeatureVector(featureVector);
			}

			iterations++;
		}
		return Arrays.asList(clusters);
	}

	private boolean terminated() {
		boolean hasOneCenterChanged = false;
		for (Cluster cluster : clusters) {
			hasOneCenterChanged |= cluster.centerChanged();
		}
		return hasOneCenterChanged;
	}

	private void recalculateCentersAndClearClusters() {
		for (Cluster cluster : clusters) {
			cluster.calculateCenter();
			cluster.clear();
		}
	}

	protected void init() {
		boolean[] alreadyPicked = new boolean[data.length];
		for (int i = 0; i < clusters.length; i++) {
			int center = rand.nextInt(data.length);
			while (alreadyPicked[center]) {
				center = rand.nextInt(data.length);
			}
			clusters[i] = new Cluster();
			clusters[i].addFeatureVector(data[center]);
			alreadyPicked[center] = true;
		}
	}

	public DistanceMeasure getDistanceMeasure() {
		return distance;
	}

	public void setDistanceMeasure(DistanceMeasure distance) {
		this.distance = distance;
	}

	public Random getRandom() {
		return rand;
	}

	public void setRandom(Random rand) {
		this.rand = rand;
	}

}
