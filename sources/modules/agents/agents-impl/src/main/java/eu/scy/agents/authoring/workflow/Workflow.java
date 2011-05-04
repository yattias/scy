package eu.scy.agents.authoring.workflow;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Workflow implements Serializable, Iterable<WorkflowItem> {

	private static final long serialVersionUID = 4962962803749645958L;

	private Map<String, WorkflowItem> workflowItems;

	public Workflow() {
		workflowItems = new LinkedHashMap<String, WorkflowItem>();
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

	@Override
	public Iterator<WorkflowItem> iterator() {
		return workflowItems.values().iterator();
	}

}
