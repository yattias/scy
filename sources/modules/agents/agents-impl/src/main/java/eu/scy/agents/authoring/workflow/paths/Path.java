package eu.scy.agents.authoring.workflow.paths;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.DefaultTimer;
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
			path.get(path.size() - 1).endTiming();
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

	public long timeSpentInItem(WorkflowItem item) {
		long timeSpent = 0;
		for (PathComponent pathComponent : path) {
			if (pathComponent.getWorkflowItemId().equals(item.getId())) {
				timeSpent += pathComponent.getTimeSpent();
			}
		}
		return timeSpent;
	}
}
