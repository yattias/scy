package eu.scy.client.desktop.localtoolbroker;

import java.io.Closeable;

import eu.scy.actionlogging.FileLogger;
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

    public LocalFileActionLogger(String logDirectory) {
        this.logDirectory = logDirectory;
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
            fileLogger.log(action);
            fileLogger.flush();
        }
    }


}
