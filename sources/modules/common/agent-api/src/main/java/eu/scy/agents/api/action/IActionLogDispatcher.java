package eu.scy.agents.api.action;

import java.util.List;

/**
 * Intercepts every action log message that is saved to the action log
 * repository and hands it over to registered agents for processing.
 * 
 * @author Florian Schulz
 * 
 */
public interface IActionLogDispatcher {

	/**
	 * Set a list of agents that receive the action logs. Should mainly used by
	 * Spring.
	 * 
	 * @param agents
	 *            The list of agents to set.
	 */
	void setAgents(List<IActionLogFilterAgent> agents);

	/**
	 * Add a single action to the processing queue.
	 * 
	 * @param agent
	 *            The agent to add.
	 */
	void addAgent(IActionLogFilterAgent agent);

}
