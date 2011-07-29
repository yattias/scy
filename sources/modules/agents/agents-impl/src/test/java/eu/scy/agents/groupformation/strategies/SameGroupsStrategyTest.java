package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.GroupCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class SameGroupsStrategyTest {

	private SameGroupsStrategy strategy;
	private Group group1;
	private Group group2;

	@Before
	public void setUp() throws Exception {
		strategy = new SameGroupsStrategy();
		GroupCache cache = new GroupCache();

		group1 = new Group();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		group2 = new Group();
		group2.add("G2Test1");
		group2.add("G2Test2");
		group2.add("G2Test3");
		cache.addGroup(group2);

		strategy.setGroupFormationCache(cache);
	}

	@Test
	public void testFormGroups() {
		Collection<Group> formGroup = strategy.formGroup(null);
		Iterator<Group> iter = formGroup.iterator();
		assertSame(group1, iter.next());
		assertSame(group2, iter.next());
		assertFalse(iter.hasNext());
	}
}
