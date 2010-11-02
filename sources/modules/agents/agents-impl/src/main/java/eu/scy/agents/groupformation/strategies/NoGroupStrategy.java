package eu.scy.agents.groupformation.strategies;

import java.util.Collections;
import java.util.List;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;

public class NoGroupStrategy implements GroupFormationStrategy {

	@Override
	public List<String> formGroup(IELO elo, String user) {
		return Collections.emptyList();
	}

}
