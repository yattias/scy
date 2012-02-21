package eu.scy.agents.agenda.guidance.model;

import java.util.ArrayList;
import java.util.List;

public class Activity {

	public enum ActivityState {
		ENABLED("ACTIVATED"),
		MODIFIED("ACTIVATED"),
		FINISHED("COMPLETED"),
		NEEDTOCHECK("NEED2CHECK");
		
		private String text;
		
		private ActivityState(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
	}
	
	private ActivityState state;
	private String anchorId;
	private String eloTitle;
	private String currentEloUri;
	private long lastModificationTime;
	private final String firstVersionEloUri;
	private final List<Activity> successors = new ArrayList<Activity>();
	private final List<Activity> predecessors = new ArrayList<Activity>();
	
	public Activity(String anchorId, String firstVersionEloUri) {
		this(ActivityState.ENABLED, anchorId, firstVersionEloUri, Long.MIN_VALUE);
	}
	
	public Activity(ActivityState state, String anchorId, String firstVersionEloUri, long latestModification) {
		setState(state);
		this.anchorId = anchorId;
		this.firstVersionEloUri = firstVersionEloUri;
		setLastModificationTime(latestModification);
		setCurrentEloUri(firstVersionEloUri);
	}
	
	public ActivityState getState() {
		return this.state;
	}

	public void setState(ActivityState state) {
		this.state = state;
	}
	
	public String getAnchorId() {
		return this.anchorId;
	}

	public String getEloTitle() {
		return this.eloTitle;
	}

	public void setEloTitle(String eloTitle) {
		this.eloTitle = eloTitle;
	}

	public String getFirstVersionEloUri() {
		return this.firstVersionEloUri;
	}

	public long getLastModificationTime() {
		return this.lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	
	public String getCurrentEloUri() {
		return this.currentEloUri;
	}
	
	public void setCurrentEloUri(String currentEloUri) {
		this.currentEloUri = currentEloUri;
	}

	/**
	 * Gets the successors of this activity.
	 * That are those activities on which this activity depends on.
	 * this -> successors
	 *
	 * @return the dependencies
	 */
	public List<Activity> getSuccessors() {
		return this.successors;
	}
	
	/**
	 * Gets the predecessors of this activity.
	 * That are those activities that depend on this activity.
	 * predecessors -> this
	 *
	 * @return the depending on activities
	 */
	public List<Activity> getPredecessors() {
		return this.predecessors;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ Anchor ID: ");
		sb.append(this.anchorId);
		sb.append(" | State: ");
		sb.append(this.state);
		sb.append(" | Last Modification: ");
		sb.append(this.lastModificationTime);
		sb.append(" | Predecessors: ");
		for(Activity dependency : this.predecessors) {
			sb.append("'");
			sb.append(dependency.getAnchorId());
			sb.append("' ");
		}
		sb.append("| Successors: ");
		for(Activity dependency : this.successors) {
			sb.append("'");
			sb.append(dependency.getAnchorId());
			sb.append("' ");
		}
		sb.append("]");
		return sb.toString();
	}
}
