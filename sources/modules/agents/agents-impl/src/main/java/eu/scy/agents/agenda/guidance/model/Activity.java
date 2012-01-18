package eu.scy.agents.agenda.guidance.model;

public class Activity {

	public enum State {
		Enabled,
		Modified,
		Finished;
	}
	
	private State state;
	
	private String name;
	
	private String eloUri;
	
	private long latestModification;

	
	public Activity(String name, String eloUri, long latestModification) {
		this(State.Enabled, name, eloUri, latestModification);
	}
	
	public Activity(State state, String name, String eloUri, long latestModification) {
		setState(state);
		setName(name);
		setEloUri(eloUri);
		setLatestModification(latestModification);
	}
	
	public State getState() {
		return this.state;
	}

	public void setState(State state) {
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

	public long getLatestModification() {
		return this.latestModification;
	}

	public void setLatestModification(long latestModification) {
		this.latestModification = latestModification;
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
