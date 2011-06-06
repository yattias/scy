package eu.scy.agents.groupformation;

import org.junit.Test;

public class GroupNumberTest {

	private int numberOfUsers;
	private int minGroupSize;
	private int maxGroupSize;

	public GroupNumberTest() {
	}

	@Test
	public void testGroupNumbers() {
		numberOfUsers = 10;
		for (minGroupSize = 1; minGroupSize < 10; minGroupSize++) {
			for (maxGroupSize = minGroupSize; maxGroupSize < 10; maxGroupSize++) {
				System.out.println(numberOfUsers + "[" + minGroupSize + ", "
						+ maxGroupSize + "]" + getNumberOfGroups());
			}
		}
	}

	public int getNumberOfGroups() {
		int minimumNumberOfGroups = 0;
		if (numberOfUsers % getMaximumGroupSize() == 0) {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize();
		} else {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize() + 1;
		}

		int maximumNumberOfGroups = 0;
		if (numberOfUsers % getMinimumGroupSize() == 0) {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize();
		} else {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize() + 1;
		}

		return (int) Math
				.floor((maximumNumberOfGroups + minimumNumberOfGroups) / 2.0);
	}

	private int getMinimumGroupSize() {
		return minGroupSize;
	}

	private int getMaximumGroupSize() {
		return maxGroupSize;
	}

}
