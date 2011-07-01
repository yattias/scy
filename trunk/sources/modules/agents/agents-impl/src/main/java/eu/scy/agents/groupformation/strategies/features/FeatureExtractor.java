package eu.scy.agents.groupformation.strategies.features;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.Map;
import java.util.Set;

import roolo.api.IRepository;
import roolo.elo.api.IELO;

public interface FeatureExtractor {

	public void setCommandSpace(TupleSpace commandSpace);
	
	public void setRepository(IRepository repository);

	public TupleSpace getCommandSpace();

	public boolean canRun(IELO elo);

	public Map<String, double[]> getFeatures(Set<String> availableUsers,
			String mission, IELO elo);
}
