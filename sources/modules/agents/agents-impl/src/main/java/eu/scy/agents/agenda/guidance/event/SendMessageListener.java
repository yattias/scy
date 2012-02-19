package eu.scy.agents.agenda.guidance.event;

import java.util.EventListener;

public interface SendMessageListener extends EventListener {

	public void sendMessage(SendMessageEvent event);
	
}
