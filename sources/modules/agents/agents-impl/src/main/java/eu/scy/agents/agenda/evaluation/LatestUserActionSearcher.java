package eu.scy.agents.agenda.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.agenda.evaluation.evaluators.IEvaluator;

/**
 * This class is used to search for the latest tuple matching a specific pattern.
 * It uses tool-specific IEvaluator to check for matching tuples.
 * @author Chris
 */
public class LatestUserActionSearcher {
	
	private static final int READ_ALL_WINDOW_SIZE = 100;
	
	private final TupleSpace actionSpace;
	
	private final Map<String, IEvaluator> signatures; 
	
	private boolean interrupt;
	
	private boolean isActive;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	public LatestUserActionSearcher(TupleSpace actionSpace, Map<String, IEvaluator> signatures) {
		this.actionSpace = actionSpace;
		this.signatures = signatures;
		this.interrupt = false;
		this.isActive = false;
	}
	
	public void stop() {
		this.interrupt = true;
	}
	
	public boolean isActive() {
		this.lock.lock();
		try {
			return this.isActive;
		} finally {
			this.lock.unlock();
		}
	}
	
	public Map<String, Tuple> search(String id, long timestamp, String mission, String userName, String[] eloUris) throws TupleSpaceException {
		this.lock.lock();
		try {
			this.isActive = true;
		} finally {
			this.lock.unlock();
		}
		
		try {
			// create hashmap: ELOURI -> last modification tuple (default to null)
			Map<String, Tuple> elosLastModification = new HashMap<String, Tuple>();
			for(String eloUri : eloUris) {
				elosLastModification.put(eloUri, null);
			}
			
			// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			//  <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
			Field timeField = new Field(Integer.class);
			timeField.setLowerBound(timestamp);
			Tuple actionSignature = new Tuple("action", String.class, timeField, String.class, userName, String.class, mission, Field.createWildCardField());
			
			for (Tuple tuple : actionSpace.readAll(actionSignature, READ_ALL_WINDOW_SIZE)) {
				
				// search has been interrupted from outside
				if(this.interrupt) {
					this.interrupt = false;
					return null;
				}
	
				// check whether the tuple's ELO URI is relevant
				String currentEloUri = tuple.getField(8).getValue().toString();
				if(!elosLastModification.containsKey(currentEloUri)) {
					continue;
				}
				
				// check whether tuple's action type indicates a modification
				String tool = tuple.getField(5).getValue().toString();
				IEvaluator evaluator = signatures.get(tool);
				if(evaluator == null || !evaluator.doesMatch(tuple)) {
					continue;
				}
				
				Tuple lastModification = elosLastModification.get(currentEloUri);
				if(lastModification == null) {
					
					// first tuple for this elo uri
					elosLastModification.put(currentEloUri, tuple);
				} else {
					
					// another tuple for elo uri already exists
					long time;
					long lastModificationTime;
					try {
						time = Long.valueOf(tuple.getField(2).getValue().toString());
						lastModificationTime = Long.valueOf(lastModification.getField(2).getValue().toString());
					} catch(NumberFormatException e) {
						// invalid timestamp, ignore this action
						continue;
					}

					// update only if this tuple is newer than the last one
					if(lastModificationTime < time) {
						elosLastModification.put(currentEloUri, tuple);
					}
				}
			}
			return elosLastModification;
		} finally {
			this.lock.lock();
			try {
				this.isActive = false;
			} finally {
				this.lock.unlock();
			}
		}
	}
}
