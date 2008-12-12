package eu.scy.agents.api.action;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.IAgent;

public interface IActionLogFilterAgent extends IAgent {
    
    public void process(IAction action);
    
}
