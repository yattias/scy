package eu.scy.agents.groupformation.strategies;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.agents.groupformation.cache.GroupCache;

public class ClusterStrategyTest {

	private AbstractGroupFormationStrategy strategy;

	@Before
	public void setup() {
		strategy = new ClusterStrategy();
		Set<String> users = new HashSet<String>();
		users.add("TestUser1");
		users.add("TestUser2");
		users.add("TestUser3");
		users.add("TestUser4");
		users.add("TestUser5");
		users.add("TestUser6");
		strategy = new DummyStrategy();
		strategy.setAvailableUsers(users);
		strategy.setMinimumGroupSize(2);
		strategy.setMaximumGroupSize(3);
		GroupCache groupFormationCache = new GroupCache();
		strategy.setGroupFormationCache(groupFormationCache);
	}

	@Test
	@Ignore("needs to be reimplemented")
	public void testFormGroup() {

	}

}
