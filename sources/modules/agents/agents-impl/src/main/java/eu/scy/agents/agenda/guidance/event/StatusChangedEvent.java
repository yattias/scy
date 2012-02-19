package eu.scy.agents.agenda.guidance.event;

import java.util.EventObject;

import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.Activity.ActivityState;

public class StatusChangedEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private final Activity activity;
	private final ActivityState newState;
	
	public StatusChangedEvent(Object source, Activity act, ActivityState newState) {
		super(source);
		this.activity = act;
		this.newState = newState;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public ActivityState getNewState() {
		return this.newState;
	}
	
}
