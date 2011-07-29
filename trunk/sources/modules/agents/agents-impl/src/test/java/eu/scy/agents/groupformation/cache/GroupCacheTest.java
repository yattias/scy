package eu.scy.agents.groupformation.cache;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class GroupCacheTest {

	private GroupCache cache;

	@Before
	public void setup() {
		cache = new GroupCache();
	}

	@Test
	public void addGroups() {
		Group group1 = new Group();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		Group group2 = new Group();
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
		Group group1 = new Group();
		group1.add("G1Test1");
		group1.add("G1Test2");
		group1.add("G1Test3");
		cache.addGroup(group1);

		Group group2 = new Group();
		group2.add("G2Test1");
		group2.add("G2Test2");
		group2.add("G2Test3");
		cache.addGroup(group2);

		Collection<Group> groups = cache.getGroups();
		assertEquals(2, groups.size());
		Iterator<Group> iterator = groups.iterator();
		assertSame(group1, iterator.next());
		assertSame(group2, iterator.next());
		assertFalse(iterator.hasNext());
	}
}
