package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ActionLoggedEvent;
import eu.scy.actionlogging.api.ActionLoggedEventListener;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This IActionLogger simply accepts any IAction and does nothing with it.
 * @author lars
 *
 */
public class DevNullActionLogger implements IActionLogger {

    private List<ActionLoggedEventListener> actionLoggedEventListeners = new CopyOnWriteArrayList<ActionLoggedEventListener>();

    @Override
    public void log(IAction action) {
        // doing absolutely nothing here
       sendActionLoggedEvent(action);
    }

    @Override
    @Deprecated
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
