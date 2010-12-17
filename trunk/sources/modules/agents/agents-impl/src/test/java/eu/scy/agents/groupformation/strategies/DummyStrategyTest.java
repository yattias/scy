package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DummyStrategyTest {

	private DummyStrategy strategy;

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
	}

	@Test
	public void testFormGroup() {
		Collection<Set<String>> groups = strategy.formGroup(null);
		assertEquals(2, groups.size());
	}

}
