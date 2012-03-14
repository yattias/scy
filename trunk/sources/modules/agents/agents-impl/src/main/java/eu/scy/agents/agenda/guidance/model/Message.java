package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;
import eu.scy.agents.agenda.exception.InvalidMessageTupleException;

public class Message implements Comparable<Message> {

	public static final String FIELD_MESSAGE = "message";
	
	private long timestamp;
	private String text;
	private String userName;
	private String missionRuntimeUri;
	
	public Message() {
	}
	
	public Message(long timestamp, String text, String userName, String missionRuntimeUri) {
		this.timestamp = timestamp;
		this.text = text;
		this.userName = userName;
		this.missionRuntimeUri = missionRuntimeUri;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getText() {
		return this.text;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getMissionRuntimeUri() {
		return this.missionRuntimeUri;
	}
	
	public void loadFromTuple(Tuple tuple) throws InvalidMessageTupleException {
		if(tuple.getFields().length != 5) {
			throw new InvalidMessageTupleException(
					"Invalid signature - tuple must have 5 fields, but has " + tuple.getFields().length);
		}
		if(!tuple.getField(0).getValue().toString().equals(FIELD_MESSAGE)) {
			throw new InvalidMessageTupleException("Invalid signature - first field must be 'message'");
		}
		String timeStampString = tuple.getField(3).getValue().toString();
		try {
			this.timestamp = Long.valueOf(timeStampString);
			this.userName = tuple.getField(1).getValue().toString();
			this.missionRuntimeUri = tuple.getField(2).getValue().toString();
			this.text = tuple.getField(4).getValue().toString();
		} catch (NumberFormatException e) {
			throw new InvalidMessageTupleException("Invalid signature - invalid timestamp: " + timeStampString);
		}
	}
	
	public Tuple toTuple() {
		Tuple t = new Tuple(FIELD_MESSAGE);
		t.add(this.userName);
		t.add(this.missionRuntimeUri);
		t.add(this.timestamp);
		t.add(this.text);
		return t;
	}

	@Override
	public int compareTo(Message o) {
		return (int)(this.getTimestamp() - o.getTimestamp());
	}
}
