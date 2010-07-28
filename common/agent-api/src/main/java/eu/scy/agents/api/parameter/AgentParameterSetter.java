package eu.scy.agents.api.parameter;

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
public interface AgentParameterSetter {

	/**
	 * Set the parameter for the agent with <code>agentName</code> for a
	 * specific mission and a specific user.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param parameter
	 *            An object describing the parameter and holds its value.
	 * @see AgentParameter
	 */
	public <T> void setParameter(String agentName, AgentParameter parameter);

	/**
	 * Get the current parameter for the agent with <code>agentName</code> for a
	 * specific mission and a specific user.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param parameter
	 *            An object describing the parameter.
	 * @return Returns the current set value or the value one level above the
	 *         specific user level. Which would be mission specific.
	 */
	public <T> T getParameter(String agentName, AgentParameter parameter);

	/**
	 * Set the parameter for the agent with <code>agentName</code> for a
	 * specific mission and all users.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param mission
	 *            The mission the parameter is valid for.
	 * @param parameterName
	 *            The name of the parameter. TODO These parameter names should
	 *            be published by agents.
	 * @param value
	 *            The value to set.
	 */
	// public <T> void setParameter(String agentName, String mission,
	// String parameterName, T value);

	/**
	 * Get the current parameter for the agent with <code>agentName</code> for a
	 * specific mission.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param mission
	 *            The mission the parameter is valid for.
	 * @param parameterName
	 *            The name of the parameter. TODO These parameter names should
	 *            be published by agents.
	 * @return Returns the current set value or the value one level above the
	 *         specific mission level. Which would be general level.
	 */
	// public <T> T getParameter(String agentName, String mission,
	// String parameterName);

	/**
	 * Set the parameter for the agent with <code>agentName</code> for all
	 * missions and all users.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param parameterName
	 *            The name of the parameter. TODO These parameter names should
	 *            be published by agents.
	 * @param value
	 *            The value to set.
	 */
	// public <T> void setParameter(String agentName, String parameterName, T
	// value);

	/**
	 * Get the current global parameter for the agent with
	 * <code>agentName</code>.
	 * 
	 * @param <T>
	 *            The type of the parameter to set.
	 * @param agentName
	 *            The name of the agent the parameter should be set for.
	 * @param mission
	 *            The mission the parameter is valid for.
	 * @param parameterName
	 *            The name of the parameter. TODO These parameter names should
	 *            be published by agents.
	 * @return Returns the current set value. Should not be null.
	 */
	// public <T> T getParameter(String agentName, String parameterName);
}
