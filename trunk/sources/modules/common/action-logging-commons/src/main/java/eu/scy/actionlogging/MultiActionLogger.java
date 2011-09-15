package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ActionLoggedEvent;
import eu.scy.actionlogging.api.ActionLoggedEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is a multiplexer of action loggers. Other action loggers can register and deregister
 * here and each action will be logged to all loggers.
 * 
 * @author Stefan Weinbrenner
 * 
 */
public class MultiActionLogger implements IActionLogger {

    private List<ActionLoggedEventListener> actionLoggedEventListeners = new CopyOnWriteArrayList<ActionLoggedEventListener>();
    private Lock lock;

    private List<IActionLogger> loggers;

    public MultiActionLogger(IActionLogger... loggers) {
        this(new ArrayList<IActionLogger>(Arrays.asList(loggers)));
    }
    public MultiActionLogger(List<IActionLogger> loggers) {
        this.loggers = loggers;
        this.lock = new ReentrantLock();
    }

    public List<IActionLogger> getLoggers() {
        lock.lock();
        try {
            return new ArrayList<IActionLogger>(loggers);
        } finally {
            lock.unlock();
        }
    }
    
    public void addLogger(IActionLogger logger) {
        lock.lock();
        try {
            loggers.add(logger);
        } finally {
            lock.unlock();
        }
    }

    public void removeLogger(IActionLogger logger) {
        lock.lock();
        try {
            loggers.remove(logger);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void log(IAction action) {
        lock.lock();
        try {
            for (IActionLogger logger : loggers) {
                logger.log(action);
            }
        } finally {
            lock.unlock();
        }
        sendActionLoggedEvent(action);
    }

    @Override
    @Deprecated
    public void log(String username, String source, IAction action) {
        lock.lock();
        try {
            for (IActionLogger logger : loggers) {
                logger.log(username, source, action);
            }
        } finally {
            lock.unlock();
        }
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
