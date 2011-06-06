package eu.scy.agents.groupformation.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.scy.agents.groupformation.GroupFormationStrategy;

import roolo.elo.api.IELO;

public class DummyStrategy extends AbstractGroupFormationStrategy {

	@Override
	public Collection<Set<String>> formGroup(IELO elo) {
		int numberOfGroups = getNumberOfGroups(getAvailableUsers().size());
		List<Set<String>> groups = new ArrayList<Set<String>>(numberOfGroups);
		for (int i = 0; i < numberOfGroups; i++) {
			groups.add(new HashSet<String>());
		}
		int groupCounter = 0;
		for (String user : getAvailableUsers()) {
			if (groupCounter >= numberOfGroups) {
				groupCounter = 0;
			}
			groups.get(groupCounter).add(user);
			groupCounter++;
		}
		return groups;
	}

	@Override
	public GroupFormationStrategy makeNewEmptyInstance() {
		return new DummyStrategy();
	}
}
