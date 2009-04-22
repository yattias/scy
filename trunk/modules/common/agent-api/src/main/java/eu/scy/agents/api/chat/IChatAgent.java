package eu.scy.agents.api.chat;

import eu.scy.agents.api.IAgent;

public interface IChatAgent extends IAgent {
    
    public void doSomethingWithChatMessage(String message);
    
}
