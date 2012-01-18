package eu.scy.agents.agenda.guidance.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MissionModel {

	private final String name;
	
	private final String user;
	
	private final Set<Activity> activities = new HashSet<Activity>();
	
	private final Set<Dependency> dependencies = new HashSet<Dependency>();

	
	public MissionModel(String name, String user) {
		this.name = name;
		this.user = user;
	}

	public String getName() {
		return this.name;
	}

	public String getUser() {
		return this.user;
	}
	
	public Activity getActivity(String eloUri) {
		for(Activity act : this.activities) {
			if(act.getEloUri().equals(eloUri)) {
				return act;
			}
		}
		return null;
	}
	
	public List<Activity> getDependingActivities(Activity activity) {
		ArrayList<Activity> list = new ArrayList<Activity>(); 
		for(Dependency dep : this.dependencies) {
			if(dep.getFrom().equals(activity)) {
				list.add(dep.getTo());
			}
		}
		return list;
	}

	public void addActivity(Activity act) {
		this.activities.add(act);
	}
	
	public void addDependency(Dependency dep) {
		this.dependencies.add(dep);
	}
	public void addDependency(Activity from, Activity to) {
		addDependency(new Dependency(from, to));
	}
	
}
