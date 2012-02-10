package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.agenda.exception.InvalidMissionSpecificationException;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;
import eu.scy.agents.agenda.guidance.MissionModelBuilder;
import eu.scy.agents.agenda.guidance.RooloAccessor;
import eu.scy.agents.agenda.serialization.BasicMissionAnchorModel;
import eu.scy.agents.agenda.serialization.MissionSpecificationParser;
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
	
	private volatile ModelState state;
	private final String missionRuntimeUri;
	private final String user;
	private MissionModelBuilder umc;
	
	
	public MissionModel(RooloAccessor rooloAccessor, Session session, String missionRuntimeUri, String userName) {
		this.rooloAccessor = rooloAccessor;
		this.session = session;
		this.missionRuntimeUri = missionRuntimeUri;
		this.user = userName;
		this.state = ModelState.Enabled;
	}

	public String getMissionRuntimeUri() {
		return this.missionRuntimeUri;
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
		if(act.getTemplateUri() == null) {
			return;
		}
		this.template2Activity.put(act.getTemplateUri(), act);
		// we need to get informed if Activity's elo uri has been set
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
				this.umc = new MissionModelBuilder(this);
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
						getMissionRuntimeUri()));
				
				List<Activity> dependingActivities = getDependingActivities(activity);
				for(Activity act : dependingActivities) {
					// TODO perform changes on the depending activities
					// Maybe checks must be performed recursivly ... needs to be discussed
				}
			} catch (IllegalArgumentException e) {
				logger.warn(String.format("Received tuple for user '%s' and mission '%s' has the unknown type: '%s'.", 
						getUser(), 
						getMissionRuntimeUri(),
						type));
			}
		}
	}
	
	public void reconstruct() throws InvalidMissionSpecificationException, URISyntaxException, NoRepositoryAvailableException {
		String missionRuntimeURI = this.session.getMissionRuntimeURI(getUser());
		String missionId = this.session.getMissionId(missionRuntimeURI);
		String missionSpecificationURI = this.session.getMissionSpecification(missionRuntimeURI);

		createDependencyModel(missionRuntimeURI, missionId, missionSpecificationURI);		
	}
	
	private void createDependencyModel(String missionRuntimeURI, String missionId, String missionSpecUri) 
					throws InvalidMissionSpecificationException, URISyntaxException, NoRepositoryAvailableException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUser(), 
				getMissionRuntimeUri()));
		
		// Get the mission specification and create dependency model (which contains template URIs)
		
//		IELO missionSpec = this.rooloAccessor.getElo(missionSpecificationURI);
//		logger.debug("missionSpecURI: " + missionSpec.getUri());

		Document doc = readMissionSpecification();
		
		// MissionID -> MissionModel
		Map<String, BasicMissionAnchorModel> anchorMap = MissionSpecificationParser.parse(doc);
		
		// EloUri -> Activity
		Map<String, Activity> activityMap = new HashMap<String, Activity>();
		
		// create activities for all basic mission anchor models
		for(String anchorId : anchorMap.keySet()) {
			BasicMissionAnchorModel bmaModel = anchorMap.get(anchorId);
			Activity activity = new Activity(bmaModel.getId(), bmaModel.getUri());
			activityMap.put(bmaModel.getId(), activity);
		}
		
		// now create the dependencies between the activities
		for(String anchorId : anchorMap.keySet()) {
			BasicMissionAnchorModel bmaModel = anchorMap.get(anchorId);
			Activity dependent = activityMap.get(anchorId);
			for(String id : bmaModel.getDependingOnMissionIds()) {
				Activity depending = activityMap.get(id);
				addDependency(dependent, depending);
			}
		}

		// dependency model has just template URIs, so we'll add the concrete ELO URIs
		List<String> eloUris = this.rooloAccessor.getEloUrisForMissionRuntime(missionId, getUser());
		for(String eloUri : eloUris) {
			String templateEloUri = getTemplateEloUri(this.rooloAccessor, eloUri);
			Activity act = getActivityByTemplateUri(templateEloUri);
			if(act != null) {
				act.setEloUri(eloUri);
			}
		}
	}
	
	private Document readMissionSpecification() throws InvalidMissionSpecificationException {
		// TODO This is just for testing... don't blame me for that!
		try {
			return new SAXBuilder().build(MissionSpecificationParser.class.getResource("/pizzaMission.xml").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InvalidMissionSpecificationException("Error while reading mission specification!");
		}
	}
	
	private String getTemplateEloUri(RooloAccessor rooloAccessor, String eloUri) throws URISyntaxException, NoRepositoryAvailableException {
		IMetadata metadata =  rooloAccessor.getMetadata(eloUri);

		// Search for the IS_FORK_OF metadata key
		IMetadataValueContainer valueContainer = null;
		for(IMetadataKey key : metadata.getAllMetadataKeys()){
			if(key.getId().equals(CoreRooloMetadataKeyIds.IS_FORK_OF.getId())) {
				valueContainer = metadata.getMetadataValueContainer(key);
				break;
			}
		}
		if(valueContainer != null) {
			return getTemplateEloUri(rooloAccessor, valueContainer.getValue().toString());
		} 
		return eloUri;
	}
}
