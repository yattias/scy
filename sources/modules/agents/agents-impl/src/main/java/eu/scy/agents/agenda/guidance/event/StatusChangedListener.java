package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;

public interface StatusChangedListener extends EventListener {

	public void statusChanged(StatusChangedEvent event) throws TupleSpaceException;
	
}
