package eu.scy.agents.agenda.evaluation;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import roolo.elo.JDomStringConversion;
import eu.scy.agents.agenda.evaluation.evaluators.IEvaluator;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.model.EscapeUtils;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public abstract class AbstractActivityEvaluationAgent extends AbstractThreadedAgent {

	private static final String RESPONSE_STRING = "response";
	
	protected final Logger logger;
	
	protected TupleSpace actionSpace;
	
	protected TupleSpace commandSpace;
	
	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	
	private final Map<String, IEvaluator> toolEvaluatorMap = new HashMap<String, IEvaluator>(); 
		
	
	protected AbstractActivityEvaluationAgent(String name, Map<String, Object> map) {
        super(name, (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        this.logger = Logger.getLogger(name);
        
        try {
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);

			initiateSignatures();
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}
	
	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		registerCallbacks();
		
        while (this.status == Status.Running) {
            try {
                sendAliveUpdate();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            
            // Waits for requests to get last modification timestamp
            Tuple returnTuple = commandSpace.waitToRead(getRequestTemplate(), AgentProtocol.COMMAND_EXPIRATION);
            if (returnTuple != null) {
            	try {
            		// searches for the last modification of a user in a 
            		String id = returnTuple.getField(0).getValue().toString();
            		String mission = returnTuple.getField(3).getValue().toString();
            		String userName = returnTuple.getField(4).getValue().toString();
            		long timestamp = Long.valueOf(returnTuple.getField(5).getValue().toString());
            		String[] eloUris = EscapeUtils.deEscape(returnTuple.getField(6).getValue().toString());
            		logger.debug(String.format(
            				"Received rebuild request with data [ User: %s | Mission: %s | EloURIs: %s]", 
            				userName, mission, eloUris.toString()));

            		performRebuild(id, mission, userName, timestamp, eloUris);
            	        		
            	} catch (NumberFormatException e) {
            		logger.warn("Received history request tuple had an invalid timestamp: " + Long.valueOf(returnTuple.getField(5).getValue().toString()));
            	}
            }		
        }
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
        try {
        	deregisterCallbacks();
            this.actionSpace.disconnect();
        } catch (TupleSpaceException e) {
    		logger.error("Error while disconnecting from ActionSpace!");
        }
        try {
        	this.commandSpace.disconnect();
        } catch (TupleSpaceException e) {
        	logger.error("Error while disconnecting from CommandSpace!");
        }
	}

	@Override
	public boolean isStopped() {
        return (this.status != Status.Running);
	}

	protected void registerEvaluator(IEvaluator evaluator) {
		this.toolEvaluatorMap.put(evaluator.getTool(), evaluator);
		logger.debug("Registered user action evaluator for tool: " + evaluator.getTool());
	}
	
	protected void performRebuild(String id, String mission, String userName, long timestamp, String[] eloUris) throws TupleSpaceException {
		LatestUserActionSearcher searcher = new LatestUserActionSearcher(actionSpace, this.toolEvaluatorMap);
		Map<String, Tuple> result = searcher.search(id, timestamp, mission, userName, eloUris);

		writeRebuildTuple(id, result.values());
	}

	private void writeRebuildTuple(String id, Collection<Tuple> tuples) throws TupleSpaceException {
    	Element root = new Element(UserAction.ELE_ROOT);
    	
    	for(Tuple tuple : tuples) {
    		long timestamp;
    		if(tuple != null) {
    			timestamp =  Long.valueOf(tuple.getField(2).getValue().toString());
    		} else {
    			timestamp = Long.MIN_VALUE;
    		}
    		String eloUri = tuple.getField(8).getValue().toString();
    		UserAction action = new UserAction(timestamp, eloUri);
    		root.addContent(action.toXmlElement());
    	}

    	Document document = new Document(root);
        JDomStringConversion stringConversion = new JDomStringConversion();
        String actionsAsXml = stringConversion.xmlToString(document);
        
        Tuple rebuildTuple = new Tuple(RESPONSE_STRING, id, actionsAsXml);
        this.commandSpace.write(rebuildTuple);
	}
	
	/**
	 * Registers callbacks for all initiated tools
	 * @throws TupleSpaceException
	 */
	protected void registerCallbacks() throws TupleSpaceException {
		for(String tool: this.toolEvaluatorMap.keySet()) {
			Callback cb = new UserActionCallback();
			Tuple actionSignature = new Tuple("action", String.class, Long.class, String.class, String.class, tool, Field.createWildCardField());
			int id = this.actionSpace.eventRegister(Command.WRITE, actionSignature, cb, true);
			this.registeredCallbacks.add(id);
			logger.info("Registered user action callback for tool: " + tool);
		}
	}

	protected void deregisterCallbacks() throws TupleSpaceException {
		for(Integer i : this.registeredCallbacks) {
			this.actionSpace.eventDeRegister(i);
		}
	}

	class UserActionCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			//  <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
			String tool = afterTuple.getField(5).getValue().toString();
			IEvaluator evaluator = toolEvaluatorMap.get(tool);
			if(evaluator != null && evaluator.doesMatch(afterTuple)) {
				// User has modified the activity
				try {
					long timestamp = Long.valueOf(afterTuple.getField(2).getValue().toString());
					String userName = afterTuple.getField(4).getValue().toString();
					String mission = afterTuple.getField(6).getValue().toString();
					String eloUri = afterTuple.getField(8).getValue().toString();
					
					handleMatchingUserAction(timestamp, mission, userName, tool, eloUri);
				} catch(NumberFormatException e) {
					logger.warn("Received UserAction with invalid timestamp " + afterTuple.getField(5).getValue().toString());
				}
			}
		}
	}

	protected abstract Tuple getRequestTemplate();
	
	/**
	 * Handles matching user action.
	 *
	 * @param timestamp the timestamp
	 * @param mission the mission
	 * @param userName the user name
	 * @param eloUri the elo uri
	 */
	protected abstract void handleMatchingUserAction(long timestamp, String mission, String userName, String tool, String eloUri);
		
	/**
	 * This method is called in the constructor and you should initiate the signatures (register Evaluators) here.
	 */
	protected abstract void initiateSignatures();
	
}
