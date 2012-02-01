package eu.scy.agents.agenda.guidance.model;

import java.util.EventListener;

public interface ActivityModelChangedListener extends EventListener {

	public void modelChanged(ActivityModelChangedEvent event);
	
}
