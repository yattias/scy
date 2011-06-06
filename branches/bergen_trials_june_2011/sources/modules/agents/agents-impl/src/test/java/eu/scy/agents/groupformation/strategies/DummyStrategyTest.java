package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.groupformation.cache.GroupCache;

public class DummyStrategyTest {

	private DummyStrategy strategy;
	private GroupCache groupFormationCache;

	@Before
	public void setup() {
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
		groupFormationCache = new GroupCache();
		strategy.setGroupFormationCache(groupFormationCache);
	}

	@Test
	public void testFormGroup() {
		Collection<Set<String>> groups = strategy.formGroup(null);
		assertEquals(2, groups.size());
		Iterator<Set<String>> iter = groups.iterator();
		Set<String> group1 = iter.next();
		assertEquals(3, group1.size());
		assertTrue(group1.contains("TestUser2"));
		assertTrue(group1.contains("TestUser4"));
		assertTrue(group1.contains("TestUser6"));
		Set<String> group2 = iter.next();
		assertEquals(3, group2.size());
		assertTrue(group2.contains("TestUser1"));
		assertTrue(group2.contains("TestUser3"));
		assertTrue(group2.contains("TestUser5"));
	}
}
