package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import roolo.elo.api.IELO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DummyStrategy extends AbstractGroupFormationStrategy {

	@Override
	public Collection<Group> formGroup(IELO elo) {
		int numberOfGroups = getNumberOfGroups(getAvailableUsers().size());
		List<Group> groups = new ArrayList<Group>(numberOfGroups);
		for (int i = 0; i < numberOfGroups; i++) {
			groups.add(new Group());
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
    public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo) {
        Collection<Group> groups = cache.getGroups();
        int minGroupSize = Integer.MAX_VALUE;
        Group smallestGroup = null;
        for(Group group : groups) {
            if(minGroupSize >= group.size()) {
                minGroupSize = group.size();
                smallestGroup = group;
            }
        }
        smallestGroup.add(newUser);
        return cache.getGroups();
    }

    @Override
	public GroupFormationStrategy makeNewEmptyInstance() {
		return new DummyStrategy();
	}
}
