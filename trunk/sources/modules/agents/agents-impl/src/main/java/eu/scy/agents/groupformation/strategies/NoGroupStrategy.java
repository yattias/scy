package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import roolo.elo.api.IELO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NoGroupStrategy extends AbstractGroupFormationStrategy {

	@Override
	public List<Group> formGroup(IELO elo) {
		return Collections.emptyList();
	}

    @Override
    public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo) {
        return Collections.emptyList();
    }

    @Override
	public GroupFormationStrategy makeNewEmptyInstance() {
		return this;
	}
}
