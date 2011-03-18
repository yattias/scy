package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.groupformation.cache.GroupCache;

public class SameGroupsStrategyTest {

	private SameGroupsStrategy strategy;
	private Set<String> group1;
	private Set<String> group2;

	@Before
	public void setUp() throws Exception {
		strategy = new SameGroupsStrategy();
		GroupCache cache = new GroupCache();

		group1 = new HashSet<String>();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		group2 = new HashSet<String>();
		group2.add("G2Test1");
		group2.add("G2Test2");
		group2.add("G2Test3");
		cache.addGroup(group2);

		strategy.setGroupFormationCache(cache);
	}

	@Test
	public void testFormGroups() {
		Collection<Set<String>> formGroup = strategy.formGroup(null);
		Iterator<Set<String>> iter = formGroup.iterator();
		assertSame(group1, iter.next());
		assertSame(group2, iter.next());
		assertFalse(iter.hasNext());
	}
}
