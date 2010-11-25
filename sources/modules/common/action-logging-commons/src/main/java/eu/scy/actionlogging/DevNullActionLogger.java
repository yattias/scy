package eu.scy.actionlogging;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

/**
 * This IActionLogger simply accepts any IAction and does nothing with it.
 * @author lars
 *
 */
public class DevNullActionLogger implements IActionLogger {

    @Override
    public void log(IAction action) {
        // doing absolutely nothing here
    }

    @Override
    @Deprecated
    public void log(String username, String source, IAction action) {
        log(action);
    }

    @Override
    public void setMissionRuntimeURI(String missionRuntimeURI) {
        // doing absolutely nothing here
    }

    @Override
    public String getMissionRuntimeURI() {
        return null;
    }
}
