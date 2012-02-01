package eu.scy.agents.agenda.guidance.model;

import java.util.EventObject;

public class ActivityModelChangedEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public ActivityModelChangedEvent(Activity act) {
		super(act);
	}
	
}
