package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Duration;

public class TimeExcess {

	private WorkflowItem item;

	private Duration timeExcess;

	public WorkflowItem getItem() {
		return item;
	}

	public void setItem(WorkflowItem item) {
		this.item = item;
	}

	public Duration getTimeExcess() {
		return timeExcess;
	}

	public void setTimeExcess(Duration timeExcess) {
		this.timeExcess = timeExcess;
	}

}
