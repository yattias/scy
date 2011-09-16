package eu.scy.actionlogging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

/**
 * This class is a multiplexer of action loggers. Other action loggers can register and deregister
 * here and each action will be logged to all loggers.
 * 
 * @author Stefan Weinbrenner
 * 
 */
public class MultiActionLogger implements IActionLogger {

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
    }

}
