package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.strategies.algorithms.Cluster;
import eu.scy.agents.groupformation.strategies.algorithms.FeatureVector;
import eu.scy.agents.groupformation.strategies.algorithms.KMeansAlgorithm;
import eu.scy.agents.groupformation.strategies.features.FeatureExtractor;
import roolo.elo.api.IELO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A group formation strategy that combines several group formation strategies in a sequence. Only possible group member that are found in
 * predefined number of strategies are included into the final group.
 *
 * @author Florian Schulz
 */
public class ClusterStrategy extends AbstractGroupFormationStrategy {

    private List<FeatureExtractor> extractors;

    private KMeansAlgorithm clusterAlgorithm;

    public ClusterStrategy() {
        extractors = new ArrayList<FeatureExtractor>();
    }

    @Override
    public Collection<Group> formGroup(IELO elo) {
        int numberOfClusters = getNumberOfGroups(getAvailableUsers().size());

        clusterAlgorithm = new KMeansAlgorithm(numberOfClusters);

        Map<String, FeatureVector> featureVectors = buildFeatureVectors(
                getAvailableUsers(), elo);

        List<Cluster> clusters = runClusterAlgorithm(featureVectors);

        List<Group> groups = transformClusterToGroups(clusters);

        return groups;
    }

    @Override
    public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo) {
        Set<String> tmpUserSet = new HashSet<String>();
        tmpUserSet.add(newUser);
        Map<String, FeatureVector> featureVectors = buildFeatureVectors(tmpUserSet, referenceElo);
        FeatureVector features = featureVectors.get(newUser);

        Collection<Group> groups = cache.getGroups();
        double minDistance = Double.MAX_VALUE;
        Group minDistanceGroup = null;
        for ( Group group : groups ) {
            double[] center = (double[]) group.getData();
            double distance = clusterAlgorithm.getDistanceMeasure().distance(center, features.getVector());
            if ( distance >= minDistance ) {
                minDistance = distance;
                minDistanceGroup = group;
            }
        }
        minDistanceGroup.add(newUser);
        return cache.getGroups();
    }

    private List<Group> transformClusterToGroups(List<Cluster> clusters) {
        List<Group> groups = new ArrayList<Group>();
        for ( Cluster cluster : clusters ) {
            Group group = new Group();
            for ( FeatureVector featureVector : cluster.getMembers() ) {
                group.add(featureVector.getId());
            }
            group.setData(cluster.getCenter());
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
            Set<String> availableUsers, IELO elo) {
        Map<String, FeatureVector> featureVectors = initFeatureVectors(availableUsers);
        fillFeatureVectors(availableUsers, featureVectors, elo);
        return featureVectors;
    }

    private void fillFeatureVectors(Set<String> availableUsers,
                                    Map<String, FeatureVector> featureVectors, IELO elo) {
        for ( FeatureExtractor extractor : extractors ) {
            extractor.setRepository(getRepository());
            if ( extractor.canRun(elo) ) {
                Map<String, double[]> features = extractor.getFeatures(
                        availableUsers, getMission(), elo);
                for ( String user : features.keySet() ) {
                    FeatureVector featureVector = featureVectors.get(user);
                    featureVector.add(features.get(user));
                }
            }
        }
    }

    private Map<String, FeatureVector> initFeatureVectors(
            Set<String> availableUsers) {
        Map<String, FeatureVector> featureVectors = new HashMap<String, FeatureVector>();
        for ( String user : availableUsers ) {
            featureVectors.put(user, new FeatureVector(user));
        }
        return featureVectors;
    }

    public void addFeatureExtractor(FeatureExtractor featureExtractor) {
        extractors.add(featureExtractor);
    }

    public void removeFeatureExtractor(int index) {
        extractors.remove(index);
    }

    @Override
    public GroupFormationStrategy makeNewEmptyInstance() {
        return new ClusterStrategy();
    }

}
