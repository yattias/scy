package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;

public class GroupFormationStrategySequenceTest {

	private GroupFormationStrategySequence groupFormationStrategySequence;

	@Before
	public void setup() {
		groupFormationStrategySequence = new GroupFormationStrategySequence();
		groupFormationStrategySequence
				.addStrategy(new GroupFormationStrategy() {

					@Override
					public List<String> formGroup(IELO elo, String user) {
						return Arrays
								.asList("User1", "User2", "User3", "User4");
					}
				});

		groupFormationStrategySequence
				.addStrategy(new GroupFormationStrategy() {

					@Override
					public List<String> formGroup(IELO elo, String user) {
						return Arrays.asList("User2", "User3", "User5");
					}
				});

		groupFormationStrategySequence
				.addStrategy(new GroupFormationStrategy() {

					@Override
					public List<String> formGroup(IELO elo, String user) {
						return Arrays.asList("User2", "User4", "User5");
					}
				});

		groupFormationStrategySequence.addStrategy(new NoGroupStrategy());
	}

	@Test
	public void testFormGroup() {
		List<String> formGroup = groupFormationStrategySequence.formGroup(null,
				"someUser");
		assertEquals(5, formGroup.size());
	}

}
