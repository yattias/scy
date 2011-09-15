package eu.scy.client.desktop.localtoolbroker;

import eu.scy.actionlogging.api.ActionLoggedEventListener;
import java.io.Closeable;

import eu.scy.actionlogging.FileLogger;
import eu.scy.actionlogging.api.ActionLoggedEvent;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * @author bollen, sikken
 */
public class LocalFileActionLogger implements IActionLogger, Closeable {

    private static final String fileName = "actions";
    private String logDirectory;
    private FileLogger fileLogger = null;
    private boolean enableLogging = false;
    private List<ActionLoggedEventListener> actionLoggedEventListeners = new CopyOnWriteArrayList<ActionLoggedEventListener>();

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
        sendActionLoggedEvent(action);
    }

    @Override
    public void log(String username, String source, IAction action) {
        log(action);
    }

   @Override
   public void addActionLoggedEventListener(ActionLoggedEventListener actionLoggedEventListener)
   {
      if (!actionLoggedEventListeners.contains(actionLoggedEventListener)){
         actionLoggedEventListeners.add(actionLoggedEventListener);
      }
   }

   @Override
   public void removeActionLoggedEventListener(ActionLoggedEventListener actionLoggedEventListener)
   {
      actionLoggedEventListeners.remove(actionLoggedEventListener);
   }

   private void sendActionLoggedEvent(IAction action){
      if (!actionLoggedEventListeners.isEmpty()){
         ActionLoggedEvent actionLoggedEvent = new ActionLoggedEvent(action);
         for (ActionLoggedEventListener actionLoggedEventListener: actionLoggedEventListeners){
            actionLoggedEventListener.actionLogged(actionLoggedEvent);
         }
      }
   }

}
