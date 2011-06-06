package eu.scy.agents.conceptmap.model;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

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
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class UserConceptMapAgent extends AbstractThreadedAgent {

    private static final String TOOL = "scymapper";

    private static final String AGENT_NAME = "userconceptmapagent";

    private static final String SERVICE_TYPE = "conceptmap";

    private static final String REQUEST_NODES = "nodes";

    private static final String REQUEST_EDGES = "edges";

    private static final Tuple TEMPLATE_FOR_MAPPER_ACTIONS = new Tuple("action", String.class, Long.class, String.class, String.class, TOOL, Field.createWildCardField());

    // (<ID>:String, <AgentName>:String, <ServiceType>:String, <UserName>:String, <EloURI>:String,
    // <RequestType>:String, <Parameters>...)
    private static final Tuple TEMPLATE_FOR_REQUEST_CONCEPT_MAP = new Tuple(String.class, AGENT_NAME, SERVICE_TYPE, String.class, String.class, String.class, Field.createWildCardField());

    // If you want to add new types, also update graphChanges() method
    private static final String TYPE_SYNONYM_ADDED = "synonym_added";

    private static final String TYPE_NODE_ADDED = "node_added";

    private static final String TYPE_NODE_RENAMED = "node_renamed";

    private static final String TYPE_NODE_REMOVED = "node_removed";

    private static final String TYPE_LINK_ADDED = "link_added";

    private static final String TYPE_LINK_RENAMED = "link_renamed";

    private static final String TYPE_LINK_REMOVED = "link_removed";

    private static final String TYPE_LINK_FLIPPED = "link_flipped";
    
    private static final String TYPE_ELOLOAD = "elo_load";

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    private static final Logger logger = Logger.getLogger(UserConceptMapAgent.class.getName());

    private HashMap<String, ConceptMapUser> users;

    private Map<String, BlockingQueue<Tuple>> userBlockingQueue;

    private final ReentrantLock myLock = new ReentrantLock(true);

    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "userconceptmap modeller id");
        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_PORT, 2525);
        UserConceptMapAgent agent = new UserConceptMapAgent(map);
        agent.start();
    }

    public UserConceptMapAgent(Map<String, Object> map) {
        super(UserConceptMapAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        userBlockingQueue = new Hashtable<String, BlockingQueue<Tuple>>();
        try {
            users = new HashMap<String, ConceptMapUser>();
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
            Callback cmmc = new ConceptMapModelCallback();
            actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_MAPPER_ACTIONS, cmmc, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException {
        while (status == Status.Running) {
            try {
                sendAliveUpdate();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            Tuple returnTuple = commandSpace.waitToTake(TEMPLATE_FOR_REQUEST_CONCEPT_MAP, AgentProtocol.COMMAND_EXPIRATION);
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
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
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
            String value = prop.substring(IndexfirstEqualSign + 1);
            props.put(key, value);
        }

        try {
            ConceptMapUser currentUser = findUser(userName);
            // Search for the current model in his model list
            ConceptMapModel currentModel = currentUser.getModel(eloUri);

            if (currentModel == null) {
                // no local model - search in Roolo
                myLock.lock();
                userBlockingQueue.put(eloUri, new LinkedBlockingQueue<Tuple>());
                currentModel = reconstructConceptMapModel(currentUser, eloUri);
            }

            if (graphChanges(type)) {
                if (userBlockingQueue.containsKey(eloUri)) {
                    // User changed the graph, while another thread is locking
                    try {
                        userBlockingQueue.get(eloUri).put(actionTuple);
                    } catch (InterruptedException e) {
                        logger.info("Interruption while adding new tuple to the blocking queue.");
                    }
                    return;
                } else {
                    modifyGraph(currentModel, type, props);
                }
            } else if (type.equals("print_graph")) {
                // TODO only for testing purposes
                // System.out.println(currentModel.getGraph().toString());
            }

        } catch (MissingModelException e) {
            // worst case, no error recovery!
            // TODO changed for testing purposes
            // logger.severe("Unable to open concept map model for ELO "+ eloUri);
            // System.out.println("Unable to open concept map model for ELO " + eloUri);
        } catch (TupleSpaceException e) {
            // worst case, no error recovery!
            // TODO changed for testing purposes
            // logger.info("Error in TupleSpace while reconstructing model");
            // System.out.println("Error in TupleSpace while reconstruction model");
        } finally {
            if (myLock.isHeldByCurrentThread()) {
                myLock.unlock();
            }
        }
    }

    private ConceptMapUser findUser(String userName) {
        // Search for the current user in the userlist
        ConceptMapUser currentUser = users.get(userName);
        if (currentUser == null) {
            // User not found - create new user
            currentUser = new ConceptMapUser(userName);
            users.put(userName, currentUser);
        }
        return currentUser;
    }

    public void modifyGraph(ConceptMapModel model, String type, Properties props) {
        if (type.equals(TYPE_NODE_ADDED)) {
            model.nodeAdded(props);
        } else if (type.equals(TYPE_SYNONYM_ADDED)) {
        	model.synonymAdded(props);
        } else if (type.equals(TYPE_NODE_REMOVED)) {
            model.nodeRemoved(props);
        } else if (type.equals(TYPE_LINK_ADDED)) {
            model.edgeAdded(props);
        } else if (type.equals(TYPE_LINK_REMOVED)) {
            model.edgeRemoved(props);
        } else if (type.equals(TYPE_NODE_RENAMED) || type.equals(TYPE_LINK_RENAMED)) {
            model.labelChanged(props);
        } else if (type.equals(TYPE_LINK_FLIPPED)) {
            model.edgeFlipped(props);
        }
    }

    private Tuple generateResponse(Tuple requestTuple) throws AgentLifecycleException {
        // Expects a Tuple that requests all user nodes or edges.
        // Generates a tuple with all nodes OR edges of the current concept map.
        Field[] nodesOrEdges;
        Field[] responseFields;
        String id = requestTuple.getField(0).getValue().toString();
        String userName = requestTuple.getField(3).getValue().toString();
        String eloUri = requestTuple.getField(4).getValue().toString();

        try {
            myLock.lock();
            if (requestTuple.getField(5).getValue().toString().equals(REQUEST_NODES)) {
                nodesOrEdges = users.get(userName).getModel(eloUri).getGraph().getNodesAsFields();
            } else if (requestTuple.getField(5).getValue().toString().equals(REQUEST_EDGES)) {
                nodesOrEdges = users.get(userName).getModel(eloUri).getGraph().getEdgesAsFields();
            } else {
                throw new AgentLifecycleException("Unknown RequestType");
            }
        } finally {
            myLock.unlock();
        }

        // ResponseTuple: (<ID>:String, "response":String, <NodesOrEdges> ...)
        responseFields = new Field[nodesOrEdges.length + 2];
        responseFields[0] = new Field(id);
        responseFields[1] = new Field("response");
        System.arraycopy(nodesOrEdges, 0, responseFields, 2, nodesOrEdges.length);

        return new Tuple(responseFields);
    }

    private ConceptMapModel reconstructConceptMapModel(ConceptMapUser user, String eloUri) throws TupleSpaceException, MissingModelException {
        // Generates a model of the ELO with the given EloURI.
        ConceptMapModel currentModel;
        long latestTimestamp = 0;
        Tuple eloloadTemplate = new Tuple("action", String.class, Long.class, TYPE_ELOLOAD, String.class, TOOL, String.class, String.class, eloUri, Field.createWildCardField());

        currentModel = loadELO(eloUri);
        if (currentModel == null) {
            throw new MissingModelException();
        }

        // model loaded, but there may be tuples in the blocking queue
        BlockingQueue<Tuple> actionQueue = userBlockingQueue.get(eloUri);
        while (!actionQueue.isEmpty()) {
            Tuple remove = actionQueue.remove();
            handleAction(remove);
        }

        // last check
        actionQueue = userBlockingQueue.remove(eloUri);
        while (!actionQueue.isEmpty()) {
            handleAction(actionQueue.remove());
        }
        
        user.addConceptMapModel(currentModel);
        
        // TODO TupleSpace server does not respond
        // Search timestamp of the latest eloload
        for (Tuple tuple : actionSpace.readAll(eloloadTemplate)) {
            if (latestTimestamp < tuple.getCreationTimestamp()) {
                latestTimestamp = tuple.getCreationTimestamp();
            }
        }

        // regard possible changes on the model since load
        Field timeField = new Field(Long.class);
        timeField.setLowerBound(latestTimestamp + 1);
        Tuple actionTemplate = new Tuple("action", String.class, timeField, String.class, String.class, TOOL, String.class, String.class, eloUri, Field.createWildCardField());
        Tuple[] latestTuplesArray = actionSpace.readAll(actionTemplate);
        List<Tuple> latestTuples = new ArrayList<Tuple>(Arrays.asList(latestTuplesArray));

        Collections.sort(latestTuples, new Comparator<Tuple>() {

            @Override
            public int compare(Tuple t1, Tuple t2) {
                return (int) (t1.getCreationTimestamp() - t2.getCreationTimestamp());
            }
        });
        for (Tuple tuple : latestTuples) {
            handleAction(tuple);
        }

        return currentModel;
    }

    /**
     * Loads an ELO for a given eloURI and returns a ConceptMapModel of it. If no responding ELO
     * tuple arrives within 30 sec, {@literal null} will be returned.
     * 
     * @param eloUri
     * @return The ConceptMapModel for the ELO
     */
    public ConceptMapModel loadELO(String eloUri) throws TupleSpaceException {
        // TODO changed for testing purposes
        // Tuple responseTuple;
        // String uniqueId = new VMID().toString();
        // Tuple queryTemplate = new Tuple(uniqueId, ROOLO_AGENT_NAME, "elo", eloUri);
        // Tuple responseTemplate = new Tuple(uniqueId,"response", String.class);

        // commandSpace.write(queryTemplate);
        // responseTuple = commandSpace.waitToTake(responseTemplate, 30000);
        // if(responseTuple == null) {
        // return null;
        // }
        //
        // String eloAsXML = responseTuple.getField(2).toString();
        // String eloAsXML = TestingEnvironment.getExampleXml();
        // SCYMapperAdapter adapter = new SCYMapperAdapter();
        // return new ConceptMapModel(eloUri, adapter.transformToGraph(eloAsXML));
        return new ConceptMapModel(eloUri, new Graph());
    }

    /**
     * Returns true if the given type will change the graph.
     * 
     * @param type
     * @return
     */
    public static boolean graphChanges(String type) {
        if (type.equals(TYPE_LINK_ADDED) || type.equals(TYPE_LINK_REMOVED) || type.equals(TYPE_LINK_RENAMED) || type.equals(TYPE_NODE_ADDED) || type.equals(TYPE_NODE_REMOVED) || type.equals(TYPE_NODE_RENAMED) || type.equals(TYPE_LINK_FLIPPED) || type.equals(TYPE_SYNONYM_ADDED)) {
            return true;
        }
        return false;
    }
}
