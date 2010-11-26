package eu.scy.agents.api.parameter;

import java.net.URI;

public class AgentParameter {

	private String mission;
	private String user;
	private String parameterName;
	private Object parameterValue;

	private String las;
	private URI eloUri;

	public AgentParameter() {
		mission = null;
		user = null;
		parameterName = null;
		parameterValue = null;
	}

	public AgentParameter(String mission, String parameterName) {
		this.mission = mission;
		this.user = null;
		this.parameterName = parameterName;
		this.parameterValue = null;
	}

	public AgentParameter(String mission, String user, String parameterName) {
		this.mission = mission;
		this.user = user;
		this.parameterName = parameterName;
		this.parameterValue = null;
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

	public String getLas() {
		return las;
	}

	public void setLas(String las) {
		this.las = las;
	}

	public URI getELOUri() {
		return eloUri;
	}

	public void setELOUri(URI uri) {
		eloUri = uri;
	}
}
