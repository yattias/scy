package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import org.apache.log4j.Logger;
import roolo.elo.api.IELO;

import java.util.Collection;
import java.util.Collections;

public class SameGroupsStrategy extends AbstractGroupFormationStrategy {

	private static final Logger LOGGER = Logger
			.getLogger(SameGroupsStrategy.class);

	@Override
	public Collection<Group> formGroup(IELO elo) {
		if (cache == null) {
			LOGGER.info("No previous groups formed. Cannot return same groups as before");
			return Collections.emptyList();
		}
		return cache.getGroups();
	}

    @Override
    public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo) {
        return cache.getGroups();
    }

    @Override
	public GroupFormationStrategy makeNewEmptyInstance() {
		return new SameGroupsStrategy();
	}

}
