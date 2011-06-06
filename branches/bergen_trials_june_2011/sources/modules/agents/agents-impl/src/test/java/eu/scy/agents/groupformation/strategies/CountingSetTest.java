package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.util.CountingSet;

public class CountingSetTest {

	private CountingSet<String> countingSet;

	@Before
	public void setup() {
		countingSet = new CountingSet<String>();
	}

	@Test
	public void addIncrementsCounter() {
		assertEquals(0, countingSet.getCount("Test"));
		countingSet.add("Test");
		assertEquals(1, countingSet.getCount("Test"));
		countingSet.add("Test");
		assertEquals(2, countingSet.getCount("Test"));
		countingSet.add("Test2");
		assertEquals(1, countingSet.getCount("Test2"));
	}

	@Test
	public void removeDecrementsCounter() {
		countingSet.add("Test");
		countingSet.add("Test");
		assertEquals(2, countingSet.getCount("Test"));
		countingSet.remove("Test");
		assertEquals(1, countingSet.getCount("Test"));
		countingSet.remove("Test");
		assertEquals(0, countingSet.getCount("Test"));
	}

	@Test
	public void notPresentRemoveDoesNotThrowUp() {
		countingSet.remove("Test");
		assertEquals(0, countingSet.getCount("Test"));
	}

	@Test
	public void testClearAndIsEmpty() {
		countingSet.add("Test");
		countingSet.add("Test");
		assertFalse(countingSet.isEmpty());
		countingSet.clear();
		assertTrue(countingSet.isEmpty());
	}

	@Test
	public void contains() {
		countingSet.add("Test");
		countingSet.add("Test2");
		assertTrue(countingSet.contains("Test"));
		assertTrue(countingSet.contains("Test2"));
		countingSet.remove("Test2");
		assertTrue(countingSet.contains("Test"));
		assertFalse(countingSet.contains("Test2"));
	}

	@Test
	public void toArray() {
		countingSet.add("Test");
		countingSet.add("Test2");

		Object[] keys = countingSet.toArray();
		assertArrayEquals(new String[] { "Test", "Test2" }, keys);
	}

	@Test
	public void toArrayParams() {
		countingSet.add("Test");
		countingSet.add("Test2");

		String[] keys = countingSet.toArray(new String[0]);
		assertArrayEquals(new String[] { "Test", "Test2" }, keys);
	}

	@Test
	public void size() {
		assertEquals(0, countingSet.size());
		countingSet.add("Test");
		assertEquals(1, countingSet.size());
		countingSet.add("Test");
		assertEquals(1, countingSet.size());
		countingSet.add("Test2");
		assertEquals(2, countingSet.size());
		countingSet.remove("Test");
		assertEquals(2, countingSet.size());
		countingSet.remove("Test2");
		assertEquals(1, countingSet.size());
		countingSet.remove("Test");
		assertEquals(0, countingSet.size());
	}

	@Test
	public void addAll() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		assertEquals(3, countingSet.size());
		assertEquals(2, countingSet.getCount("Test"));
		assertEquals(2, countingSet.getCount("Test2"));
		assertEquals(1, countingSet.getCount("Test3"));
	}

	@Test
	public void containsAll() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		assertTrue(countingSet.containsAll(Arrays.asList("Test", "Test2")));
		assertFalse(countingSet.containsAll(Arrays.asList("Test", "Test4")));
	}

	@Test
	public void retainAll() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		countingSet.retainAll(Arrays.asList("Test", "Test2"));
		assertEquals(2, countingSet.size());
		assertFalse(countingSet.contains(Arrays.asList("Test3")));
	}

	@Test
	public void removeAll() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		countingSet.removeAll(Arrays.asList("Test", "Test2"));
		assertEquals(3, countingSet.size());
		assertEquals(1, countingSet.getCount("Test"));
		assertEquals(1, countingSet.getCount("Test2"));
		assertEquals(1, countingSet.getCount("Test3"));
		countingSet.removeAll(Arrays.asList("Test", "Test2"));
		assertEquals(1, countingSet.size());
		assertEquals(1, countingSet.getCount("Test3"));
	}

	@Test
	public void iteration() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		Iterator<String> iter = countingSet.iterator();
		assertEquals("Test", iter.next());
		assertEquals("Test2", iter.next());
		assertEquals("Test3", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void getCountArray() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3", "Test"));
		int[] counts = countingSet.getCountArray();
		assertEquals(3, counts.length);
		assertEquals(3, counts[0]);
		assertEquals(2, counts[1]);
		assertEquals(1, counts[2]);
	}

	@Test
	public void getEntries() {
		countingSet.addAll(Arrays.asList("Test", "Test", "Test2", "Test2",
				"Test3"));
		Set<Entry<String, Integer>> counts = countingSet.getEntries();
		assertEquals(3, counts.size());
		Iterator<Entry<String, Integer>> iterator = counts.iterator();
		Entry<String, Integer> entry0 = iterator.next();
		assertEquals("Test", entry0.getKey());
		assertEquals(Integer.valueOf(2), entry0.getValue());
		Entry<String, Integer> entry1 = iterator.next();
		assertEquals("Test2", entry1.getKey());
		assertEquals(Integer.valueOf(2), entry1.getValue());
		Entry<String, Integer> entry2 = iterator.next();
		assertEquals("Test3", entry2.getKey());
		assertEquals(Integer.valueOf(1), entry2.getValue());
	}
}
