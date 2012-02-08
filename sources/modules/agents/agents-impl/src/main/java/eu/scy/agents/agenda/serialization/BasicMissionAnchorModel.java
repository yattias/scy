package eu.scy.agents.agenda.serialization;

import java.util.ArrayList;
import java.util.List;

public class BasicMissionAnchorModel {

	private String id;
	private String uri;
	private List<String> dependingOnMissionIds;
	
	BasicMissionAnchorModel() {
		this.dependingOnMissionIds  = new ArrayList<String>();
	}

	public String getId() {
		return this.id;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	void setUri(String uri) {
		this.uri = uri;
	}
	
	public List<String> getDependingOnMissionIds() {
		return this.dependingOnMissionIds;
	}
	
	@Override
	public String toString() {
		return String.format("[ ID: %s | URI: %s | DependingOn: %s ]", id, uri, dependingOnMissionIds.toString());
	}
}

