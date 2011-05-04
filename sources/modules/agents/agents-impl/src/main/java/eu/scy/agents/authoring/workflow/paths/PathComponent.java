package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Duration;
import eu.scy.agents.util.time.Timer;

public class PathComponent {

	private WorkflowItem workflowItem;

	private long startTime = 0;

	private Duration elapsedTime = new Duration();

	private Timer timer;

	private boolean isTiming;

	PathComponent(Timer timer, WorkflowItem workflowItem) {
		this.workflowItem = workflowItem;
		this.timer = timer;
	}

	/**
	 * Start timing how long this path component is visited.
	 */
	public synchronized void startTiming() {
		startTime = timer.currentTimeMillis();
		isTiming = true;
	}

	/**
	 * End timing how long this path component is visited.
	 */
	public synchronized void endTiming() {
		if (isTiming) {
			elapsedTime = elapsedTime.add(startTime, timer.currentTimeMillis());
			isTiming = false;
		}
	}

	/**
	 * Get the time spent in this pathcomponent.
	 * 
	 * @return The time spent in this pathcomponent.
	 */
	public synchronized Duration getTimeSpent() {
		if (isTiming) {
			return elapsedTime.add(startTime, timer.currentTimeMillis());
		}
		return elapsedTime;
	}

	public WorkflowItem getWorkflowItem() {
		return workflowItem;
	}

	public String getWorkflowItemId() {
		return workflowItem.getId();
	}

	@Override
	public String toString() {
		return "(" + workflowItem.getId() + "," + elapsedTime + ")";
	}

}
