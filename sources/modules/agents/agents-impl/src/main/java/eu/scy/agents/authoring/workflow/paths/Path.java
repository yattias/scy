package eu.scy.agents.authoring.workflow.paths;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.DefaultTimer;
import eu.scy.agents.util.time.Duration;
import eu.scy.agents.util.time.Timer;

public class Path implements Iterable<PathComponent> {

	private List<PathComponent> path;
	private Timer timer;

	public Path() {
		path = new LinkedList<PathComponent>();
		timer = new DefaultTimer();
	}

	public void addPathComponent(WorkflowItem item) {
		if (!path.isEmpty()) {
			PathComponent lastPathComponent = path.get(path.size() - 1);
			if (lastPathComponent.getWorkflowItemId().equals(item.getId())) {
				lastPathComponent.startTiming();
				return;
			} else {
				lastPathComponent.endTiming();
			}
		}
		PathComponent pathComponent = new PathComponent(timer, item);
		pathComponent.startTiming();
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

	public Duration timeSpentInItem(WorkflowItem item) {
		Duration timeSpent = new Duration();
		for (PathComponent pathComponent : path) {
			if (pathComponent.getWorkflowItemId().equals(item.getId())) {
				timeSpent = timeSpent.add(pathComponent.getTimeSpent());
			}
		}
		return timeSpent;
	}

	public void stopTiming() {
		if (!path.isEmpty()) {
			path.get(path.size() - 1).endTiming();
		}
	}

	@Override
	public String toString() {
		return path.toString();
	}
}
