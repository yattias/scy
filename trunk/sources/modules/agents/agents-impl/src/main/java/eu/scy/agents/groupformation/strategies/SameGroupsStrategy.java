package eu.scy.agents.groupformation.strategies;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;

import roolo.elo.api.IELO;

public class SameGroupsStrategy extends AbstractGroupFormationStrategy {

	private static final Logger LOGGER = Logger
			.getLogger(SameGroupsStrategy.class);

	@Override
	public Collection<Set<String>> formGroup(IELO elo) {
		if (cache == null) {
			LOGGER
					.info("No previous groups formed. Cannot return same groups as before");
			return Collections.emptyList();
		}
		return cache.getGroups();
	}

}
