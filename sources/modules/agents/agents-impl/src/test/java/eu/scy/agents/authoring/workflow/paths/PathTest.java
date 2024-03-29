package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        //path.stopTiming();
        timer.setTime(15000);
        path.addPathComponent(new WorkflowItem("TestID4"));
        timer.setTime(16000);

        assertEquals(new Duration(2000), path.timeSpentInItem(new WorkflowItem("TestID1")));
        assertEquals(new Duration(2000), path.timeSpentInItem(new WorkflowItem("TestID2")));
        assertEquals(new Duration(1000), path.timeSpentInItem(new WorkflowItem("TestID3")));
        assertEquals(new Duration(11000), path.timeSpentInItem(new WorkflowItem("TestID4")));

        assertEquals(new Duration(0), path.timeSpentInItem(new WorkflowItem("TestID5")));
    }

    @Test
    public void pathTimingWithTimeStamp() {
        timer.setTime(16);
        path.addPathComponent(new WorkflowItem("TestID2"),2);
        path.addPathComponent(new WorkflowItem("TestID1"),4);
        path.addPathComponent(new WorkflowItem("TestID2"),6);
        path.addPathComponent(new WorkflowItem("TestID3"),8);
        path.addPathComponent(new WorkflowItem("TestID1"),10);
        path.addPathComponent(new WorkflowItem("TestID4"),12);
        path.addPathComponent(new WorkflowItem("TestID4"),14);

        assertEquals(new Duration(4), path.timeSpentInItem(new WorkflowItem("TestID1")));
        assertEquals(new Duration(4), path.timeSpentInItem(new WorkflowItem("TestID2")));
        assertEquals(new Duration(2), path.timeSpentInItem(new WorkflowItem("TestID3")));
        assertEquals(new Duration(4), path.timeSpentInItem(new WorkflowItem("TestID4")));

        assertEquals(new Duration(0), path.timeSpentInItem(new WorkflowItem("TestID5")));
    }
}
