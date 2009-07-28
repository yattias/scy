package eu.scy.agents.api.chat;

import eu.scy.agents.api.IAgent;

/**
 * Processes chat messages. TODO define more.
 * 
 * @author Florian Schulz
 * 
 */
public interface IChatAgent extends IAgent {

	public void doSomethingWithChatMessage(String message);

}
