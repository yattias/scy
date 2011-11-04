package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.strategies.algorithms.Cluster;
import eu.scy.agents.groupformation.strategies.algorithms.DistanceMeasure;
import eu.scy.agents.groupformation.strategies.algorithms.FeatureVector;
import eu.scy.agents.groupformation.strategies.algorithms.KMeansAlgorithm;
import eu.scy.agents.groupformation.strategies.features.CMapFeatureExtractor;
import eu.scy.agents.groupformation.strategies.features.FeatureExtractor;
import eu.scy.agents.groupformation.strategies.features.HypothesisFeatureExtractor;
import roolo.elo.api.IELO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A group formation strategy that combines several group formation strategies in a sequence. Only possible group member
 * that are found in predefined number of strategies are included into the final group.
 * 
 * @author Florian Schulz
 */
public class ClusterStrategy extends AbstractGroupFormationStrategy {

	private List<FeatureExtractor> extractors;

	private KMeansAlgorithm clusterAlgorithm;

	public ClusterStrategy() {
		this.extractors = new ArrayList<FeatureExtractor>();
		this.extractors.add(new CMapFeatureExtractor());
		this.extractors.add(new HypothesisFeatureExtractor());
	}

	@Override
	public Collection<Group> formGroup(IELO elo) {
		int numberOfClusters = this.getNumberOfGroups(this.getAvailableUsers().size());

		this.clusterAlgorithm = new KMeansAlgorithm(numberOfClusters);

		Map<String, FeatureVector> featureVectors = this.buildFeatureVectors(this.getAvailableUsers(), elo);

		List<Cluster> clusters = this.runClusterAlgorithm(featureVectors);

		List<Group> groups = this.transformClusterToGroups(clusters);

		return groups;
	}

	@Override
	public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo) {
		Set<String> tmpUserSet = new HashSet<String>();
		tmpUserSet.add(newUser);
		Map<String, FeatureVector> featureVectors = this.buildFeatureVectors(tmpUserSet, referenceElo);
		FeatureVector features = featureVectors.get(newUser);

		Collection<Group> groups = this.cache.getGroups();
		double minDistance = Double.MAX_VALUE;
		Group minDistanceGroup = null;
		for (Group group : groups) {
			double[] center = (double[]) group.getData();
			double distance = this.clusterAlgorithm.getDistanceMeasure().distance(center, features.getVector());
			if (distance >= minDistance) {
				minDistance = distance;
				minDistanceGroup = group;
			}
		}
		minDistanceGroup.add(newUser);
		return this.cache.getGroups();
	}

	private List<Group> transformClusterToGroups(List<Cluster> clusters) {
		List<Cluster> clusterToReassign = new ArrayList<Cluster>();
		List<Group> groups = new ArrayList<Group>();
		for (Cluster cluster : clusters) {
			if (cluster.size() < this.minimumGroupSize) {
				clusterToReassign.add(cluster);
				continue;
			}
			Group group = new Group();
			for (FeatureVector featureVector : cluster.getMembers()) {
				group.add(featureVector.getId());
			}
			group.setData(cluster.getCenter());
			groups.add(group);
		}

		DistanceMeasure distanceMeasure = this.clusterAlgorithm.getDistanceMeasure();
		for (Cluster cluster : clusterToReassign) {
			for (FeatureVector featureVector : cluster.getMembers()) {
				double minDistance = Double.MAX_VALUE;
				Group minGroup = null;
				for (Group group : groups) {
					double distance = distanceMeasure.distance((double[]) group.getData(), featureVector.getVector());
					if (distance < minDistance) {
						minGroup = group;
						minDistance = distance;
					}
				}
				minGroup.add(featureVector.getId());
			}
		}
		return groups;
	}

	private List<Cluster> runClusterAlgorithm(Map<String, FeatureVector> featureVectors) {
		Collection<FeatureVector> values = featureVectors.values();
		this.clusterAlgorithm.setData(values.toArray(new FeatureVector[values.size()]));
		List<Cluster> clusters = this.clusterAlgorithm.run();
		return clusters;
	}

	private Map<String, FeatureVector> buildFeatureVectors(Set<String> availableUsers, IELO elo) {
		Map<String, FeatureVector> featureVectors = this.initFeatureVectors(availableUsers);
		this.fillFeatureVectors(availableUsers, featureVectors, elo);
		return featureVectors;
	}

	private void fillFeatureVectors(Set<String> availableUsers, Map<String, FeatureVector> featureVectors, IELO elo) {
		for (FeatureExtractor extractor : this.extractors) {
			extractor.setRepository(this.getRooloServices());
			if (extractor.canRun(elo)) {
				Map<String, double[]> features = extractor.getFeatures(availableUsers, this.getMission(), elo);
				for (String user : features.keySet()) {
					FeatureVector featureVector = featureVectors.get(user);
					featureVector.add(features.get(user));
				}
			}
		}
	}

	private Map<String, FeatureVector> initFeatureVectors(Set<String> availableUsers) {
		Map<String, FeatureVector> featureVectors = new HashMap<String, FeatureVector>();
		for (String user : availableUsers) {
			featureVectors.put(user, new FeatureVector(user));
		}
		return featureVectors;
	}

	public void addFeatureExtractor(FeatureExtractor featureExtractor) {
		this.extractors.add(featureExtractor);
	}

	public void removeFeatureExtractor(int index) {
		this.extractors.remove(index);
	}

	@Override
	public GroupFormationStrategy makeNewEmptyInstance() {
		return new ClusterStrategy();
	}

}
