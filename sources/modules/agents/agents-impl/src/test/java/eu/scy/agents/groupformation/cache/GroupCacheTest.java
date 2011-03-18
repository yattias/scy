package eu.scy.agents.groupformation.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.groupformation.cache.GroupCache;

public class GroupCacheTest {

	private GroupCache cache;

	@Before
	public void setup() {
		cache = new GroupCache();
	}

	@Test
	public void addGroups() {
		Set<String> group1 = new HashSet<String>();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		Set<String> group2 = new HashSet<String>();
		group2.add("G2Test1");
		group2.add("G2Test2");
		group2.add("G2Test3");
		cache.addGroup(group2);

		assertSame(group2, cache.getGroup("G2Test1"));
		assertSame(group2, cache.getGroup("G2Test2"));
		assertSame(group2, cache.getGroup("G2Test3"));
		assertSame(group1, cache.getGroup("G1Test1"));
		assertSame(group1, cache.getGroup("G1Test2"));
		assertSame(group1, cache.getGroup("G1Test3"));

		assertNotSame(group2, cache.getGroup("G1Test1"));
	}

	@Test
	public void getGroups() {
		Set<String> group1 = new HashSet<String>();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		Set<String> group2 = new HashSet<String>();
		group2.add("G2Test1");
		group2.add("G2Test2");
		group2.add("G2Test3");
		cache.addGroup(group2);

		Collection<Set<String>> groups = cache.getGroups();
		assertEquals(2, groups.size());
		Iterator<Set<String>> iterator = groups.iterator();
		assertSame(group1, iterator.next());
		assertSame(group2, iterator.next());
		assertFalse(iterator.hasNext());
	}
}
