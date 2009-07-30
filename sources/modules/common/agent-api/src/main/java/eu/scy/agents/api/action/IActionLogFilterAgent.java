package eu.scy.agents.api.action;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.IAgent;

/**
 * An agent to Process an action log.
 * 
 * @author Florian Schulz
 * 
 */
public interface IActionLogFilterAgent extends IAgent {

	/**
	 * Process an action log and do something with it.
	 * 
	 * @param action
	 *            The action to process.
	 */
	public void process(IAction action);

}
