package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import roolo.elo.api.IELO;
import eu.scy.agents.general.UserLocationAgent;
import eu.scy.agents.groupformation.strategies.algorithms.Cluster;
import eu.scy.agents.groupformation.strategies.algorithms.FeatureVector;
import eu.scy.agents.groupformation.strategies.algorithms.KMeansAlgorithm;
import eu.scy.agents.groupformation.strategies.features.FeatureExtractor;
import eu.scy.agents.impl.AgentProtocol;

/**
 * A group formation strategy that combines several group formation strategies
 * in a sequence. Only possible group member that are found in predefined number
 * of strategies are included into the final group.
 * 
 * @author Florian Schulz
 * 
 */
public class ClusterStrategy extends AbstractGroupFormationStrategy {

	private List<FeatureExtractor> extractors;

	private KMeansAlgorithm clusterAlgorithm;

	public ClusterStrategy() {
		extractors = new ArrayList<FeatureExtractor>();
	}

	@Override
	public List<Set<String>> formGroup(IELO elo) {
		Set<String> availableUsers = getAvailableUsers();

		int numberOfClusters = getNumberOfClusters(availableUsers.size());

		clusterAlgorithm = new KMeansAlgorithm(numberOfClusters);

		Map<String, FeatureVector> featureVectors = buildFeatureVectors(availableUsers);

		List<Cluster> clusters = runClusterAlgorithm(featureVectors);

		List<Set<String>> groups = transformClusterToGroups(clusters);

		return groups;
	}

	private List<Set<String>> transformClusterToGroups(List<Cluster> clusters) {
		List<Set<String>> groups = new ArrayList<Set<String>>();
		for (Cluster cluster : clusters) {
			Set<String> group = new LinkedHashSet<String>();
			for (FeatureVector featureVector : cluster.getMembers()) {
				group.add(featureVector.getId());
			}
			groups.add(group);
		}
		return groups;
	}

	private List<Cluster> runClusterAlgorithm(
			Map<String, FeatureVector> featureVectors) {
		Collection<FeatureVector> values = featureVectors.values();
		clusterAlgorithm.setData(values
				.toArray(new FeatureVector[values.size()]));
		List<Cluster> clusters = clusterAlgorithm.run();
		return clusters;
	}

	private Map<String, FeatureVector> buildFeatureVectors(
			Set<String> availableUsers) {
		Map<String, FeatureVector> featureVectors = new HashMap<String, FeatureVector>();
		for (String user : availableUsers) {
			featureVectors.put(user, new FeatureVector(user,
					new double[extractors.size()]));
		}
		int featureVectorIndex = 0;
		for (FeatureExtractor extractor : extractors) {
			Map<String, Double> features = extractor.getFeatures(
					availableUsers, getMission());
			for (String user : features.keySet()) {
				FeatureVector featureVector = featureVectors.get(user);
				featureVector.set(featureVectorIndex, features.get(user));
			}
			featureVectorIndex++;
		}
		return featureVectors;
	}

	private int getNumberOfClusters(int numberOfUsers) {
		int minimumNumberOfGroups = 0;
		if (numberOfUsers % getMaximumGroupSize() == 0) {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize();
		} else {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize() + 1;
		}

		int maximumNumberOfGroups = 0;
		if (numberOfUsers % getMinimumGroupSize() == 0) {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize();
		} else {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize() + 1;
		}

		return (int) Math
				.floor((maximumNumberOfGroups + minimumNumberOfGroups) / 2.0);
	}

	private Set<String> getAvailableUsers() {
		switch (scope) {
		case LAS:
			return getUsers(UserLocationAgent.METHOD_USERS_IN_LAS);
		case MISSION:
			return getUsers(UserLocationAgent.METHOD_USERS_IN_MISSION);
		}

		return Collections.emptySet();
	}

	private Set<String> getUsers(String method) {
		Tuple request = new Tuple(UserLocationAgent.USER_INFO_REQUEST,
				AgentProtocol.QUERY, new VMID().toString(), mission, method,
				getLas());
		try {
			getCommandSpace().write(request);
			Tuple response = getCommandSpace().waitToTake(
					new Tuple(UserLocationAgent.USER_INFO_REQUEST,
							AgentProtocol.RESPONSE, String.class, Field
									.createWildCardField()),
					AgentProtocol.ALIVE_INTERVAL);
			Set<String> availableUsers = new HashSet<String>();
			for (int i = 3; i < response.getNumberOfFields(); i++) {
				availableUsers.add((String) response.getField(i).getValue());
			}
			return availableUsers;
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();

	}

	public void addFeatureExtractor(FeatureExtractor featureExtractor) {
		extractors.add(featureExtractor);
	}

	public void removeFeatureExtractor(int index) {
		extractors.remove(index);
	}

}
