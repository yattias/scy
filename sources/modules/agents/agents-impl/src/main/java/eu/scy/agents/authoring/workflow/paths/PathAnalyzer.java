package eu.scy.agents.authoring.workflow.paths;

import java.util.ArrayList;
import java.util.List;

import eu.scy.agents.authoring.workflow.Workflow;
import eu.scy.agents.authoring.workflow.WorkflowItem;
import eu.scy.agents.util.time.Duration;

public class PathAnalyzer {

	private Workflow workflow;

	public PathAnalyzer() {
		this(null);
	}

	public PathAnalyzer(Workflow workflow) {
		this.workflow = workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	/**
	 * Returns the workflow items i.e. LASes in the path that exceed the given
	 * working time.
	 * 
	 * @param path
	 *            The path the user traveled through the lasses.
	 * @return A List of workflowItems that exceed the given time.
	 */
	public List<TimeExcess> getTimeExcesses(Path path) {
		List<TimeExcess> exceededItems = new ArrayList<TimeExcess>();
		for (WorkflowItem item : workflow) {
			Duration timeSpentInItem = path.timeSpentInItem(item);
			Duration expectedTime = item.getExpectedTime();
			if (timeSpentInItem.greater(expectedTime)) {
				TimeExcess timeExcess = new TimeExcess();
				timeExcess.setItem(item);
				timeExcess.setTimeExcess(expectedTime
						.difference(timeSpentInItem));
				exceededItems.add(timeExcess);
			}
		}
		return exceededItems;
	}
}
