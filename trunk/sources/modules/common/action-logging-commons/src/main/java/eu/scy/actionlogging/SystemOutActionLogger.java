package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ActionLoggedEvent;
import eu.scy.actionlogging.api.ActionLoggedEventListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This IActionLogger logs to the console (useful for testing and debugging.
 * Makes use of the helper class ActionXMLTransformer.
 * @see ActionXMLTransformer
 * @author lars
 *
 */
public class SystemOutActionLogger implements IActionLogger {

    private OutputFormat format;
    private XMLWriter writer;
    private List<ActionLoggedEventListener> actionLoggedEventListeners = new CopyOnWriteArrayList<ActionLoggedEventListener>();

    public SystemOutActionLogger() {
        this.format = OutputFormat.createPrettyPrint();
        try {
            writer = new XMLWriter(System.out, format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(IAction action) {
        // logging to the console
        try {
            writer.write(new ActionXMLTransformer(action).getActionAsElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
