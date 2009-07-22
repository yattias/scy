package eu.scy.agents.impl;

import eu.scy.agents.impl.AbstractThreadedAgent.Status;
import info.collide.sqlspaces.commons.Tuple;

public final class AgentProtocol {

    private static final int COMMAND_EXPIRATION = 5000;

    public static final String COMMAND_SPACE_NAME = "command";

    public static final String COMMAND_LINE = "agentCommand";

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
    public static final Tuple COMMAND_COMMAND_TEMPLATE = new Tuple(COMMAND_LINE, String.class, String.class);

    public static Tuple getStartTuple(String agentName, String agentId) {
        Tuple startTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,agentId, AgentProtocol.MESSAGE_START);
        startTuple.setExpiration(COMMAND_EXPIRATION);
        return startTuple;
    }

    public static Tuple getStopTuple(String agentName, String agentId) {
        Tuple stopTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,agentId, AgentProtocol.MESSAGE_STOP);
        stopTuple.setExpiration(COMMAND_EXPIRATION);
        return stopTuple;
    }

    public static Tuple getKillTuple(String agentName, String agentId) {
        Tuple resumeTuple = new Tuple(AgentProtocol.COMMAND_LINE, agentName,agentId, AgentProtocol.MESSAGE_KILL);
        resumeTuple.setExpiration(COMMAND_EXPIRATION);
        return resumeTuple;
    }

    public static Tuple getAliveTuple(String agentName, String agentId, Status status) {
        Tuple aliveTuple = new Tuple(AgentProtocol.COMMAND_LINE, AgentProtocol.ALIVE, agentId, agentName, status.toString());
        aliveTuple.setExpiration(ALIVE_INTERVAL);
        return aliveTuple;
    }

}
