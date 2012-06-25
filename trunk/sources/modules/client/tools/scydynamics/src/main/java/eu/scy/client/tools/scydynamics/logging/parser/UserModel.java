package eu.scy.client.tools.scydynamics.logging.parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;

public class UserModel {

	public static final String STATISTICS_TEMPLATE = "name, actions[#], duration[minutes], phase_changes[#], feedback_requested[#], model_ran[#], model_ran_error[#]";
	private String userName;
	// actions are organized by timestamp in this treemap
	private TreeMap<Long, IAction> actions;
	private TreeMap<Long, IAction> backupActions;
	private LinkedList<IAction> nextPhaseActions;
	private LinkedList<IAction> feedbackActions;
	private boolean statisticsValid;
	private LinkedList<IAction> modelRanActions;
	private LinkedList<IAction> modelRanErrorActions;

	public UserModel(String userName) {
		this.userName = userName;
		actions = new TreeMap<Long, IAction>();
		backupActions = new TreeMap<Long, IAction>();
		nextPhaseActions = new LinkedList<IAction>();
		feedbackActions = new LinkedList<IAction>();
		modelRanActions = new LinkedList<IAction>();
		modelRanErrorActions = new LinkedList<IAction>();
		statisticsValid = false;
	}

	public IAction getLastAction() {
		return actions.lastEntry().getValue();
	}

	public IAction getFirstAction() {
		return actions.firstEntry().getValue();
	}

	public void releaseFilters() {
		actions = (TreeMap<Long, IAction>) backupActions.clone();
		statisticsValid = false;
	}

	public void filterForTime(long start, long end) {
		TreeMap<Long, IAction> actionsClone = (TreeMap<Long, IAction>) actions.clone();
		for (long timestamp : actionsClone.keySet()) {
			if (timestamp < start || timestamp > end) {
				actions.remove(timestamp);
			}
		}
		statisticsValid = false;
	}

	public void addAction(IAction newAction) {
		if (userName.equals(newAction.getUser())) {
			this.actions.put(newAction.getTimeInMillis(), newAction);
			statisticsValid = false;
		} else {
			System.out.println("tried to add action to wrong user (expected: " + userName + ", received " + newAction.getUser() + "; ignoring.");
		}
	}

	public String getUserName() {
		return this.userName;
	}

	private long getDurationMinutes() {
		long duration = -99;
		try {
			duration = actions.get(actions.lastKey()).getTimeInMillis() - actions.get(actions.firstKey()).getTimeInMillis();
			duration = duration / 60000;
		} catch (Exception e) {
			System.out.println("exception caught in UserModel.getDuration: " + e.getMessage());
		}
		return duration;
	}

	@Override
	public String toString() {
		if (!statisticsValid) {
			calculateStatistics();
		}
		String returnString;
		returnString = userName + ", " + actions.size() + ", " + getDurationMinutes() + ", " + nextPhaseActions.size() +", "+feedbackActions.size()+", "+modelRanActions.size()+", "+modelRanErrorActions.size();
		return returnString;
	}

	private void calculateStatistics() {
		nextPhaseActions = new LinkedList<IAction>();
		for (IAction action : actions.values()) {
			if (action.getType().equals(ModellingLogger.NEXT_PHASE)) {
				nextPhaseActions.add(action);
			}

			if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
				addFeedback(action);
			}
			
			if (action.getType().equals(ModellingLogger.MODEL_RAN)) {
				modelRanActions.add(action);
			}
			
			if (action.getType().equals(ModellingLogger.MODEL_RAN_ERROR)) {
				modelRanErrorActions.add(action);
			}
		}
		statisticsValid = true;
	}

	private void addFeedback(IAction action) {
		if (feedbackActions.isEmpty()) {
			// this is the first feedback actions, simply add
			feedbackActions.add(action);
		} else {
			if (meaningfulActionsBetween(feedbackActions.getLast(), action)) {
				feedbackActions.add(action);
			} else {
				// do nothing
			}
		}
	}

	private boolean meaningfulActionsBetween(IAction firstAction, IAction lastAction) {
		SortedMap<Long, IAction> betweenActions = actions.subMap(firstAction.getTimeInMillis(), lastAction.getTimeInMillis());
		System.out.print("meaningful/feedback for "+this.userName+": checking "+betweenActions.size()+" between "+firstAction.getType()+" and "+lastAction.getType());
		for (IAction action : betweenActions.values()) {
			if (meaningfulAction(action)) {
				System.out.println(" -> true");
				return true;
			} else {
				// no meaningful action found yet, continue for-loop
			}
		}
		// no meaningful action found at all, return false
		System.out.println(" -> false");
		return false;
	}

	private boolean meaningfulAction(IAction action) {
		// meaningful actions are those actions that change the model
		// this is a little bit dangerous, because the first conditions are not type-safe
		if (action.getType().endsWith("_added") ||
				action.getType().endsWith("_deleted") ||
				action.getType().equals(ModellingLogger.SPECIFICATION_CHANGED) ||
				action.getType().equals(ModellingLogger.ELEMENT_RENAMED) ||
				action.getType().equals(ModellingLogger.MODEL_LOADED) ||
				action.getType().equals(ModellingLogger.MODEL_CLEARED)) {
			return true;
		} else {
			return false;
		}
	}

	public Collection<IAction> getActions() {
		return this.actions.values();
	}

	public void setBackupPoint() {
		backupActions = (TreeMap<Long, IAction>) actions.clone();
	}
}
