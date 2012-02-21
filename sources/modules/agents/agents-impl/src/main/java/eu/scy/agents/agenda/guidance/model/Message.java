package eu.scy.agents.agenda.guidance.model;

public class Message {

	private final long timestamp;
	private final String message;
	
	public Message(long timestamp, String message) {
		this.timestamp = timestamp;
		this.message = message;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getMessage() {
		return this.message;
	}
	
}
