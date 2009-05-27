/**
 * 
 */
package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;

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

	public ThreadedAgentMock() {
		super(NAME);
		runCount = 0;
	}

	@Override
	protected void doRun(Tuple trigger) {
		runCount++;
	}

	@Override
	protected Tuple getTemplateTuple() {
		return new Tuple(NAME, String.class, Long.class);
	}

	public int getRunCount() {
		return runCount;
	}

	// @Override
	// protected TimerTask getAliveTupleUpdater() {
	// return new MockUpdater();
	// }
}