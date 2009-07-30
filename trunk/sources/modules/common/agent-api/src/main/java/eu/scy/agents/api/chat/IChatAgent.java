package eu.scy.agents.api.chat;

import eu.scy.agents.api.IAgent;

/**
 * Processes chat messages. TODO define more.
 * 
 * @author Florian Schulz
 * 
 */
public interface IChatAgent extends IAgent {

	/**
	 * Processes a chat message.
	 * 
	 * @param message
	 *            The message to process.
	 */
	public void processChatMessage(String message);

}
