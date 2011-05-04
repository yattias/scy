package eu.scy.agents.authoring.workflow.paths;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.authoring.workflow.Workflow;
import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.util.time.Duration;

public class PathAnalyzerTest {

	private PathAnalyzer pathAnalyzer;
	private WorkflowItem workflowItem1;
	private WorkflowItem workflowItem2;
	private WorkflowItem workflowItem3;

	@Before
	public void setUp() throws Exception {
		Workflow workflow = new Workflow();
		workflowItem1 = new WorkflowItem("test1");
		workflowItem1.setExpectedTimeInMinutes(30);
		workflow.addWorkflowItem(workflowItem1);

		workflowItem2 = new WorkflowItem("test2");
		workflowItem2.setExpectedTimeInMinutes(10);
		workflow.addWorkflowItem(workflowItem2);

		workflowItem3 = new WorkflowItem("test3");
		workflowItem3.setExpectedTimeInMinutes(20);
		workflow.addWorkflowItem(workflowItem3);

		pathAnalyzer = new PathAnalyzer(workflow);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTimeExcesses() {
		Path path = new Path();
		MockTimer timer = new MockTimer();
		path.setTimer(timer);
		path.addPathComponent(workflowItem1);
		timer.setTime(AgentProtocol.MINUTE * 10);
		path.addPathComponent(workflowItem2);
		timer.addTime(AgentProtocol.MINUTE * 10);
		path.addPathComponent(workflowItem3);
		timer.addTime(AgentProtocol.MINUTE * 10);
		path.addPathComponent(workflowItem1);
		timer.setTime(AgentProtocol.MINUTE * 10);
		path.addPathComponent(workflowItem2);
		timer.addTime(AgentProtocol.MINUTE * 10);
		path.addPathComponent(workflowItem3);
		timer.addTime(AgentProtocol.MINUTE * 10);

		List<TimeExcess> timeExcesses = pathAnalyzer.getTimeExcesses(path);
		assertEquals(1, timeExcesses.size());
		assertEquals(workflowItem2, timeExcesses.get(0).getItem());
		assertEquals(new Duration(AgentProtocol.MINUTE * 10),
				timeExcesses.get(0).getTimeExcess());
	}
}
