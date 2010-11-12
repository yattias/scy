package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.ArrayList;
import java.util.List;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;

/**
 * TODO implement
 * @author fschulz
 *
 */
public class SimilarELOsStrategy implements GroupFormationStrategy {

	@Override
	public List<String> formGroup(IELO elo, String mission, String user) {
		List<String> group = new ArrayList<String>();
		return group;
	}

	@Override
	public TupleSpace getCommandSpace() {
		return null;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
	}

}
