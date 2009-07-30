package eu.scy.agents.impl.action;

import java.util.LinkedList;
import java.util.List;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionCallback;
import eu.scy.agents.api.action.IActionLogDispatcher;
import eu.scy.agents.api.action.IActionLogFilterAgent;

/**
 * Hands every action log that is written to the log repository to the
 * registered agents.
 * 
 * @author Florian Schulz
 * 
 */
public class ActionLogDispatcher implements IActionLogDispatcher,
		IActionCallback {

	List<IActionLogFilterAgent> agentList;

	/**
	 * Create a new ActionLogDispatcher. Needs to be registered as listener on
	 * the action log repository.
	 */
	public ActionLogDispatcher() {
		agentList = new LinkedList<IActionLogFilterAgent>();
	}

	@Override
	public void addAgent(IActionLogFilterAgent agent) {
		agentList.add(agent);
	}

	@Override
	public void setAgents(List<IActionLogFilterAgent> agents) {
		agentList.addAll(agents);
	}

	@Override
	public void onAction(IAction action) {
		for (IActionLogFilterAgent agent : agentList) {
			agent.process(action);
		}
	}

}
