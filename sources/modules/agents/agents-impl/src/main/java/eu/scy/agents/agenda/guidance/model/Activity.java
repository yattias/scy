package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;

import java.util.ArrayList;
import java.util.List;

import eu.scy.agents.agenda.exception.InvalidActivityTupleException;

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
	
	public static final String FIELD_ACTIVITY = "activity";
	
	private ActivityState state;
	private String anchorId;
	private String eloTitle;
	private String currentEloUri;
	private boolean displayInCurtain;
	private long lastModificationTime;
	private String firstVersionEloUri;
	private final List<Activity> successors = new ArrayList<Activity>();
	private final List<Activity> predecessors = new ArrayList<Activity>();
	
	public Activity(String anchorId, String firstVersionEloUri) {
		this(ActivityState.ENABLED, anchorId, firstVersionEloUri, 0);
	}
	
	public Activity(ActivityState state, String anchorId, String firstVersionEloUri, long latestModification) {
		setState(state);
		this.anchorId = anchorId;
		this.firstVersionEloUri = firstVersionEloUri;
		setLastModificationTime(latestModification);
		setCurrentEloUri(firstVersionEloUri);
		setDisplayedInCurtain(false);
	}
	
	public boolean isDisplayedInCurtain() {
		return this.displayInCurtain;
	}

	public void setDisplayedInCurtain(boolean displayInCurtain) {
		this.displayInCurtain = displayInCurtain;
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
	
	/**
	 * Load data of this activity from the tuple.
	 *
	 * @param tuple the tuple
	 * @return true, if tuple was loaded. If last modification is too old, returns false
	 * @throws InvalidActivityTupleException the invalid activity tuple exception
	 */
	public boolean loadFromTuple(Tuple tuple) throws InvalidActivityTupleException {
		if(tuple.getFields().length != 9) {
			throw new InvalidActivityTupleException(
					"Invalid signature - tuple must have 9 fields, but has " + tuple.getFields().length);
		}
		if(!tuple.getField(0).getValue().toString().equals(FIELD_ACTIVITY)) {
			throw new InvalidActivityTupleException("Invalid signature - first field must be 'activity'");
		}
		if(!tuple.getField(3).getValue().toString().equals(this.anchorId)) {
			throw new InvalidActivityTupleException(String.format(
					"Invalid signature - anchor IDs do not match ( %s != %s )",
					tuple.getField(3).getValue().toString(),
					this.anchorId));
		}
		String stateString = tuple.getField(4).getValue().toString();
		String eloTitle = tuple.getField(5).getValue().toString();
		String currentEloUri = tuple.getField(6).getValue().toString();
		String timeStampString = tuple.getField(7).getValue().toString();
		String displayInCurtainString = tuple.getField(8).getValue().toString();
		try {
			ActivityState state = ActivityState.valueOf(stateString);
			Long lastModificationTimestamp = Long.valueOf(timeStampString);
			if(lastModificationTimestamp >= this.lastModificationTime) {
				// only save newer timestamps
				this.displayInCurtain = displayInCurtainString.equals("true");
				this.eloTitle = (eloTitle.equals("")) ? null : eloTitle;
				this.state = state;
				this.currentEloUri = currentEloUri;
				this.lastModificationTime = lastModificationTimestamp;
				return true;
			}
			return false;
			
		} catch (NumberFormatException e) {
			throw new InvalidActivityTupleException("Invalid signature - invalid timestamp: " + timeStampString);
		} catch (IllegalArgumentException e) {
			throw new InvalidActivityTupleException("Invalid signature - invalid state: " + stateString);
		}
	}
	
	/**
	 * Returns a tuple representation of this activity.
	 * ( "activity":String, <UserName>:String, <MissionRuntime>:String, <anchorId>:String, <state>:String, 
	 * <eloTitle>:String, <currentEloUri>:String, <lastModificationTimestamp>:Long, <displayInCurtain>:String )
	 *
	 * @param userName the user name
	 * @param missionRuntime the mission runtime
	 * @return the tuple
	 */
	public Tuple toTuple(String userName, String missionRuntime) {
		Tuple t = new Tuple(FIELD_ACTIVITY);
		t.add(userName);
		t.add(missionRuntime);
		t.add(this.anchorId);
		t.add(this.state.toString());
		String title = (this.eloTitle == null) ? "" : this.eloTitle;
		t.add(title);
		t.add(this.currentEloUri);
		t.add(this.lastModificationTime);
		t.add(String.valueOf(this.displayInCurtain));
		return t;
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
