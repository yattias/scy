package eu.scy.actionlogging.api;

public interface IActionReceiver {
    /**
     * registers new IACtionCallback
     * @param callback
     */
    public void registerCallback(IActionCallback callback);
    
    /**
     * 	deregisters IActionCallback
     * @param callback
     * @return	success/fail
     */
    public boolean deregisterCallback(IActionCallback callback);
    
    /**
     * notifies all IActionCallbacks with an Action
     * @param action
     */
    public void notifyCallbacks(IAction action);
}
