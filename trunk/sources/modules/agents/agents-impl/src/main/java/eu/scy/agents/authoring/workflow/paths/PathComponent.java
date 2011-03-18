package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Timer;

public class PathComponent {

	private WorkflowItem workflowItem;

	private long startTime = 0;

	private long elapsedTime = 0;

	private Timer timer;

	PathComponent(Timer timer, WorkflowItem workflowItem) {
		this.workflowItem = workflowItem;
		this.timer = timer;
	}

	/**
	 * Start timing how long this path component is visited.
	 */
	public void startTiming() {
		startTime = timer.currentTimeMillis();
	}

	/**
	 * End timing how long this path component is visited.
	 */
	public void endTiming() {
		elapsedTime = timer.currentTimeMillis() - startTime;
	}

	/**
	 * Get the time spent in this pathcomponent.
	 * 
	 * @return The time spent in this pathcomponent.
	 */
	public long getTimeSpent() {
		if (elapsedTime == 0) {
			if (startTime > 0) {
				// still running
				return System.currentTimeMillis() - startTime;
			}
		}
		return elapsedTime;
	}

	public WorkflowItem getWorkflowItem() {
		return workflowItem;
	}

	public String getWorkflowItemId() {
		return workflowItem.getId();
	}

}
