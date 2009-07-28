/**
 * 
 */
package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

public class ThreadedAgentMock extends AbstractThreadedAgent {

    public static final String NAME = "eu.scy.agents.impl.ThreadedAgentMock";

    private boolean first = true;

    private int runCount;

    private boolean updated = false;

    public ThreadedAgentMock(Map<String, Object> map) {
        super(NAME, (String) map.get("id"));
        runCount = 0;
    }

    @Override
    public void doRun() throws TupleSpaceException {
        while (status == Status.Running) {

            sendAliveUpdate();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // @SuppressWarnings("unused")
            // Tuple triggerTuple = getTupleSpace().waitToTake(new Tuple(NAME, String.class, Long.class), 5000);
            // TODO: interpret tuple ...
            // runCount++;
        }
    }

    @Override
    protected void doStop() {
        //Do nothing
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        Tuple t = new Tuple(AgentProtocol.RESPONSE, queryId, this.getId(), this.getName(), AgentProtocol.MESSAGE_IDENTIFY, "This is just a test");
        return t;
    }

    public int getRunCount() {
        return runCount;
    }

    public boolean isFirst() {
        return first;
    }

    // TODO think about it
    @Override
    public boolean isStopped() {
        return status == Status.Stopping;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

}