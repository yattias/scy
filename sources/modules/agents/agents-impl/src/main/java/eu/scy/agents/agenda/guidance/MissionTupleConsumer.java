package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.commons.Tuple;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.scy.agents.agenda.guidance.model.MissionModel;

class MissionTupleConsumer implements Runnable {
	
	private static final long TAKE_TIMEOUT = 1000;
	private static final Logger logger = Logger.getLogger(MissionTupleConsumer.class.getName());

	private final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
	private final UserModelDictionary userModelDict;
	private volatile boolean keepRunning = true;
	
	public MissionTupleConsumer(UserModelDictionary userModelDict) {
		if(userModelDict == null) {
			throw new IllegalArgumentException("userModelDict can not be null");
		}
		this.userModelDict = userModelDict;
	}
	
	public void addTuple(Tuple t) throws InterruptedException {
		logger.debug("Adding tuple to queue " + t.toString());
		this.tupleQueue.put(t);
	}
	
	public void stop() {
		this.keepRunning = false;
	}
	
	@Override
	public void run() {
		this.keepRunning = true;
		logger.debug("Mission tuple consumer started.");
		while(this.keepRunning) {
			try {
				Tuple tuple = tupleQueue.poll(TAKE_TIMEOUT, TimeUnit.MILLISECONDS);
				if(tuple != null) {
					String userName = tuple.getField(2).getValue().toString();
					String missionName = tuple.getField(1).getValue().toString();
					logger.debug(String.format("Received new tuple from user %s for known mission %s.", userName, missionName));
					
					MissionModel missionModel = userModelDict.getMissionModel(userName, missionName);
					missionModel.processTuple(tuple);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Mission tuple consumer stopped.");
	}

}

