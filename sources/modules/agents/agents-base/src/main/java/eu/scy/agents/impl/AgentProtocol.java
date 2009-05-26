package eu.scy.agents.impl;

import eu.scy.agents.impl.AbstractThreadedAgent.Status;
import info.collide.sqlspaces.commons.Tuple;

public abstract class AgentProtocol {

	public static final String COMMAND_LINE = "agentCommand";

	public static final Tuple COMMAND_TEMPLATE = new Tuple(COMMAND_LINE,
			String.class, String.class, Long.class);

	public static final String MESSAGE_STOP = "Stop";

	public static final String MESSAGE_START = "Start";

	public static final String MESSAGE_SUSPEND = "Suspend";

	public static final String MESSAGE_RESUME = "Resume";

	public static final String ALIVE = "Alive";

	public static final int ALIVE_INTERVAL = 10 * 1000;

	private static Tuple aliveTupleTemplate = new Tuple(
			AgentProtocol.COMMAND_LINE, AgentProtocol.ALIVE, String.class,
			String.class, Long.class);

	public static Tuple getStartTuple(String agentName) {
		Tuple startTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,
				AgentProtocol.MESSAGE_START, System.currentTimeMillis());
		startTuple.setExpiration(5000);

		return startTuple;
	}

	public static Tuple getStopTuple(String agentName) {
		Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,
				AgentProtocol.MESSAGE_STOP, System.currentTimeMillis());
		stopTuple.setExpiration(5000);

		return stopTuple;
	}

	public static Tuple getSuspendTuple(String agentName) {
		Tuple suspendTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,
				AgentProtocol.MESSAGE_SUSPEND, System.currentTimeMillis());
		suspendTuple.setExpiration(5000);

		return suspendTuple;
	}

	public static Tuple getResumeTuple(String agentName) {
		Tuple resumeTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,
				AgentProtocol.MESSAGE_RESUME, System.currentTimeMillis());
		resumeTuple.setExpiration(5000);

		return resumeTuple;
	}

	public static Tuple getAliveTupleTemplate() {
		return aliveTupleTemplate;
	}

	public static Tuple getAliveTuple(String agentName, Status status) {
		Tuple aliveTuple = new Tuple(AgentProtocol.COMMAND_LINE,
				AgentProtocol.ALIVE, agentName, status.toString(), System
						.currentTimeMillis());
		aliveTuple.setExpiration(ALIVE_INTERVAL);
		return aliveTuple;
	}

}
