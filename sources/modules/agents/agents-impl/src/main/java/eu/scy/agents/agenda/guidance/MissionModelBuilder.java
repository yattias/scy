package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.commons.Tuple;

import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import eu.scy.agents.agenda.exception.InvalidMissionSpecificationException;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;
import eu.scy.agents.agenda.guidance.model.MissionModel;

public class MissionModelBuilder implements Runnable {
	
	private static final Logger logger = Logger.getLogger(MissionModelBuilder.class.getName());

	private final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
	private final MissionModel missionModel;
	
	public MissionModelBuilder(MissionModel missionModel) {
		if(missionModel == null) {
			throw new IllegalArgumentException("missionModel can not be null");
		}
		this.missionModel = missionModel;
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
			missionModel.reconstruct();
			
			// after reconstructing the model, all tuples will be applied to it. if a newer tuple arrives
			// in the meantime , tuples in this queue won't be applied to the model due to older timestamps
			missionModel.setInitialized();
			while(!tupleQueue.isEmpty()) {
				Tuple tuple = tupleQueue.remove();
				missionModel.processTuple(tuple);
			}
		} catch (NoSuchElementException e) {
			logger.warn("Tried to remove an element from an empty queue");
		} catch (InvalidMissionSpecificationException e) {
			logger.error("Could not create mission model, because mission specification was invalid", e);
		} catch (URISyntaxException e) {
			logger.error("Could not create model because eloUri is no valid URL", e);
		} catch (NoRepositoryAvailableException e) {
			logger.error("Could not create mission model, because repository could not be queried.", e);
		}
	}

}
