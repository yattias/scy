package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Tuple;

import java.rmi.dgc.VMID;

public final class AgentProtocol {

    private static final int COMMAND_EXPIRATION = 5000;

    public static final String QUERY = "query";

    public static final String RESPONSE = "query";

    public static final String COMMAND_SPACE_NAME = "command";

    public static final String COMMAND_LINE = "agentCommand";

    public static final String MESSAGE_IDENTIFY = "identify";

    public static final String MESSAGE_STOP = "Stop";

    public static final String MESSAGE_START = "Start";

    public static final String MESSAGE_KILL = "Kill";

    public static final String ALIVE = "Alive";

    public static final int ALIVE_INTERVAL = 2 * 5000;

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
    public static Tuple ALIVE_TUPLE_TEMPLATE = new Tuple(AgentProtocol.COMMAND_LINE, AgentProtocol.ALIVE, String.class, String.class);

    /**
     * Get the command tuple template. <br />
     * ("agentCommand":String, <AgentName>:String <br />
     * +<AgentName>:String <br />
     * +<Command>:String <br />
     */
    public static final Tuple COMMAND_COMMAND_TEMPLATE = new Tuple(COMMAND_LINE, String.class, String.class, String.class, String.class);

    public static final Tuple IDENTIFY_TEMPLATE = new Tuple(QUERY, String.class, String.class, String.class, MESSAGE_IDENTIFY);

    public static Tuple getStartTuple(String agentId, String agentName, VMID queryId) {
        Tuple startTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName, AgentProtocol.MESSAGE_START);
        startTuple.setExpiration(COMMAND_EXPIRATION);
        return startTuple;
    }

    public static Tuple getStopTuple(String agentId, String agentName, VMID queryId) {
        Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName, AgentProtocol.MESSAGE_STOP);
        stopTuple.setExpiration(COMMAND_EXPIRATION);
        return stopTuple;
    }

    public static Tuple getAliveTuple(String agentId, String agentName, VMID queryId) {
        Tuple aliveTuple = new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), agentId, agentName, AgentProtocol.ALIVE);
        aliveTuple.setExpiration(ALIVE_INTERVAL);
        return aliveTuple;
    }

    public static Tuple getIdentifyTuple(String agentId, String agentName, VMID queryId) {
        Tuple identifyTuple = new Tuple(AgentProtocol.QUERY, queryId.toString(), agentId, agentId, AgentProtocol.MESSAGE_IDENTIFY);
        identifyTuple.setExpiration(COMMAND_EXPIRATION);
        return identifyTuple;
    }

}
