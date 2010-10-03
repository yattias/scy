package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IContext;


public class Context implements IContext{
	
	public static final String PATH = "context";
	
	private String tool;
	
	private String mission;
	
	private String session;
	
	private String eloURI;
	
	public Context() {}
	
	public Context(String tool, String mission, String session, String eloURI) {
		this.tool = tool;
		this.mission = mission;
		this.session = session;
		this.eloURI = eloURI;
	}
	
	/**
	 * @return the tool
	 */
	public String getTool() {
		return tool;
	}

	/**
	 * @param tool the tool to set
	 */
	public void setTool(String tool) {
		this.tool = tool;
	}

	/**
	 * @return the mission
	 */
	public String getMission() {
		return mission;
	}

	/**
	 * @param mission the mission to set
	 */
	public void setMission(String mission) {
		this.mission = mission;
	}

	/**
	 * @return the session
	 */
	public String getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(String session) {
		this.session = session;
	}

	public String getEloURI() {
		return eloURI;
	}
	
	public void setEloURI(String eloURI) {
		this.eloURI = eloURI;
	}
	
	
	/**
	 * 
	 * @param constant
	 * @return
	 */
	public String get(ContextConstants constant) {
		switch (constant) {
			case tool: return getTool();
			case mission: return getMission();
			case session: return getSession();
			case eloURI: return getEloURI();
			default: throw new IllegalArgumentException("Constant " + constant + " not known!");
		}
	}

	/**
	 * 
	 * @return
	 */
	public Enum<?>[] getEnum() {
		return ContextConstants.values();
	}

	public void set(ContextConstants constant, String value) {
		switch (constant) {
			case tool: setTool(value); break;
			case mission: setMission(value); break;
			case session: setSession(value); break;
			case eloURI: setEloURI(value); break;
			default: throw new IllegalArgumentException("Constant " + constant + " not known!");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mission == null) ? 0 : mission.hashCode());
		result = prime * result + ((session == null) ? 0 : session.hashCode());
		result = prime * result + ((tool == null) ? 0 : tool.hashCode());
		result = prime * result + ((eloURI == null) ? 0 : eloURI.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Context))
			return false;
		Context other = (Context) obj;
		if (mission == null) {
			if (other.mission != null)
				return false;
		} else if (!mission.equals(other.mission))
			return false;
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.equals(other.session))
			return false;
		if (tool == null) {
			if (other.tool != null)
				return false;
		} else if (!tool.equals(other.tool))
			return false;
		if (eloURI == null) {
			if (other.eloURI != null)
				return false;
		} else if (!eloURI.equals(other.eloURI))
			return false;
		return true;
	}
	
}
