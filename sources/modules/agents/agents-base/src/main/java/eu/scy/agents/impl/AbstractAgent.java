package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
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

	private static final String DEFAULT_HOST = "localhost";

	private TupleSpace tupleSpace;

	private boolean runAutonomous;

	private int port;

	private String host;

	/**
	 * The name of the agent.
	 */
	protected String name;

	/**
	 * The id of the agents. Automatically created.
	 */
	protected String id;

	/**
	 * Create a new AbstractAgent with <code>name</code> and <code>id</code>.
	 * 
	 * @param agentName
	 *            The name of the agent.
	 * @param agentId
	 *            The id of the agent.
	 */
	public AbstractAgent(String agentName, String agentId) {
		this(agentName, agentId, DEFAULT_HOST, Configuration.getConfiguration()
				.getNonSSLPort());
	}

	/**
	 * Create a new AbstractAgent that connects to the by <code>host:port</code>
	 * specified TupleSpace.
	 * 
	 * @param agentName
	 *            The name of the agent.
	 * @param agentId
	 *            The id of the agent.
	 * @param tsHost
	 *            The host where the TupleSpace is running.
	 * @param tsPort
	 *            The port the TupleSpace is running on.
	 */
	public AbstractAgent(String agentName, String agentId, String tsHost,
			int tsPort) {
		this(agentName, agentId, tsHost, tsPort, false);
	}

	/**
	 * Create a new AbstractAgent that connects to the by <code>host:port</code>
	 * specified TupleSpace. If <code>runAutonomous</code> is true the agent
	 * will allocate a communication thread to the tuplespace that must be
	 * explicitly stopped. Needed if the agent does not waitToTake but registers
	 * a listener to TupleSpace updates.
	 * 
	 * @param agentName
	 *            The name of the agent.
	 * @param agentId
	 *            The id of the agent.
	 * @param tsHost
	 *            The host where the TupleSpace is running.
	 * @param tsPort
	 *            The port the TupleSpace is running on.
	 * @param runAutonomous
	 *            true if the agents allocated own communication thread.
	 */
	public AbstractAgent(String agentName, String agentId, String tsHost,
			int tsPort, boolean runAutonomous) {
		this.name = agentName;
		this.id = agentId;
		this.host = tsHost;
		this.port = tsPort;
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
				tupleSpace = new TupleSpace(new User(getName()), host, port,
						runAutonomous, false, AgentProtocol.COMMAND_SPACE_NAME);
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
