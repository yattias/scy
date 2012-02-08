package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.scy.agents.agenda.guidance.MissionModelBuilder;
import eu.scy.agents.agenda.guidance.RooloAccessor;
import eu.scy.agents.session.Session;

public class MissionModel implements ActivityModelChangedListener {

	enum ModelState {
		Enabled, 
		Initializing, 
		Initialized;
	}
	
	private static final Logger logger = Logger.getLogger(MissionModel.class.getName());
	
	private final Map<String, Activity> userElo2Activity = new HashMap<String, Activity>();
	private final Map<String, Activity> template2Activity = new HashMap<String, Activity>();
	private final Set<Dependency> dependencies = new HashSet<Dependency>();
	private final Session session;
	private final RooloAccessor rooloAccessor;
	
	private final String name;
	private final String user;
	private volatile ModelState state = ModelState.Enabled;
	private MissionModelBuilder umc;
	
	
	public MissionModel(RooloAccessor rooloAccessor, Session session, String name, String user) {
		this.rooloAccessor = rooloAccessor;
		this.session = session;
		this.name = name;
		this.user = user;
	}

	public String getName() {
		return this.name;
	}

	public String getUser() {
		return this.user;
	}
	
	public void setInitialized() {
		synchronized (this) {
			this.state = ModelState.Initialized;
		}
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
			if(dep.getDependent().equals(activity)) {
				list.add(dep.getDepending());
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
	
	public void addDependency(Activity dependent, Activity depending) {
		addDependency(new Dependency(dependent, depending));
	}
	
	public void addDependency(Dependency dep) {
		this.dependencies.add(dep);
	}
	
	@Override
	public void modelChanged(ActivityModelChangedEvent event) {
		// a call of this method means that the elo URI has been set, so we have to update the map
		Activity act = (Activity)event.getSource();
		if(act.getEloUri() != null) {
			this.userElo2Activity.put(act.getEloUri(), act);
		}
	}
	
	public void processTuple(Tuple tuple) {
		synchronized(this) {
			switch(this.state) {
			case Enabled:
				this.state = ModelState.Initializing;
				this.umc = new MissionModelBuilder(this, this.rooloAccessor, this.session);
				new Thread(this.umc).start();
				break;
			case Initializing:
				this.umc.addTuple(tuple);
				break;
			case Initialized:
				// tidy up
				if(this.umc != null) {
					this.umc = null;
				}
				applyTupleToModel(tuple);
				break;
			}
		}
	}
	
	private void applyTupleToModel(Tuple tuple) {
		String type = tuple.getField(0).getValue().toString();
		String eloUri = tuple.getField(3).getValue().toString();
		long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());
		// change activity
		Activity activity = getActivityByUserElo(eloUri);
		if(timestamp > activity.getLatestModificationTime()) {
			try {
				Activity.ActivityState actState = Activity.ActivityState.valueOf(type);
				activity.setState(actState);
				activity.setLatestModificationTime(timestamp);
				logger.debug(String.format("Set state %s for activity %s (user: %s | mission: %s).", 
						type,
						eloUri, 
						getUser(), 
						getName()));
				
				List<Activity> dependingActivities = getDependingActivities(activity);
				for(Activity act : dependingActivities) {
					// TODO perform changes on the depending activities
					// Maybe checks must be performed recursivly ... needs to be discussed
				}
			} catch (IllegalArgumentException e) {
				logger.warn(String.format("Received tuple for user '%s' and mission '%s' has the unknown type: '%s'.", 
						getUser(), 
						getName(),
						type));
			}
		}
	}
}

