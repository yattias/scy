package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;

public interface ClearCurtainListener extends EventListener {

	public void clearCurtain(ClearCurtainEvent event) throws TupleSpaceException;
	
}
