package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import roolo.elo.BasicELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.agenda.exception.AgentDoesNotRespondException;
import eu.scy.agents.agenda.exception.InvalidMissionSpecificationException;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.serialization.BasicMissionAnchorModel;
import eu.scy.agents.agenda.serialization.MissionSpecificationParser;
import eu.scy.agents.session.Session;

public class MissionModelBuilder implements Runnable {
	
	private static final Logger logger = Logger.getLogger(MissionModelBuilder.class.getName());

	private final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
	private final MissionModel missionModel;
	private final RooloAccessor rooloAccessor;
	private final Session session;
	
	public MissionModelBuilder(MissionModel missionModel, RooloAccessor rooloAccessor, Session session) {
		if(missionModel == null) {
			throw new IllegalArgumentException("missionModel can not be null");
		}
		if(rooloAccessor == null) {
			throw new IllegalArgumentException("rooloAccessor can not be null");
		}
		if(session == null) {
			throw new IllegalArgumentException("session can not be null");
		}
		this.missionModel = missionModel;
		this.rooloAccessor = rooloAccessor;
		this.session = session;
	}

	public void addTuple(Tuple t) {
		try {
			this.tupleQueue.put(t);
		} catch (InterruptedException e) {
			logger.warn("Error while adding Tuple to creation queue.");
		}
	}
	
	@Override
	public void run() {
		try {
			reconstructMissionModel();
			missionModel.setInitialized();
			
			// after reconstructing the model, all tuples will be applied to it. if a newer tuple arrives
			// in the meantime , tuples in this queue won't be applied to the model due to older timestamps
			while(!tupleQueue.isEmpty()) {
				Tuple tuple = tupleQueue.remove();
				missionModel.processTuple(tuple);
			}
		} catch (NoSuchElementException e) {
			logger.warn("Tried to remove an element from an empty queue");
		} catch (AgentDoesNotRespondException e) {
			logger.warn(e.getMessage());
		} catch (InvalidMissionSpecificationException e) {
			logger.error("Could not create mission model", e);
		} catch (TupleSpaceException e) {
			logger.error("Connection to TupleSpace lost");
		}
	}

	public void reconstructMissionModel() throws TupleSpaceException, AgentDoesNotRespondException, InvalidMissionSpecificationException {
		// MissionModel is empty, so we'll create the dependencies and activities for this mission
		createDependencyModel();

		// dependency model has just template URIs, so we'll add the concrete ELO URIs
		List<String> eloUris = this.rooloAccessor.getEloUris(missionModel.getName(), missionModel.getUser());
		if(eloUris != null) {
			for(String eloUri : eloUris) {
				String templateEloUri = getTemplateEloUri(this.rooloAccessor, eloUri);
				Activity act = missionModel.getActivityByTemplateUri(templateEloUri);
				if(act != null) {
					act.setEloUri(eloUri);
				}
			}
		} else {
			throw new AgentDoesNotRespondException("Could not reconstruct mission model, because RooloAccessorAgent did not respond.");
		}
	}
	
	private void createDependencyModel() throws TupleSpaceException, InvalidMissionSpecificationException {
		logger.debug(String.format("Creating dependency model [ user: %s | mission %s ]", 
				missionModel.getUser(), 
				missionModel.getName()));
		
		// TODO Seems to be identical to missionModel.getName()

		// Get the mission specification and create dependency model (which contains template URIs)
		String missionRuntimeURI = this.session.getMissionRuntimeURI(missionModel.getUser());
		String missionSpecificationURI = this.session.getMissionSpecification(missionRuntimeURI);

//		BasicELO missionSpec = this.rooloAccessor.getBasicElo(missionSpecificationURI);
//		logger.debug("missionSpecURI: " + missionSpec.getUri());
//		logger.debug("missionSpecRootURIString: " + missionSpec.getRootUriString());

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
				this.missionModel.addDependency(dependent, depending);
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
	
	private String getTemplateEloUri(RooloAccessor rooloAccessor, String eloUri) throws TupleSpaceException {
		BasicELO elo = rooloAccessor.getBasicElo(eloUri);
		IMetadata metadata = elo.getMetadata();

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
