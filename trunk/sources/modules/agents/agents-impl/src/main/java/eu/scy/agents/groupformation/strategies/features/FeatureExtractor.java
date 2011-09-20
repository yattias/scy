package eu.scy.agents.groupformation.strategies.features;

import eu.scy.common.scyelo.RooloServices;
import info.collide.sqlspaces.client.TupleSpace;
import roolo.elo.api.IELO;

import java.util.Map;
import java.util.Set;

public interface FeatureExtractor {

	public void setCommandSpace(TupleSpace commandSpace);
	
	public void setRepository(RooloServices repository);

	public TupleSpace getCommandSpace();

	public boolean canRun(IELO elo);

	public Map<String, double[]> getFeatures(Set<String> availableUsers,
			String mission, IELO elo);
}
