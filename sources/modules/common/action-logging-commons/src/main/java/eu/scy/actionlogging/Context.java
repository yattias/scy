package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ContextConstants;

public class Context {
	
	public static final String PATH = "context";
	
	private String tool;
	
	private String mission;
	
	private String session;
	
	public Context() {}
	
	public Context(String tool, String mission, String session) {
		this.tool = tool;
		this.mission = mission;
		this.session = session;
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
			default: throw new IllegalArgumentException("Constant " + constant + " not known!");
		}
	}
	
}
