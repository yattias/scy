/**
 * 
 */
package eu.scy.agents.impl;

import java.util.Map;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

public class ThreadedAgentMock extends AbstractThreadedAgent {

	private boolean first = true;
	private boolean updated = false;

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public static final String NAME = "MockAgent";
	private int runCount;

	public ThreadedAgentMock(@SuppressWarnings("unused") Map<String, Object> map) {
		super(NAME, (String) map.get("id"));
		runCount = 0;
	}

	@Override
	public void doRun() throws TupleSpaceException {
		while (status == Status.Running) {
			sendAliveUpdate();
			@SuppressWarnings("unused")
			Tuple triggerTuple = getTupleSpace().waitToTake(
					new Tuple(NAME, String.class, Long.class), 5000);
			// TODO: interpret tuple ...
			runCount++;
		}
	}

	public int getRunCount() {
		return runCount;
	}

	@Override
	protected void doStop() {
		// do nothing
	}

	// TODO think about it
	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

    @Override
    protected Tuple getIdentifyTuple() {
        // TODO Auto-generated method stub
        return null;
    }

}