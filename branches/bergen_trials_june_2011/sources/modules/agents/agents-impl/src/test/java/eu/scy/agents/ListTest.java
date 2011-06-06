package eu.scy.agents;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListTest {

	List<String> list = new ArrayList<String>();

	@Before
	public void setup() {
		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");
	}

	@Test
	public void testRemoveIndex() {
		assertEquals(4, list.size());
		for (int i = 0; i < 4; i++) {
			assertEquals("" + i, list.get(i));
		}
		list.remove(2);
		assertEquals(3, list.size());
		assertEquals("0", list.get(0));
		assertEquals("1", list.get(1));
		assertEquals("3", list.get(2));
	}

}

