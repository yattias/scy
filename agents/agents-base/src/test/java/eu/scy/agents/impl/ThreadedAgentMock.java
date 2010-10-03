/**
 * 
 */
package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Arrays;
import java.util.Map;

public class ThreadedAgentMock extends AbstractThreadedAgent {

	public static final String TEST_PARAMETER = "TestParameter";
	public static final String TEST_VALUE = "TestValue";
	public static final String MISSION = "SomeMission";
	public static final String USER = "SomeUser";

	/**
	 * Name of the agent.
	 */
	public static final String NAME = ThreadedAgentMock.class.getName();

	private boolean first = true;

	private int runCount;

	private boolean updated = false;

	public ThreadedAgentMock(Map<String, Object> map) {
		super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID));
		configuration.setParameter(TEST_PARAMETER, TEST_VALUE);
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
		Tuple t = new Tuple(AgentProtocol.RESPONSE, queryId, getId(),
				getName(), AgentProtocol.MESSAGE_IDENTIFY,
				"This is just a test");
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
		first = f;
	}

	public void setUpdated(boolean u) {
		updated = u;
	}

	@Override
	protected Tuple getListParameterTuple(String queryId) {
		return AgentProtocol.getListParametersTupleResponse(getName(), queryId,
				Arrays.asList(TEST_PARAMETER));
	}

}
