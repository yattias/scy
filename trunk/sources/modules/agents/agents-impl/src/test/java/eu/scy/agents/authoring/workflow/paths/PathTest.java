package eu.scy.agents.authoring.workflow.paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Duration;

public class PathTest {

	private Path path;
	private MockTimer timer;

	@Before
	public void setUp() throws Exception {
		path = new Path();
		timer = new MockTimer();
		path.setTimer(timer);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void pathTiming() {
		timer.setTime(0);
		path.addPathComponent(new WorkflowItem("TestID2"));
		timer.setTime(1000);
		path.addPathComponent(new WorkflowItem("TestID1"));
		timer.setTime(2000);
		path.addPathComponent(new WorkflowItem("TestID2"));
		timer.setTime(3000);
		path.addPathComponent(new WorkflowItem("TestID3"));
		timer.setTime(4000);
		path.addPathComponent(new WorkflowItem("TestID1"));
		timer.setTime(5000);
		path.addPathComponent(new WorkflowItem("TestID4"));
		timer.setTime(6000);
		path.stopTiming();
		timer.setTime(15000);
		path.addPathComponent(new WorkflowItem("TestID4"));
		timer.setTime(16000);

		assertEquals(new Duration(2000),
				path.timeSpentInItem(new WorkflowItem("TestID1")));
		assertEquals(new Duration(2000),
				path.timeSpentInItem(new WorkflowItem("TestID2")));
		assertEquals(new Duration(1000),
				path.timeSpentInItem(new WorkflowItem("TestID3")));
		assertEquals(new Duration(2000),
				path.timeSpentInItem(new WorkflowItem("TestID4")));
	}
}
