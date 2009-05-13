package eu.scy.agents.api.action;

import java.util.List;

public interface IActionLogDispatcher {
    
    void setAgents(List<IActionLogFilterAgent> agents);
    
    void addAgent(IActionLogFilterAgent agent);
    
}
