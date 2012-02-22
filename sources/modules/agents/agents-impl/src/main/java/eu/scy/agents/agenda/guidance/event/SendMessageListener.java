package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.EventListener;

public interface SendMessageListener extends EventListener {

	public void sendCurtainMessage(SendMessageEvent event) throws TupleSpaceException;
	
}
