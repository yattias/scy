package eu.scy.agents.agenda.guidance.model;

import java.util.ArrayList;
import java.util.List;

public class Activity {

	public enum ActivityState {
		ENABLED,
		MODIFIED,
		FINISHED,
		NEEDTOCHECK;
	}
	
	private ActivityState state;
	private String name;
	private String firstVersionEloUri;
	private String currentEloUri;
	private long latestModificationTime;
	private final List<Activity> dependencies = new ArrayList<Activity>();
	
	public Activity(String id, String firstVersionEloUri) {
		this(ActivityState.ENABLED, id, firstVersionEloUri, Long.MIN_VALUE);
	}
	
	public Activity(ActivityState state, String name, String firstVersionEloUri, long latestModification) {
		setState(state);
		this.name = name;
		this.firstVersionEloUri = firstVersionEloUri;
		setLatestModificationTime(latestModification);
	}
	
	public ActivityState getState() {
		return this.state;
	}

	public void setState(ActivityState state) {
		this.state = state;
	}
	
	public String getName() {
		return this.name;
	}

	public String getFirstVersionEloUri() {
		return this.firstVersionEloUri;
	}

	public long getLatestModificationTime() {
		return this.latestModificationTime;
	}

	public void setLatestModificationTime(long latestModificationTime) {
		this.latestModificationTime = latestModificationTime;
	}
	
	public String getCurrentEloUri() {
		return this.currentEloUri;
	}
	
	public void setCurrentEloUri(String currentEloUri) {
		this.currentEloUri = currentEloUri;
	}

	public List<Activity> getDependencies() {
		return this.dependencies;
	}
	
	
//	@Override
//	public boolean equals(Object that) {
//	  if (that == null ) {
//		  return false; 
//	  }
//	  if (this == that) {
//		  return true;
//	  }
//	  // Two activities are considers equal, if both have the same first version ELO URI.
//	  return ((Activity)that).getFirstVersionEloUri().equals(this.getFirstVersionEloUri());
//	}
	
}
