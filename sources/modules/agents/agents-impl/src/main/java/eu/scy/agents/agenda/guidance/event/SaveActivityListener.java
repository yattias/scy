package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;

public interface SaveActivityListener extends EventListener {

	public TupleID saveActivity(SaveActivityEvent event) throws TupleSpaceException;
	
}
