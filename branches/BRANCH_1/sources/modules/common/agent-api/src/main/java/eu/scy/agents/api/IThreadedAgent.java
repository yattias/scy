package eu.scy.agents.api;

/**
 * Common interface to control an agent that runs as a own thread. As a thread
 * it can be started, stopped and killed.
 * 
 * @author Florian Schulz, Jan Engler, Stefan Weinbrenner
 * 
 */
public interface IThreadedAgent extends IAgent, Runnable {

	/**
	 * This method starts the agent and its logic.
	 * 
	 * @throws AgentLifecycleException
	 *             if something went wrong during starting
	 */
	public void start() throws AgentLifecycleException;

	/**
	 * This method kills the agent.
	 * 
	 * @throws AgentLifecycleException
	 *             if something went wrong during killing
	 */
	public void kill() throws AgentLifecycleException;

	/**
	 * Returns if the Agent is running or not.
	 * 
	 * @return {@code True} if running, {@code false} otherwise.
	 */
	public boolean isRunning();

	/**
	 * Returns if the agent is stopped...
	 * 
	 * @return {@code True} if stopped, {@code false} otherwise.
	 */
	public boolean isStopped();

}
