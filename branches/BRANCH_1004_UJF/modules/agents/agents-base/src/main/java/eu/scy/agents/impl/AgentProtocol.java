package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;

import java.rmi.dgc.VMID;

/**
 * Class that defines necessary constants and methods for the AgentProtocol.
 * 
 * @author Florian Schulz
 */
public final class AgentProtocol {

	public static final int COMMAND_EXPIRATION = 5000;

	public static final String QUERY = "query";
	public static final String RESPONSE = "response";

	public static final String COMMAND_SPACE_NAME = "command";
	public static final String ACTION_SPACE_NAME = "actions";

	public static final String COMMAND_LINE = "agent command";

	public static final String MESSAGE_IDENTIFY = "identify";
	public static final String MESSAGE_STOP = "stop";
	public static final String MESSAGE_START = "start";
	public static final String MESSAGE_KILL = "kill";

	public static final String ALIVE = "alive";

	public static final String PARAM_AGENT_ID = "id";
	public static final String TS_PORT = "tsPort";
	public static final String TS_HOST = "tsHost";

	/**
	 * The interval in which alive tuples should expire.
	 */
	public static final int ALIVE_INTERVAL = 4 * COMMAND_EXPIRATION;

	public enum ResonseType {
		OK, ANSWER, ERROR;
	}

	public static final String ACTION = "action";
	public static final String ACTION_ELO_SAVED = "elo_save";
	public static final String ACTION_ELO_LOADED = "elo_load";
	public static final String ACTIONLOG_ELO_TYPE = "type";
	public static final String ACTIONLOG_ELO_URI = "elo_uri";
	public static final String ACTION_TOOL_STARTED = "tool_start";
	public static final String ACTION_TOOL_OPENED = "tool_opened";
	public static final String ACTION_TOOL_CLOSED = "tool_closed";
	public static final String ACTION_NODE_ADDED = "node_added";
	public static final String ACTION_NODE_REMOVED = "node_removed";

	public static final String NOTIFICATION = "notification";

	private AgentProtocol() {
		// should never be called
	}

	/**
	 * Get the alive tuple template. <br />
	 * ("agentCommand":String, "Alive":String, <AgentName>:String, <Status>:String, <TimeStamp>:Long)<br />
	 * +AgentName: The name of the Agent <br />
	 * +Status: The status of the Agent (Running,Suspended,Stopped)<br />
	 * +TimeStamp: current time. <br />
	 */
	public static final Tuple ALIVE_TUPLE_TEMPLATE = new Tuple(AgentProtocol.COMMAND_LINE, String.class, String.class,
			String.class, AgentProtocol.ALIVE);

	/**
	 * Get the command tuple template. <br />
	 * ("agentCommand":String, <AgentName>:String <br />
	 * +<AgentName>:String <br />
	 * +<Command>:String <br />
	 */
	public static final Tuple COMMAND_COMMAND_TEMPLATE = new Tuple(COMMAND_LINE, String.class, String.class,
			String.class, String.class);

	public static final Tuple IDENTIFY_TEMPLATE = new Tuple(QUERY, String.class, String.class, String.class,
			MESSAGE_IDENTIFY);

	public static Tuple getStartTuple(String agentId, String agentName, VMID queryId) {
		Tuple startTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_START);
		startTuple.setExpiration(COMMAND_EXPIRATION);
		return startTuple;
	}

	public static Tuple getStopTuple(String agentId, String agentName, VMID queryId) {
		Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_STOP);
		stopTuple.setExpiration(COMMAND_EXPIRATION);
		return stopTuple;
	}

	public static Tuple getCommandTuple(String agentId, String agentName) {
		Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, String.class, agentId, agentName,
				AgentProtocol.MESSAGE_STOP);
		stopTuple.setExpiration(COMMAND_EXPIRATION);
		return stopTuple;
	}

	public static Tuple getAliveTuple(String agentId, String agentName, VMID queryId) {
		Tuple aliveTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName,
				AgentProtocol.ALIVE);
		aliveTuple.setExpiration(2 * ALIVE_INTERVAL);
		return aliveTuple;
	}

	public static Tuple getIdentifyTuple(String agentId, String agentName, VMID queryId) {
		Tuple identifyTuple = new Tuple(AgentProtocol.QUERY, queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_IDENTIFY);
		identifyTuple.setExpiration(COMMAND_EXPIRATION);
		return identifyTuple;
	}

	public static Tuple getIdentifyTuple(String agentId, String agentName) {
		Tuple identifyTuple = new Tuple(AgentProtocol.QUERY, String.class, agentId, agentName,
				AgentProtocol.MESSAGE_IDENTIFY);
		identifyTuple.setExpiration(COMMAND_EXPIRATION);
		return identifyTuple;
	}

}
