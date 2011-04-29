package eu.scy.agents.search.searchresultenricher;

import java.lang.reflect.Array;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.collaboration.CollaborationAgent;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/*
 * This agent suggests other search queries, if the users search provided too less or too  many results.
 * It waits for a search query + search results coming from the roolo-agent and uses heuristics to enrich the search.
 * The enriched search queries will be written to the command space.
 * 
 */
public class SearchResultEnricherAgent extends AbstractThreadedAgent{
	
	private static final String AGENT_NAME = "search-result-enricher-agent";
	
	private static final String TOOL = "scy-search";
	
	private static final String TYPE = "search-query";
	
    private static final String ROOLO_AGENT_NAME = "roolo-agent";

    private static final String ROOLO_AGENT_RESPONSE = "roolo-response";
    
	private static final String ROOLO_AGENT_SEARCH = "search";
		
	private static final int ROOLO_AGENT_TIMEOUT = 5000;

    private static final int INFIMUM_GOOD_SEARCH_RESULT = 5;
    
    private static final int SUPREMUM_GOOD_SEARCH_RESULT = 20;

    private static final Tuple TEMPLATE_FOR_SEARCH_QUERY = new Tuple("action", String.class, Long.class, TYPE, String.class, TOOL, Field.createWildCardField());
    
    private static final Logger logger = Logger.getLogger(CollaborationAgent.class.getName());

    private TupleSpace actionSpace;

    private TupleSpace commandSpace;

