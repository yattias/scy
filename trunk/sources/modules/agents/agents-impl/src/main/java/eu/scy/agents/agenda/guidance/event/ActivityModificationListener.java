package eu.scy.agents.agenda.guidance.event;

import java.util.EventListener;

public interface ActivityModificationListener extends EventListener {

	public void activityModified(ActivityModificationEvent event);
	
}
