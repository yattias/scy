package eu.scy.actionlogging.api;

public interface IActionLogger
{
    /**
     * logs an action
     * @param action	the IAction object that will be logged
     */
    public void log(IAction action);
    
    /**
     * logs an action
     * @param source	the tool throwing the action
     * @param action	the IAction object that will be logged
     */
    @Deprecated
    public void log(String username, String source, IAction action);
    
}
