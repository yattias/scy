package eu.scy.agents.agenda.guidance.event;

import java.util.EventObject;

import eu.scy.agents.agenda.guidance.model.Activity;

public class ActivityModificationEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	
	public ActivityModificationEvent(Object source, Activity act) {
		super(source);
		this.activity = act;
	}

	public Activity getActivity() {
		return this.activity;
	}
	
}