    public SearchResultEnricherAgent(Map<String, Object> map) {
        super(SearchResultEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
        	actionSpace = getActionSpace();
            commandSpace = getCommandSpace();
        	
            Callback sqc = new SearchQueryCallback();
            actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_SEARCH_QUERY, sqc, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
    	System.out.println(AGENT_NAME + " started");
//    	logger.debug(AGENT_NAME + " started");
        while (status == Status.Running) {
            try {
                sendAliveUpdate();
                Thread.sleep(AgentProtocol.COMMAND_EXPIRATION);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		try {
			if (actionSpace != null) {
				actionSpace.disconnect();
			}
			if (commandSpace != null) {
				commandSpace.disconnect();
			}
			System.out.println(AGENT_NAME + " stopped");
//			logger.debug(AGENT_NAME + " stopped");
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			logger.error("Connection to TupleSpace lost!", e);
		}		
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
        return (status != Status.Running);
	}

    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "search result enricher id");
//        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_HOST, "scy.collide.info");
        map.put(AgentProtocol.TS_PORT, 2525);
        SearchResultEnricherAgent agent = new SearchResultEnricherAgent(map);
        agent.start();
    }
    
    class SearchQueryCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            enrichSearchResult(afterTuple);
        }
    }
    
    /*
     * Evaluate a user search result and decides whether to propose an optimized search query.
     * If nothing needs to be enriched, an empty array will be returned.
     */
    @SuppressWarnings("unchecked")
	public void enrichSearchResult(Tuple tuple) {
//        String id = tuple.getField(1).getValue().toString();
        String user = tuple.getField(4).getValue().toString();
        String mission = tuple.getField(6).getValue().toString();
        String session = tuple.getField(7).getValue().toString();
//        String elouri = tuple.getField(8).getValue().toString();

        Properties props = new Properties();
        for (int i = 9; i < tuple.getNumberOfFields(); i++) {
            String prop = tuple.getField(i).getValue().toString();
            int indexfirstEqualSign = prop.indexOf("=");
            String key = prop.substring(0, indexfirstEqualSign);
            String value = prop.substring(indexfirstEqualSign + 1);
            props.put(key, value);
        }
        if(props.getProperty("query") == null) {
        	return;
        }

        // Split search query in several items
        String[] items = props.getProperty("query").split(" ");
        
        KeyValue<String, Integer>[] alternativeQueries = null;
        if(props.containsKey("eloURIs")) {

        	// Split eloURIs
        	String[] results = props.getProperty("eloURIs").split(" ");

        	if(results.length < INFIMUM_GOOD_SEARCH_RESULT) {

        		// Too less results...
        		alternativeQueries = graftSearchResult(items, results);
        	} else if(results.length > SUPREMUM_GOOD_SEARCH_RESULT) {
        		
        		// Too many results...
        		alternativeQueries = pruneSearchResult(items, results);
        	} else {
        		
            	// Number of results is ok: return an empty array
        		alternativeQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, 0);        		
        	}
        } else if(props.containsKey("something else...")) {
        	
        	// handle metadata and other stuff here. 
        	alternativeQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, 0);
        } else {
        	
        	// handle something else here.
        	alternativeQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, 0);
        }

        // Propose better search queries, if found
		if (alternativeQueries != null && alternativeQueries.length > 0) {
//			KeyValue<String, Integer>[] optimizedQueries = evaluateQueries(alternativeQueries);
			System.out.println(generateResponseTuple(user, mission, session, alternativeQueries));
//			try {
//				commandSpace.write(generateResponseTuple(id, user, mission, session, optimizedQueries));
//			} catch (TupleSpaceException e) {
//				System.out.println(e);
//			}
		}
    }
    
    /*
     * Filters out queries with bad results.
     */
    @SuppressWarnings("unchecked")
	private KeyValue<String, Integer>[] evaluateQueries(KeyValue<String, Integer>[] queries) {
    	List<KeyValue<String, Integer>> list = new ArrayList<SearchResultEnricherAgent.KeyValue<String,Integer>>();
    	for(KeyValue<String, Integer> query : queries) {
    		if(query.value > INFIMUM_GOOD_SEARCH_RESULT && query.value < SUPREMUM_GOOD_SEARCH_RESULT) {
    			list.add(query);
    		}
    	}
    	return (KeyValue<String, Integer>[])list.toArray();
    }
    
    /*
     * Tries to create another search query, that will provide more results.
     */
	@SuppressWarnings("unchecked")
	private KeyValue<String, Integer>[] graftSearchResult(String[] items, String[] results) {
		KeyValue<String, Integer>[] searchQueries = null;

    	if(items.length <= 1) {
    		// Only one item and too less results... this must be handled separately
    		
    		// ToDo: this is just a stub
    		searchQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, 0);
    	} else {
    		
    		String[] itemsets = generateItemSets(items);
    		
			// Evaluate the new queries.
			searchQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, itemsets.length);
        	for(int i = 0; i < itemsets.length; i++) {

        		searchQueries[i] = performSearch(itemsets[i].toString());
        		if(searchQueries[i] == null) {
    				// roolo-agent timed out - stop process
        			break;
        		}
        	}
    	}
    	return searchQueries;
    }

	/*
	 * Generate n * (n-1)-itemsets out of the n-itemset (i.e.: two 1-itemsets for one 2-itemset)
	 */
	private String[] generateItemSets(String[] items) {
		String[] itemsets = new String[items.length];
		// Generate queries with one
		for (int i = 0; i < items.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < items.length; j++) {
				if (i != j) {
					sb.append(items[j]);
					if(j < items.length - 1) {
						sb.append(" ");
					}
				}
			}
			itemsets[i] = sb.toString().trim();
		}
		return itemsets;
	}
	
    /*
     * Tries to prune the search result, by generating a more specific search query.
     */
    @SuppressWarnings("unchecked")
	private KeyValue<String, Integer>[] pruneSearchResult(String[] items, String[] results) {
		KeyValue<String, Integer>[] searchQueries = (KeyValue<String, Integer>[]) Array.newInstance(KeyValue.class, 0);
		return searchQueries;
    }
    
    /*
     * Generates the resulting tuple, containing the alterantive search requests and their hit counts.
     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>, <Query=NumberOfHits>:String*)
     */
    private Tuple generateResponseTuple(String userId, String mission, String session, KeyValue<String, Integer>[] result) {
    	Tuple responseTuple = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), userId, TOOL, AGENT_NAME, mission, session);

    	for(KeyValue<String, Integer> keyValue : result) {
    		if(keyValue != null) {
    			responseTuple.add(keyValue.toString());
    		}
    	}
    	return responseTuple;
    }
    
    /*
     * Performs a new search and responds with a key-value pair containing the 
     * search string as key and the number of hits as response.
     */
    private KeyValue<String, Integer> performSearch(String query) {    	
    	try {
			String uniqueID = new VMID().toString();
			Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME,
					ROOLO_AGENT_SEARCH, query);
			commandSpace.write(requestTuple);

			Tuple responseTuple = commandSpace.waitToTake(
					new Tuple(uniqueID, ROOLO_AGENT_RESPONSE,Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
			if(responseTuple == null) {
				return null;
			}
			return processRooloResponseTuple(query, responseTuple);
//			return new KeyValue<String, Integer>(query, 1);
    	} catch (TupleSpaceException ex) {
    		ex.printStackTrace();
    		return null;
    	}
    }
    
    private KeyValue<String, Integer> processRooloResponseTuple(String query, Tuple responseTuple) {
    	String eloURIs = responseTuple.getField(2).getValue().toString().trim();
    	String[] uriArray = eloURIs.split(" ");
    	return new KeyValue<String, Integer>(query, uriArray.length);
    }
    
    class KeyValue<K, V>
    {    	
    	private K key;    	

    	private V value;
    	
    	public KeyValue(K key, V value)
    	{
    		this.key = key;
    		this.value = value;
    	}
    	
    	public K getKey() {
    		return this.key;
    	}
    	
    	public V getValue() {
    		return this.value;
    	}
    	
    	public String toString() {
    		return this.key + "=" + this.value;
    	}
    	
    	public Field getField() {
    		return new Field(toString());
    	}
    }
}
