package com.liferay.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Sort hasttable with long key and int as value. The list is sort from highest
 * int value to smallest.
 * 
 * @author Daniel
 * 
 */
@SuppressWarnings("unchecked")
public class IntegerComparator implements Comparator {

	public int compare(Object obj1, Object obj2) {

		int result = 0;
		Map.Entry e1 = (Map.Entry) obj1;

		Map.Entry e2 = (Map.Entry) obj2;// Sort based on values.

		Integer value1 = (Integer) e1.getValue();
		Integer value2 = (Integer) e2.getValue();

		if (value1.compareTo(value2) == 0) {

			Long wordL1 = (Long) e1.getKey();
			Long wordL2 = (Long) e2.getKey();
			String word1 = String.valueOf(wordL1);
			String word2 = String.valueOf(wordL2);

			// Sort String in an alphabetical order
			result = word1.compareToIgnoreCase(word2);

		} else {
			// Sort values in a descending order
			result = value2.compareTo(value1);
		}

		return result;
	}

}