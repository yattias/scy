package eu.scy.actionlogging.api;

public interface IActionLogger
{
    /**
     * logs an action
     * @param action	the IAction object that will be logged
     */
    public void log(IAction action);

}