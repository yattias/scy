package eu.scy.agents.api.parameter;

import java.util.List;

/**
 * An interface to set the agent parameters during runtime.
 * 
 * Every parameter can be set for different levels. More specific levels
 * overwrite the upper level. The levels are at the moment: <br/>
 * <ol>
 * <li>The initial agent parameters as a fallback. These parameters are set
 * during creation of the agent and should be overwritten whenever a mission
 * starts.</li>
 * <li>The mission specific level where parameters can be set differently for
 * different missions.</li>
 * <li>The user specific level where parameters for are set for a specific user.
 * </li>
 * </ol>
 * 
 * TODO: Adjust to mission run and activity specific settings.
 * 
 */
public interface AgentParameterAPI {

	/**
	 * Set the parameter for the agent with <code>agentName</code> for a
	 * specific mission and a specific user.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentId
	 *            The name of the agent the parameter should be set for.
	 * @param parameter
	 *            An object describing the parameter and holds its value.
	 * @see AgentParameter
	 */
	public <T> void setParameter(String agentId, AgentParameter parameter);

	/**
	 * Get the current parameter for the agent with <code>agentName</code> for a
	 * specific mission and a specific user.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentId
	 *            The name of the agent the parameter should be set for.
	 * @param parameter
	 *            An object describing the parameter.
	 * @return Returns the current set value or the value one level above the
	 *         specific user level. Which would be mission specific.
	 */
	public <T> T getParameter(String agentId, AgentParameter parameter);

	/**
	 * Get the list of parameter names an agent has.
	 * 
	 * @param agentId
	 *            The id (name) of the agent.
	 * @return A list of parameter names.
	 */
	public List<String> listAgentParameter(String agentId);
}
