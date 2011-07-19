package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.DefaultTimer;
import eu.scy.agents.util.time.Duration;
import eu.scy.agents.util.time.Timer;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Path implements Iterable<PathComponent> {

    private List<PathComponent> path;
    private Timer timer;

    public Path() {
        path = new CopyOnWriteArrayList<PathComponent>();
        timer = new DefaultTimer();
    }

    public synchronized void addPathComponent(WorkflowItem item) {
        if ( !path.isEmpty() ) {
            PathComponent lastPathComponent = path.get(path.size() - 1);
            if ( lastPathComponent.getWorkflowItemId().equals(item.getId()) ) {
                //                lastPathComponent.startTiming();
                return;
            } else {
                lastPathComponent.endTiming();
            }
        }
        PathComponent pathComponent = new PathComponent(timer, item);
        pathComponent.startTiming();
        path.add(pathComponent);
    }

    public void addPathComponent(WorkflowItem item, long timeStamp) {
        if ( !path.isEmpty() ) {
            PathComponent lastPathComponent = path.get(path.size() - 1);
            if ( lastPathComponent.getWorkflowItemId().equals(item.getId()) ) {
                lastPathComponent.endTiming(timeStamp);
                lastPathComponent.startTiming(timeStamp);
                return;
            } else {
                lastPathComponent.endTiming(timeStamp);
            }
        }
        PathComponent pathComponent = new PathComponent(timer, item, timeStamp);
        pathComponent.startTiming(timeStamp);
        path.add(pathComponent);
    }

    public void clearPath() {
        path.clear();
    }

    @Override
    public Iterator<PathComponent> iterator() {
        return path.iterator();
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public synchronized Duration timeSpentInItem(WorkflowItem item) {
        Duration timeSpent = new Duration();
        for ( PathComponent pathComponent : path ) {
            if ( pathComponent.getWorkflowItemId().equals(item.getId()) ) {
                timeSpent = timeSpent.add(pathComponent.getTimeSpent());
            }
        }
        return timeSpent;
    }

    public void stopTiming() {
        if ( !path.isEmpty() ) {
            path.get(path.size() - 1).endTiming();
        }
    }

    public void stopTiming(long timeStamp) {
        if ( !path.isEmpty() ) {
            path.get(path.size() - 1).endTiming(timeStamp);
        }
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
