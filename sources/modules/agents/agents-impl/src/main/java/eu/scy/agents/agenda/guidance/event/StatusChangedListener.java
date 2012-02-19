package eu.scy.agents.agenda.guidance.event;

import java.util.EventListener;

public interface StatusChangedListener extends EventListener {

	public void statusChanged(StatusChangedEvent event);
	
}
