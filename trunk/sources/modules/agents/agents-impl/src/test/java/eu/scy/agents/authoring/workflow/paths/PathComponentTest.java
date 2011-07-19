package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.util.time.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathComponentTest {

    private MockTimer mockTimer;
    private PathComponent component;

    @Before
    public void setUp() throws Exception {
        mockTimer = new MockTimer();
        component = new PathComponent(mockTimer, null);
    }

    @Test
    public void testGetTimeSpent() {
        component = new PathComponent(mockTimer, null, 0L);
        //		mockTimer.setTime(1000);
        component.startTiming();
        mockTimer.setTime(2000);
        component.endTiming(2000);
        assertEquals(new Duration(2000), component.getTimeSpent());
    }

    @Test
    public void testGetTimeSpentWithTimer() {
        mockTimer.setTime(1000);
        component.startTiming();
        mockTimer.setTime(2000);
        component.endTiming();
        assertEquals(new Duration(1000), component.getTimeSpent());
    }

    @Test
    public void testGetTimeSnapshot() {
        mockTimer.setTime(1000);
        component.startTiming();
        mockTimer.setTime(2000);
        assertEquals(new Duration(1000), component.getTimeSpent());
    }


}