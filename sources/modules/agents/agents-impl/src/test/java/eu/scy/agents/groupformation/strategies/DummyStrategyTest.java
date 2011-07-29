package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.GroupCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        Group group1 = new Group();
        group1.add("TestUser1");
        group1.add("TestUser3");
        group1.add("TestUser5");
        Group group2 = new Group();
        group2.add("TestUser2");
        group2.add("TestUser4");
        group2.add("TestUser6");
        groupFormationCache.addGroup(group1);
        groupFormationCache.addGroup(group2);
        strategy.setGroupFormationCache(groupFormationCache);
    }

    @Test
    public void testFormGroup() {
        Collection<Group> groups = strategy.formGroup(null);
        assertEquals(2, groups.size());
        Iterator<Group> iter = groups.iterator();
        Group group1 = iter.next();
        assertEquals(3, group1.size());
        assertTrue(group1.contains("TestUser2"));
        assertTrue(group1.contains("TestUser4"));
        assertTrue(group1.contains("TestUser6"));
        Group group2 = iter.next();
        assertEquals(3, group2.size());
        assertTrue(group2.contains("TestUser1"));
        assertTrue(group2.contains("TestUser3"));
        assertTrue(group2.contains("TestUser5"));
    }

    @Test
    public void testAssignToExistingGroups() {
        Collection<Group> groups = strategy.assignToExistingGroups("TestUser7", null);
        assertEquals(2, groups.size());
        Iterator<Group> iter = groups.iterator();
        Group group1 = iter.next();
        assertEquals(3, group1.size());
        assertTrue(group1.contains("TestUser1"));
        assertTrue(group1.contains("TestUser3"));
        assertTrue(group1.contains("TestUser5"));
        Group group2 = iter.next();
        assertEquals(4, group2.size());
        assertTrue(group2.contains("TestUser2"));
        assertTrue(group2.contains("TestUser4"));
        assertTrue(group2.contains("TestUser6"));
        assertTrue(group2.contains("TestUser7"));
    }
}
