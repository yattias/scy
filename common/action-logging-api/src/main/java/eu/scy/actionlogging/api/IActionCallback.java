package eu.scy.actionlogging.api;

public interface IActionCallback {
    /**
     * cp observer pattern -> update()
     * @param action
     */
    public void onAction(IAction action);
    
}
