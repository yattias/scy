package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;

import java.rmi.dgc.VMID;
import java.util.List;

/**
 * Class that defines necessary constants and methods for the AgentProtocol.
 * 
 * @author Florian Schulz
 */
public final class AgentProtocol {

	public static final int MILLI_SECOND = 1;
	public static final int SECOND = 1000 * MILLI_SECOND;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;

	public static final int COMMAND_EXPIRATION = 5 * SECOND;

	public static final String QUERY = "query";
	public static final String RESPONSE = "response";

	public static final String COMMAND_SPACE_NAME = "command";
	public static final String ACTION_SPACE_NAME = "actions";
	public static final String SESSION_SPACE_NAME = "session";

	public static final String COMMAND_LINE = "agent command";
	public static final String AGENT_PARAMETER_SET = "agent_parameter_set";
	public static final String AGENT_PARAMETER_GET = "agent_parameter_get";
	public static final Object LIST_PARAMETERS = "list_parameters";

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

	public static final String NOTIFICATION = "notification";

	private AgentProtocol() {
		// should never be called
	}

	/**
	 * Get the alive tuple template. <br />
	 * ("agentCommand":String, "Alive":String, <AgentName>:String,
	 * <Status>:String, <TimeStamp>:Long)<br />
	 * +AgentName: The name of the Agent <br />
	 * +Status: The status of the Agent (Running,Suspended,Stopped)<br />
	 * +TimeStamp: current time. <br />
	 */
	public static final Tuple ALIVE_TUPLE_TEMPLATE = new Tuple(
			AgentProtocol.COMMAND_LINE, String.class, String.class,
			String.class, AgentProtocol.ALIVE);

	/**
	 * Get the command tuple template. <br />
	 * ("agentCommand":String, <AgentName>:String <br />
	 * +<AgentName>:String <br />
	 * +<Command>:String <br />
	 */
	public static final Tuple COMMAND_COMMAND_TEMPLATE = new Tuple(
			COMMAND_LINE, String.class, String.class, String.class,
			String.class);

	public static final Tuple IDENTIFY_TEMPLATE = new Tuple(QUERY,
			String.class, String.class, String.class, MESSAGE_IDENTIFY);

	/**
	 * (list_parameters:String, query:String,
	 * <QueryId>:String,<AgentName>:String)
	 */
	public static final Tuple LIST_PARAMETER_QUERY = new Tuple(LIST_PARAMETERS,
			QUERY, String.class, String.class);

	/**
	 * (list_parameters:String, response:String,
	 * <QueryId>:String,<AgentName>:String, *:String)
	 */
	public static final Tuple LIST_PARAMETER_RESPONSE = new Tuple(
			LIST_PARAMETERS, RESPONSE, String.class, String.class,
			Field.createWildCardField());

	/**
	 * ("agent_parameter_get","query", <QueryId>:String, <ParameterName>:String,
	 * <MissionUri>:String,<LAS>:String, <ELOUri>:String, <User>:String)
	 */
	public static final Tuple PARAMETER_GET_QUERY = new Tuple(
			AGENT_PARAMETER_GET, QUERY, String.class, String.class,
			String.class, String.class, String.class, String.class);

	/**
	 * Get a tuple that identifies a parameter set command. The tuple format is: <br/>
	 * ("agent_parameter_set":String, <AgentName>:String, <Mission>:String,
	 * <User>:String, <ParameterName>:String, <ParameterValue>:*)<br/>
	 * 
	 */
	public static final Tuple getParameterSetTupleTemplate(String agentName) {
		return new Tuple(AgentProtocol.AGENT_PARAMETER_SET, agentName,
				String.class, String.class, String.class,
				Field.createWildCardField());
	}

	/**
	 * Get a tuple that identifies a parameter get command. The tuple format is: <br/>
	 * ("agent_parameter_get":String, "query":String, <AgentName>:String,
	 * <Mission>:String, <User>:String, <ParameterName>:String)<br/>
	 * 
	 */
	public static final Tuple getParameterGetQueryTupleTemplate(String agentName) {
		return new Tuple(AgentProtocol.AGENT_PARAMETER_GET,
				AgentProtocol.QUERY, agentName, String.class, String.class,
				String.class);
	}

	/**
	 * Get a tuple that identifies a parameter get command. The tuple format is: <br/>
	 * ("agent_parameter_get":String, "response":String, <AgentName>:String,
	 * <Mission>:String, <User>:String, <ParameterName>:String,
	 * <ParameterValue>:*)<br/>
	 */
	public static final Tuple getParameterGetResponseTupleTemplate(
			String agentName) {
		return new Tuple(AgentProtocol.AGENT_PARAMETER_GET,
				AgentProtocol.RESPONSE, agentName, String.class, String.class,
				String.class, Field.createWildCardField());
	}

	public static Tuple getStartTuple(String agentId, String agentName,
			VMID queryId) {
		Tuple startTuple = new Tuple(AgentProtocol.COMMAND_LINE,
				queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_START);
		startTuple.setExpiration(COMMAND_EXPIRATION);
		return startTuple;
	}

	public static Tuple getStopTuple(String agentId, String agentName,
			VMID queryId) {
		Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE,
				queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_STOP);
		stopTuple.setExpiration(COMMAND_EXPIRATION);
		return stopTuple;
	}

	public static Tuple getCommandTuple(String agentId, String agentName) {
		Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, String.class,
				agentId, agentName, AgentProtocol.MESSAGE_STOP);
		stopTuple.setExpiration(COMMAND_EXPIRATION);
		return stopTuple;
	}

	public static Tuple getAliveTuple(String agentId, String agentName,
			VMID queryId) {
		Tuple aliveTuple = new Tuple(AgentProtocol.COMMAND_LINE,
				queryId.toString(), agentId, agentName, AgentProtocol.ALIVE);
		aliveTuple.setExpiration(2 * ALIVE_INTERVAL);
		return aliveTuple;
	}

	public static Tuple getIdentifyTuple(String agentId, String agentName,
			VMID queryId) {
		Tuple identifyTuple = new Tuple(AgentProtocol.QUERY,
				queryId.toString(), agentId, agentName,
				AgentProtocol.MESSAGE_IDENTIFY);
		identifyTuple.setExpiration(COMMAND_EXPIRATION);
		return identifyTuple;
	}

	public static Tuple getIdentifyTuple(String agentId, String agentName) {
		Tuple identifyTuple = new Tuple(AgentProtocol.QUERY, String.class,
				agentId, agentName, AgentProtocol.MESSAGE_IDENTIFY);
		identifyTuple.setExpiration(COMMAND_EXPIRATION);
		return identifyTuple;
	}

	public static Tuple getListParametersTupleQuery(String agentName,
			String queryId) {
		Tuple listParameterQuery = LIST_PARAMETER_QUERY;
		listParameterQuery.getField(2).setValue(queryId);
		listParameterQuery.getField(3).setValue(agentName);
		return listParameterQuery;
	}

	public static Tuple getListParametersTupleResponse(String agentName,
			String queryId, List<String> parameterNames) {
		Tuple listParameterResponse = new Tuple(LIST_PARAMETERS, RESPONSE,
				queryId, agentName);
		for (String parameterName : parameterNames) {
			listParameterResponse.add(parameterName);
		}
		return listParameterResponse;
	}
}
