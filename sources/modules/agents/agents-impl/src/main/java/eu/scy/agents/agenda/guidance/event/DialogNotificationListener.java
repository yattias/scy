package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;

public interface DialogNotificationListener extends EventListener {

	public void sendDialogNotification(DialogNotificationEvent event) throws TupleSpaceException;
	
}
