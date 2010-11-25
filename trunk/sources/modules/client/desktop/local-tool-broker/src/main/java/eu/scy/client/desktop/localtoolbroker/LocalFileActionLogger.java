package eu.scy.client.desktop.localtoolbroker;

import java.io.Closeable;

import eu.scy.actionlogging.FileLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

/**
 * 
 * @author bollen, sikken
 */
public class LocalFileActionLogger implements IActionLogger, Closeable {

    private static final String fileName = "actions";
    private String logDirectory;
    private FileLogger fileLogger = null;
    private boolean enableLogging = false;
    private String missionRuntimeURI = null;

    public LocalFileActionLogger(String logDirectory) {
        this(logDirectory, null);
    }

    public LocalFileActionLogger(String logDirectory, String missionRuntimeURI) {
        this.logDirectory = logDirectory;
        this.missionRuntimeURI = missionRuntimeURI;
    }

    @Override
    public void close() {
        if (fileLogger != null) {
            fileLogger.close();
        }
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
        if (enableLogging && fileLogger == null) {
            fileLogger = new FileLogger(DirectoryUtils.getLogFile(logDirectory, fileName, ".txt").getAbsolutePath());
        }
    }

    @Override
    public void log(IAction action) {
        if (enableLogging) {
            if (missionRuntimeURI != null) {
                action.addContext(ContextConstants.mission, missionRuntimeURI);
            }
            fileLogger.log(action);
            fileLogger.flush();
        }
    }

    @Override
    public void log(String username, String source, IAction action) {
        log(action);
    }

    @Override
    public void setMissionRuntimeURI(String missionRuntimeURI) {
        this.missionRuntimeURI = missionRuntimeURI;
    }

    @Override
    public String getMissionRuntimeURI() {
        return this.missionRuntimeURI;
    }
}
