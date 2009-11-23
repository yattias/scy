package eu.scy.agents.api.elo;

import java.util.List;

import roolo.api.IRepository;

/**
 * This interface provides the basic functionality the Roolo-Agent dispatcher
 * needs to provide. The AgentDispatcher intercepts save, update and retrieve
 * calls and adds processing capabilities before an elo is saved or retrieved.
 * That is how agent provided metadata can be saved in an elo.
 * 
 * @author Florian Schulz
 * 
 */
public interface IELOAgentDispatcher extends IRepository {

	/**
	 * Set the repository calls are redirected to after processing.
	 * 
	 * @param repository
	 *            The roolo repository.
	 */
	void setRepository(IRepository repository);

	/**
	 * Get the repository calls are redirected to after processing.
	 * 
	 * 
	 * @return The roolo repository.
	 */
	IRepository getRepository();

	/**
	 * Set a list of agents that process an elo before it is saved. Mainly used
	 * by spring.
	 * 
	 * @param agents
	 *            A list of before agents.
	 * @see IELOAgentDispatcher#addBeforeAgent(IELOFilterAgent)
	 */
	void setBeforeAgents(List<IELOFilterAgent> agents);

	/**
	 * Add a single agent in the before processing queue.
	 * 
	 * @param agent
	 *            The agent to add to the queue.
	 * @see IELOAgentDispatcher#setBeforeAgents(List)
	 */
	void addBeforeAgent(IELOFilterAgent agent);

	/**
	 * Set a list of agents that process and elo after it has been saved and
	 * before a retrieve.
	 * 
	 * @param agents
	 *            The list of afer-agents to set.
	 * @see IELOAgentDispatcher#addAfterAgent(IELOFilterAgent)
	 */
	void setAfterAgents(List<IELOFilterAgent> agents);

	/**
	 * Add a single agent to the after processing queue.
	 * 
	 * @param agent
	 *            The agent to add to the queue.
	 * @see IELOAgentDispatcher#setAfterAgents(List)
	 */
	void addAfterAgent(IELOFilterAgent agent);

	/**
	 * Set a list of agents that just notify something via tuplespace and do no
	 * processing of the metadata. Elos will have an url when notification
	 * agents are processing an elo.
	 * 
	 * @param agents
	 *            The list of agents to set.
	 * @see IELOAgentDispatcher#addNotificationAgent(IELOFilterAgent)
	 */
	void setNotificationAgents(List<IELOFilterAgent> agents);

	/**
	 * Add an agent to the notification queue.
	 * 
	 * @param agent
	 *            The agent to add to the notification queue.
	 * @see IELOAgentDispatcher#setNotificationAgents(List)
	 */
	void addNotificationAgent(IELOFilterAgent agent);

}