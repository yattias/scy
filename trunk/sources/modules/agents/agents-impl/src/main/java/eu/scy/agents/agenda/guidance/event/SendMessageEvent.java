package eu.scy.agents.agenda.guidance.event;

import java.util.EventObject;

public class SendMessageEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private String message;

	private long timestamp;
	
	public SendMessageEvent(Object source, String message, long timestamp) {
		super(source);
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return this.message;
	}

	public long getTimestamp() {
		return this.timestamp;
	}
	
}
