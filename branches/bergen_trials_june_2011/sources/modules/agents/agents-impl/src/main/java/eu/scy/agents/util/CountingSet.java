package eu.scy.agents.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * Holds a set of items that can be added several times. For each addition of
 * the same key a count is increased.
 * 
 * @author fschulz
 * 
 * @param <T>
 *            Type of the keys
 */
public class CountingSet<T> implements Collection<T> {

	private Map<T, Integer> countingSet;

	public CountingSet() {
		countingSet = new LinkedHashMap<T, Integer>();
	}

	@Override
	public synchronized boolean add(T key) {
		Integer count = countingSet.get(key);
		if (count == null) {
			countingSet.put(key, 1);
		} else {
			countingSet.put(key, count + 1);
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized boolean remove(Object key) {
		Integer count = countingSet.get(key);
		if (count == null) {
			return false;
		}
		if (count == 1) {
			countingSet.remove(key);
		} else {
			countingSet.put((T) key, count - 1);
		}
		return true;
	}

	@Override
	public synchronized boolean isEmpty() {
		return countingSet.isEmpty();
	}

	@Override
	public synchronized void clear() {
		countingSet.clear();
	}

	/**
	 * Get the count for a key in the set.
	 * 
	 * @param key
	 *            The key to get the count for.
	 * @return The number of times the key is in the set.
	 */
	public synchronized int getCount(T key) {
		Integer count = countingSet.get(key);
		if (count != null) {
			return count;
		}
		return 0;
	}

	@Override
	public synchronized boolean contains(Object key) {
		return countingSet.containsKey(key);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T element : c) {
			add(element);
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean containsAll = true;
		for (Object element : c) {
			containsAll &= contains(element);
		}
		return containsAll;
	}

	@Override
	public Iterator<T> iterator() {
		return countingSet.keySet().iterator();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean allRemoved = true;
		for (Object element : c) {
			allRemoved |= remove(element);
		}
		return allRemoved;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean allRemoved = false;
		for (T element : countingSet.keySet()) {
			if (!c.contains(element)) {
				allRemoved |= remove(element);
			}
		}
		return allRemoved;
	}

	@Override
	public int size() {
		return countingSet.size();
	}

	@Override
	public Object[] toArray() {
		return countingSet.keySet().toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return countingSet.keySet().toArray(a);
	}

	/**
	 * Get the counts as an array. The array corresponds to the array that is
	 * returned by toArray.
	 * 
	 * @return The counts as array.
	 */
	@SuppressWarnings("unchecked")
	public int[] getCountArray() {
		int[] counts = new int[size()];
		int i = 0;
		for (Object key : toArray()) {
			counts[i] = getCount((T) key);
			i++;
		}
		return counts;
	}

	/**
	 * Get the entries in this CountingSet.
	 * 
	 * @return The entries containing the keys and their counts.
	 */
	public Set<Entry<T, Integer>> getEntries() {
		return countingSet.entrySet();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Entry<T, Integer> entry : getEntries()) {
			builder.append("(");
			builder.append(entry.getKey().toString());
			builder.append(",");
			builder.append(entry.getValue().toString());
			builder.append(")");
		}
		builder.append("}");
		return builder.toString();
	}

}
