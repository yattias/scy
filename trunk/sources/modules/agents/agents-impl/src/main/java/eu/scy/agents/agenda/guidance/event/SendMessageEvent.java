package eu.scy.agents.agenda.guidance.event;

import java.util.EventObject;

import eu.scy.agents.agenda.guidance.model.Message;

public class SendMessageEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private Message message;

	public SendMessageEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}

	public Message getMessage() {
		return this.message;
	}

}
