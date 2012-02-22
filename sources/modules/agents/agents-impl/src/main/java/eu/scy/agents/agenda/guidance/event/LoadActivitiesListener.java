package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;
import java.util.List;


public interface LoadActivitiesListener extends EventListener {

	public List<Tuple> loadActivities(LoadActivitiesEvent event) throws TupleSpaceException;
	
}
