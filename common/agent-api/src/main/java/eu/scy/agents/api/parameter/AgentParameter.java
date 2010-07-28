package eu.scy.agents.api.parameter;

public class AgentParameter {

	private String mission;
	private String user;
	private String parameterName;
	private Object parameterValue;

	public AgentParameter() {
		mission = null;
		user = null;
		parameterName = null;
		parameterValue = null;
	}

	public String getMission() {
		return mission;
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getParameterValue() {
		return (T) parameterValue;
	}

	public <T> void setParameterValue(T parameterValue) {
		this.parameterValue = parameterValue;
	}

}
