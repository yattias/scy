package eu.scy.actionlogging.api;

public interface IActionLogger
{
    /**
     * logs an action
     * @param action	the IAction object that will be logged
     */
    public void log(IAction action);
    
    /**
     * Sets the missionRuntimeURI, which will be set to each action to be logged.
     * If no missionRuntimeURI is set, the value will not be changed.
     * @param missionRuntimeURI	the missionRuntimeURI as a string
     */
    public void setMissionRuntimeURI(String missionRuntimeURI);

    /**
     * Gets the missionRuntimeURI. If it hasn't been set before (via constructor
     * or setter), the return value might null.
     * @return String missionRuntimeURI
     */
    public String getMissionRuntimeURI();

    /**
     * logs an action
     * @param source	the tool throwing the action
     * @param action	the IAction object that will be logged
     */
    @Deprecated
    public void log(String username, String source, IAction action);
    
}
