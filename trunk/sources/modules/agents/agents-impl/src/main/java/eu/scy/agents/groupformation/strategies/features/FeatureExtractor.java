package eu.scy.agents.groupformation.strategies.features;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.Map;
import java.util.Set;

public interface FeatureExtractor {

	public void setCommandSpace(TupleSpace commandSpace);

	public TupleSpace getCommandSpace();

	public Map<String, double[]> getFeatures(Set<String> availableUsers,
			String mission);
}
