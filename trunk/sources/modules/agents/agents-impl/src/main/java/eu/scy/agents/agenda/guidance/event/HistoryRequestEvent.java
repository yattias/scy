package eu.scy.agents.agenda.guidance.event;

import java.util.Collection;
import java.util.EventObject;

import eu.scy.agents.agenda.guidance.model.Activity;

public class HistoryRequestEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private final long timestamp;
	
	private final Collection<Activity> activities;
	
	public HistoryRequestEvent(Object source, Collection<Activity> activities, long timestamp) {
		super(source);
		this.activities = activities;
		this.timestamp = timestamp;
	}

	public Collection<Activity> getActivities() {
		return this.activities;
	}

	public long getTimestamp() {
		return this.timestamp;
	}
	
}
