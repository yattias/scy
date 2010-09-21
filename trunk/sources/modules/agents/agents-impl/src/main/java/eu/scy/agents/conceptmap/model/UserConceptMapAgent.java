package eu.scy.agents.conceptmap.model;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class UserConceptMapAgent extends AbstractThreadedAgent {

	private static final String TOOL = "scymapper";
	private static final String AGENT_NAME = "userconceptmapagent";
	private static final String SERVICE_TYPE = "conceptmap";
	private static final String REQUEST_NODES = "nodes";
	private static final String REQUEST_EDGES = "edges";

	private static final Tuple TEMPLATE_FOR_MAPPER_ACTIONS = new Tuple(
			"action", String.class, Long.class, String.class, String.class,
			TOOL, Field.createWildCardField());
//	(<ID>:String, <AgentName>:String, <ServiceType>:String, <UserName>:String, <EloURI>:String, <RequestType>:String, <Parameters>...)
	private static final Tuple TEMPLATE_FOR_REQUEST_CONCEPT_MAP = new Tuple(
			String.class, AGENT_NAME, SERVICE_TYPE, String.class, String.class, String.class,
			Field.createWildCardField());
	
	private static final String TYPE_NODE_ADDED   = "node_added";
	private static final String TYPE_NODE_RENAMED = "node_renamed";
	private static final String TYPE_NODE_REMOVED = "node_removed";
	private static final String TYPE_LINK_ADDED   = "link_added";
	private static final String TYPE_LINK_RENAMED = "link_renamed";
	private static final String TYPE_LINK_REMOVED = "link_removed";
	private static final String TYPE_ELOLOAD      = "eloload";
	
	//TODO ADD Locking for access on queue
	private TupleSpace commandSpace;

	private TupleSpace actionSpace;
	
	private HashMap<String, ConceptMapUser> users;
	private Map<String, BlockingQueue<Tuple>> userBlockingQueue;

	public UserConceptMapAgent(Map<String, Object> map) {
		super(UserConceptMapAgent.class.getName(), (String) map
				.get(AgentProtocol.PARAM_AGENT_ID), (String) map
				.get(AgentProtocol.TS_HOST), (Integer) map
				.get(AgentProtocol.TS_PORT));
		userBlockingQueue= new Hashtable<String,BlockingQueue<Tuple>>();
		try {
			users = new HashMap<String, ConceptMapUser>();
			commandSpace = new TupleSpace(new User(getSimpleName()), host,
					port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			actionSpace = new TupleSpace(new User(getSimpleName()), host, port,
					false, false, AgentProtocol.ACTION_SPACE_NAME);
			Callback cmmc = new ConceptMapModelCallback();
			actionSpace.eventRegister(Command.WRITE,
					TEMPLATE_FOR_MAPPER_ACTIONS, cmmc, false);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Tuple returnTuple = commandSpace.waitToTake(TEMPLATE_FOR_REQUEST_CONCEPT_MAP, 5000);
			if (returnTuple != null) {

				commandSpace.write(generateResponse(returnTuple));
			} 
		}
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		try {
			actionSpace.disconnect();
			commandSpace.disconnect();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStopped() {
		return (status != Status.Running);
	}

	class ConceptMapModelCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple,
				Tuple beforeTuple) {

			handleAction(afterTuple);
		}
	}

	private void handleAction(Tuple actionTuple) {
		String type = actionTuple.getField(3).getValue().toString();
		String userName = actionTuple.getField(4).getValue().toString();
		String eloUri = actionTuple.getField(8).getValue().toString();
		Properties props = new Properties();
		for (int i = 9; i < actionTuple.getNumberOfFields(); i++) {
			String prop = actionTuple.getField(i).getValue().toString();
			int IndexfirstEqualSign = prop.indexOf("=");
			String key = prop.substring(0, IndexfirstEqualSign);
			String value = prop.substring( IndexfirstEqualSign+1);
			props.put(key, value);
		}
	
		// Search for the current user in the userlist
		ConceptMapUser currentUser = users.get(userName);;
		if(currentUser == null) {
			// User not found - create new user
			currentUser = new ConceptMapUser(userName);
			users.put(userName, currentUser);
		}
	
		// Search for the current model in his model list
		ConceptMapModel currentModel = currentUser.getModel(eloUri);
		if (userBlockingQueue.containsKey(eloUri)){
			try {
				userBlockingQueue.get(eloUri).put(actionTuple);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		if(currentModel == null) {
			// no local model present - search in tuplespace for eloload
			try {
				userBlockingQueue.put(eloUri,new LinkedBlockingQueue<Tuple>());
				currentModel = searchLoadTuple(eloUri);
				userBlockingQueue.remove(eloUri);
				
			} catch (TupleSpaceException e) {
				// TODO output
				// Shouldn't happen, the tuplespace worked fine moments ago
			}
			currentUser.addConceptMapModel(currentModel);
		}
		
		if (type.equals(TYPE_NODE_ADDED)) {
			currentModel.nodeAdded(props);
		} else if(type.equals(TYPE_NODE_REMOVED)) {
			currentModel.nodeRemoved(props);
		} else if(type.equals(TYPE_LINK_ADDED)) {
			currentModel.edgeAdded(props);
		} else if(type.equals(TYPE_LINK_REMOVED)) {
			currentModel.edgeRemoved(props);
		} else if(type.equals(TYPE_NODE_RENAMED) || type.equals(TYPE_LINK_RENAMED)) {
			currentModel.labelChanged(props);
		} else if(type.equals(TYPE_ELOLOAD)) {
			// TODO load elo from roolo
		}
	}


	/**
	 * Expects a {@link import info.collide.sqlspaces.commons.Tuple} that requests all user nodes or edges.
	 * Generates a tuple with all nodes OR edges of the current concept map.
	 * @param requestTuple
	 * @return
	 * @throws AgentLifecycleException
	 */
	public Tuple generateResponse(Tuple requestTuple) throws AgentLifecycleException {
		Field[] nodesOrEdges;
		Field[] responseFields;
		String id = requestTuple.getField(0).getValue().toString();
		String userName = requestTuple.getField(3).getValue().toString();
		String eloUri = requestTuple.getField(4).getValue().toString();
	
		if(requestTuple.getField(5).getValue().toString().equals(REQUEST_NODES)) {
			nodesOrEdges = users.get(userName).getModel(eloUri).getGraph().getNodesAsFields();
		} else if(requestTuple.getField(5).getValue().toString().equals(REQUEST_EDGES)) {
			nodesOrEdges = users.get(userName).getModel(eloUri).getGraph().getEdgesAsFields();
		} else {
			throw new AgentLifecycleException("Unknown RequestType"); 
		}
	
		// ResponseTuple: (<ID>:String, "response":String, <NodesOrEdges> ...)
		responseFields = new Field[nodesOrEdges.length+2];
		responseFields[0] = new Field(id);
		responseFields[1] = new Field("response");
		System.arraycopy(nodesOrEdges, 0, responseFields, 2, nodesOrEdges.length);
	
		return new Tuple(responseFields);
	}

	private ConceptMapModel searchLoadTuple(String eloUri) throws TupleSpaceException {
		long latestTimestamp = 0;
		Tuple eloloadTemplate = new Tuple(
				"action", String.class, Long.class, TYPE_ELOLOAD, String.class,
				TOOL, String.class, String.class, eloUri, Field.createWildCardField());
		ConceptMapModel currentModel = null;
		Tuple[] latestTuplesArray;
		List<Tuple> latestTuples;

		// Search the latest eloload tuple
		for(Tuple tuple: actionSpace.readAll(eloloadTemplate)) {
			if(latestTimestamp < tuple.getCreationTimestamp()) {
				latestTimestamp = tuple.getCreationTimestamp();
			}
		}

//		currentModel = loadELO(eloUri);

		// regard possible changes on the model since load
		Field timeField = new Field(Long.class);
		timeField.setLowerBound(latestTimestamp + 1);
		Tuple actionTemplate = new Tuple("action", String.class, timeField,
				TYPE_ELOLOAD, eloUri, TOOL, Field.createWildCardField());

		latestTuplesArray = actionSpace.readAll(actionTemplate);
		latestTuples = new ArrayList<Tuple>(Arrays.asList(latestTuplesArray)); 
		Collections.sort(latestTuples, new Comparator<Tuple>() {
			@Override
			public int compare(Tuple t1, Tuple t2) {
				if(t1.getCreationTimestamp() < t2.getCreationTimestamp()) {
					return -1;
				} else if(t1.getCreationTimestamp() == t2.getCreationTimestamp()) {
					return 0;
				} else {
					return 1;
				}
			}
			
		});
//			String uid = new VMID().toString();

		return currentModel;
	}

//	/**
//	 * Loads ELO with given eloURI and creates a {@link ConceptMapModel} 
//	 * @param eloUri
//	 * @return The ConceptMapModel for the ELO
//	 */
//	public ConceptMapModel loadELO(String eloUri) {
//		// TODO stub
//		return null;
//	}

}
