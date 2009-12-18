/**
 * 
 */
package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

public class ThreadedAgentMock extends AbstractThreadedAgent {

    /**
     * Name of the agent.
     */
    public static final String NAME = "eu.scy.agents.impl.ThreadedAgentMock";

    private boolean first = true;

    private int runCount;

    private boolean updated = false;

    public ThreadedAgentMock(Map<String, Object> map) {
        super(NAME, (String) map.get("id"));
        runCount = 0;
    }

    @Override
    public void doRun() throws TupleSpaceException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }
    }

    @Override
    protected void doStop() {
        status = Status.Stopping;
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

    @Override
    public boolean isStopped() {
        return status == Status.Stopping;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setFirst(boolean f) {
        this.first = f;
    }

    public void setUpdated(boolean u) {
        this.updated = u;
    }

}