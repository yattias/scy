package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;
import java.util.List;

import eu.scy.agents.agenda.serialization.UserAction;

public interface HistoryRequestListener extends EventListener {

	public List<UserAction> requestHistory(HistoryRequestEvent event) throws TupleSpaceException;
	
}
