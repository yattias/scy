package eu.scy.agents.agenda.guidance.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MissionModel implements ActivityModelChangedListener {

	private final String name;
	
	private final String user;
	
	private final Map<String, Activity> userElo2Activity = new HashMap<String, Activity>();

	private final Map<String, Activity> template2Activity = new HashMap<String, Activity>();
	
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
	
	public Activity getActivityByUserElo(String eloUri) {
		return this.userElo2Activity.get(eloUri);
	}
	
	public Activity getActivityByTemplateUri(String templateUri) {
		return this.template2Activity.get(templateUri);
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
		if(act.getEloUri() == null && act.getTemplateUri() == null) {
			return;
		}
		if(act.getTemplateUri() != null) {
			this.template2Activity.put(act.getTemplateUri(), act);
		}
		// we need to get informed if Activity's elo uri or template uri has been changed
		act.setActivityModelChangedListener(this);
	}
	
	public void addDependency(Activity from, Activity to) {
		addDependency(new Dependency(from, to));
	}

	@Override
	public void modelChanged(ActivityModelChangedEvent event) {
		// a call of this method means that the elo URI has been set, so we have to update the map
		Activity act = (Activity)event.getSource();
		if(act.getEloUri() != null) {
			this.userElo2Activity.put(act.getEloUri(), act);
		}
	}
	
	public void addDependency(Dependency dep) {
		this.dependencies.add(dep);
	}
	
}

