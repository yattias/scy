package eu.scy.agents.authoring.workflow;

import java.io.Serializable;
import java.util.HashMap;

public class Workflow implements Serializable {

	private static final long serialVersionUID = 4962962803749645958L;

	private HashMap<String, WorkflowItem> workflowItems;

	public Workflow() {
		workflowItems = new HashMap<String, WorkflowItem>();
	}

	public WorkflowItem getItem(String id) {
		return workflowItems.get(id);
	}

	public void addWorkflowItem(WorkflowItem item) {
		workflowItems.put(item.getId(), item);
	}

	@Override
	public String toString() {
		return "Workflow [workflowItems=" + workflowItems + "]";
	}

}
