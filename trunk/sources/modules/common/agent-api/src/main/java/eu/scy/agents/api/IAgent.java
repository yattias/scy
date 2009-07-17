package eu.scy.agents.api;

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

    /*
     * Get an interface to the persistent storage for the agents.
     * 
     * @return The global persisent storage facility.
     */
    // public IPersistentStorage getPersistentStorage();

}
