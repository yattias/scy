package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.Collections;
import java.util.List;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;

public class NoGroupStrategy implements GroupFormationStrategy {

	@Override
	public List<String> formGroup(IELO elo, String mission, String user) {
		return Collections.emptyList();
	}

	@Override
	public TupleSpace getCommandSpace() {
		return null;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
	}

}
