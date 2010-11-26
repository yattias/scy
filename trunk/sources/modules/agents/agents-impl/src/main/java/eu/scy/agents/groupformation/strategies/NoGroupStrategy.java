package eu.scy.agents.groupformation.strategies;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import roolo.elo.api.IELO;

public class NoGroupStrategy extends AbstractGroupFormationStrategy {

	@Override
	public List<Set<String>> formGroup(IELO elo) {
		return Collections.emptyList();
	}

}
