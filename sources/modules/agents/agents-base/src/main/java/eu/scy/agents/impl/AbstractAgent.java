package eu.scy.agents.impl;

import info.collide.sqlspaces.client.ClientConnector;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.client.XMLClientConnector;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import eu.scy.agents.api.IAgent;

/**
 * Implementation of the IAgent interface.
 * 
 * @author fschulz
 */
public abstract class AbstractAgent implements IAgent {

    private static final String default_host = "localhost";

    private TupleSpace tupleSpace;

    private boolean runAutonomous;

    private int port;

    private String host;

    protected String name;

    protected String id;

    public AbstractAgent(String agentName, String id) {
        this(agentName, id, default_host, Configuration.getConfiguration().getNonSSLPort());
    }

    public AbstractAgent(String agentName, String id, String host, int port) {
        this(agentName, id, host, port, false);
    }

    public AbstractAgent(String agentName, String id, String host, int port, boolean runAutonomous) {
        this.name = agentName;
        this.id = id;
        this.host = host;
        this.port = port;
        this.runAutonomous = runAutonomous;
    }

    /**
     * Get an instance of the tuplespace.
     * 
     * @return The global instance of the tuple space.
     */
    public TupleSpace getTupleSpace() {
        if (tupleSpace == null) {
            try {
                tupleSpace = new TupleSpace(new User(getName()), host, port, runAutonomous, false, AgentProtocol.COMMAND_SPACE_NAME);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return tupleSpace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

}
