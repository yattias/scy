package eu.scy.agents.api;

/**
 * A common interface for all agent types.
 * 
 * @author fschulz
 */
public interface IAgent {

	/**
	 * Get the name of the agent.
	 * 
	 * @return The name of the agent.
	 */
    public String getName();

    /**
	 * Get the id of the agent.
	 * 
	 * @return The id of the agent
	 */
    public String getId();
    
}
