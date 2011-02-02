package com.liferay.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Sort hasttable with long key and double as value. The list is sort from
 * highest double value to smallest.
 * 
 * @author Daniel
 * 
 */
@SuppressWarnings("unchecked")
public class DoubleComparator implements Comparator {

	public int compare(Object obj1, Object obj2) {

		int result = 0;
		Map.Entry e1 = (Map.Entry) obj1;

		Map.Entry e2 = (Map.Entry) obj2;// Sort based on values.

		Double value1 = (Double) e1.getValue();
		Double value2 = (Double) e2.getValue();

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