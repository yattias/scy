package eu.scy.agents.agenda.guidance.model;

public class Activity {

	public enum ActivityState {
		ENABLED,
		MODIFIED,
		FINISHED,
		NEEDTOCHECK;
	}
	
	private ActivityState state;
	private String name;
	private String eloUri;
	private String firstVersionEloUri;
	private long latestModificationTime;

	
	public Activity(String name) {
		this(ActivityState.ENABLED, name, Long.MIN_VALUE);
	}
	
	public Activity(ActivityState state, String name, long latestModification) {
		setState(state);
		setName(name);
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

	public void setName(String name) {
		this.name = name;
	}

	public String getEloUri() {
		return this.eloUri;
	}

	public void setEloUri(String eloUri) {
		this.eloUri = eloUri;
	}

	public String getFirstVersionEloUri() {
		return this.firstVersionEloUri;
	}

	public void setFirstVersionEloUri(String firstVersionEloUri) {
		this.firstVersionEloUri = firstVersionEloUri;
	}

	public long getLatestModificationTime() {
		return this.latestModificationTime;
	}

	public void setLatestModificationTime(long latestModificationTime) {
		this.latestModificationTime = latestModificationTime;
	}
	
	@Override
	public boolean equals(Object that) {
	  if (that == null ) {
		  return false; 
	  }
	  if (this == that) {
		  return true;
	  }
	  // Two activities are considers equal, if both have the same ELO URI.
	  return ((Activity)that).getEloUri().equals(this.getEloUri());
	}
	
}
