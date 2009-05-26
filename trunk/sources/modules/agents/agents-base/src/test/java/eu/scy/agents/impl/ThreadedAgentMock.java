/**
 * 
 */
package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;

public class ThreadedAgentMock extends AbstractThreadedAgent {

	public static final String NAME = "MockAgent";
	private int runCount = 0;

	public ThreadedAgentMock() {
		super(NAME);
	}

	@Override
	protected void doRun() {
		runCount++;
	}

	@Override
	protected Tuple getTemplateTuple() {
		return new Tuple(NAME, String.class, Long.class);
	}

	public int getRunCount() {
		return runCount;
	}

}