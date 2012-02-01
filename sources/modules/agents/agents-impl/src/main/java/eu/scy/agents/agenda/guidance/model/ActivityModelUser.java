package eu.scy.agents.agenda.guidance.model;

import java.util.HashMap;
import java.util.Map;

public class ActivityModelUser {

	private final String name;
	private final Map<String, MissionModel> missions;
	
	public ActivityModelUser(String name) {
		super();
		this.name = name;
		this.missions = new HashMap<String, MissionModel>();
	}

	public String getName() {
		return this.name;
	}

	public MissionModel getMission(String mission) {
		return this.missions.get(mission);
	}

	public void addMission(MissionModel mission) {
		this.missions.put(mission.getName(), mission);
	}
}
