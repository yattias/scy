package eu.scy.actionlogging.receiver;

import java.util.Vector;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionCallback;
import eu.scy.actionlogging.api.IActionReceiver;

public class ActionReceiver implements IActionReceiver {
    private Vector<IActionCallback> callbacks = new Vector<IActionCallback>();

    public boolean deregisterCallback(IActionCallback callback) {
        return callbacks.remove(callback);
	}

	public void notifyCallbacks(IAction action) {
	      for(IActionCallback callback : callbacks) {
	            callback.onAction(action);
	        }		
	}

	public void registerCallback(IActionCallback callback) {
	     callbacks.add(callback);		
	}

}
