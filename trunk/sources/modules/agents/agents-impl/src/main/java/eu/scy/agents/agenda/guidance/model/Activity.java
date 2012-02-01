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
	private String templateEloUri;
	private long latestModificationTime;
	// we only need one listener, because one activity can be bind to only one mission
	private ActivityModelChangedListener changelistener;

	
	public Activity(String name, String basicEloUri) {
		this(State.Enabled, name, basicEloUri, Long.MIN_VALUE);
	}
	
	public Activity(State state, String name, String basicEloUri, long latestModification) {
		setState(state);
		setName(name);
		setTemplateUri(basicEloUri);
		setLatestModificationTime(latestModification);
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
		if(this.changelistener != null) {
			this.changelistener.modelChanged(new ActivityModelChangedEvent(this));
		}
	}

	public String getTemplateUri() {
		return this.templateEloUri;
	}

	public void setTemplateUri(String basicEloUri) {
		this.templateEloUri = basicEloUri;
		if(this.changelistener != null) {
			this.changelistener.modelChanged(new ActivityModelChangedEvent(this));
		}
	}

	public long getLatestModificationTime() {
		return this.latestModificationTime;
	}

	public void setLatestModificationTime(long latestModificationTime) {
		this.latestModificationTime = latestModificationTime;
	}
	
	public void setActivityModelChangedListener(ActivityModelChangedListener listener) {
		this.changelistener = listener;
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
