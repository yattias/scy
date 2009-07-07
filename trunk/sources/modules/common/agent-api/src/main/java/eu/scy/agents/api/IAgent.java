package eu.scy.agents.api;

import info.collide.sqlspaces.client.TupleSpace;

/**
 * A common interface for all agent types.
 * 
 * @author fschulz
 */
public interface IAgent {

	/**
	 * Get the name of the agent.
	 */
	public String getName();

	/**
	 * Get an instance of the tuplespace.
	 * 
	 * @return The global instance of the tuple space.
	 */
	public TupleSpace getTupleSpace();

	/*
	 * Get an interface to the persistent storage for the agents.
	 * 
	 * @return The global persisent storage facility.
	 */
	// public IPersistentStorage getPersistentStorage();
}
