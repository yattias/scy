package eu.scy.agents.search.searchresultenricher;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import roolo.search.ISearchResult;
import roolo.search.util.SearchResultUtils;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
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
	
	private static final String SEARCH_TOOL = "scy-simple-search";
	
	private static final String SEARCH_TYPE = "search-query";
	
    private static final String ROOLO_AGENT_NAME = "roolo-agent";

    private static final String ROOLO_AGENT_RESPONSE = "roolo-response";
    
	private static final String ROOLO_AGENT_SEARCH = "search";
	
	// TODO insert correct values
	private static final String ONTOLOGY_AGENT_NAME = "ontology-agent";
	private static final String ONTOLOGY_AGENT_COMMAND = "get synonyms";
	
	private static final int ROOLO_AGENT_TIMEOUT = 5000;
	
	private static final String ACTION_LOG_ATTRIBUTE_QUERY = "query";
		
	private static final String ACTION_LOG_ATTRIBUTE_RESULT = "search_results";

	private static final int MAXIMAL_PROPOSAL_NUMBER = 5;
	
    public static final int INFIMUM_GOOD_SEARCH_RESULT = 5;
    
    public static final int SUPREMUM_GOOD_SEARCH_RESULT = 20;
    

    private static final Tuple TEMPLATE_FOR_SEARCH_QUERY = new Tuple("action", String.class, Long.class, SEARCH_TYPE, String.class, SEARCH_TOOL, Field.createWildCardField());
    
//    private static final Logger logger = Logger.getLogger(SearchResultEnricherAgent.class.getName());

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
        
        try {
        	agent.start();
        } catch (AgentLifecycleException ex) {
        	ex.printStackTrace();
        }
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
	public void enrichSearchResult(Tuple tuple) {
//            String id = tuple.getField(1).getValue().toString();
            String eloUri = tuple.getField(8).getValue().toString();
            String user = tuple.getField(4).getValue().toString();
            String mission = tuple.getField(6).getValue().toString();
            String session = tuple.getField(7).getValue().toString();

            Properties props = new Properties();
            for (int i = 9; i < tuple.getNumberOfFields(); i++) {
                String prop = tuple.getField(i).getValue().toString();
                int indexfirstEqualSign = prop.indexOf("=");
                String key = prop.substring(0, indexfirstEqualSign);
                String value = prop.substring(indexfirstEqualSign + 1);
                props.put(key, value);
            }
            if(props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) == null || props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY).equals("") ||
                props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT) == null || props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT).equals("")) {
//                logger.warn("Received action log with missing values! Query = " +
//                                props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) + " | Result = " +
//                                props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));
                System.out.println("Received action log with missing values! Query = " +
                                props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) + " | Result = " +
                                props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));
                return;
            }

            // Split search query in several items
            String searchQuery = props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY);

            // use the results of the user query as reference
            List<ISearchResult> referenceResults = SearchResultUtils.createSearchResultsFromXML(props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));

            SearchResultRanking ranking = null;
            // Choose strategy
            if (referenceResults.size() < INFIMUM_GOOD_SEARCH_RESULT) {
                ranking = extendSearchResult(user, eloUri, searchQuery, referenceResults);

            } else if (referenceResults.size() > SUPREMUM_GOOD_SEARCH_RESULT) {
                ranking = pruneSearchResult(searchQuery, referenceResults);

            } else {
                // Number of results is ok, so we do nothing
            }
        
            // Propose better search queries, if found
            if (ranking != null && ranking.getResults().length > 0) {
                System.out.println(generateTuple(user, mission, session, ranking));

//                try {
//                        commandSpace.write(generateResponseTuple(user, mission, session, ranking.getResults()));
//                } catch (TupleSpaceException e) {
//                        System.out.println(e);
//                }
            }
        }
    
        /*
         * Tries to create another search query, that will provide more results.
         */
        private SearchResultRanking extendSearchResult(String userId, String eloUri, String query, List<ISearchResult> referenceResults) {
            SearchResultRanking ranking = new SearchResultRanking(MAXIMAL_PROPOSAL_NUMBER, referenceResults);

            // TODO ask ontology for synonyms
//		askOntologyForSynonym(userId, eloUri, "keyword");
            return ranking;
        }

	private String[] askOntologyForSynonym(String userId, String eloUri, String keyword) {
            // Asks the ontology for synonyms
            try {
                String id = new VMID().toString();
                Tuple requestTuple = new Tuple(id, userId, ONTOLOGY_AGENT_NAME, ONTOLOGY_AGENT_COMMAND, userId, eloUri, keyword);
                this.commandSpace.write(requestTuple);
                Tuple responseTuple = this.commandSpace.waitToTake(new Tuple(id, "response", Field.createWildCardField()));
                if(responseTuple != null) {
                String[] result = new String[responseTuple.getNumberOfFields() - 2];
                for (int i = 0; i < responseTuple.getNumberOfFields() - 2; i++) {
                    result[i] = responseTuple.getField(i + 2).getValue().toString();
                }
                return result;
                }
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            return new String[0];
	}

    /*
     * Tries to prune the search result.
     */
	private SearchResultRanking pruneSearchResult(String searchQuery, List<ISearchResult> referenceResults) {
    	SearchResultRanking ranking = new SearchResultRanking(MAXIMAL_PROPOSAL_NUMBER, referenceResults);
    	String[] items = searchQuery.split(" ");

    	if(items.length > 1) {
                // Itemset generation
    		String[] itemsets = generateItemSets(items);
        	for(int i = 0; i < itemsets.length; i++) {

        		List<ISearchResult> result = performSearch(itemsets[i].toString());
        		if(result != null) {
        			ranking.add(itemsets[i].toString(), result);
        		}
        	}
        	
        	// Replace OR by AND
        	String anotherQuery = replaceOrByAnd(searchQuery);
        	List <ISearchResult>  result = performSearch(anotherQuery);
        	if(result != null) {
        		ranking.add(anotherQuery, result);
        	}
    	} else {
            // Only one item and too less results... this must be handled separately

            // TODO: this is just a stub
    	}
    	return ranking;
    }
    
    /*
     * Generate n * (n-1)-itemsets out of the n-itemset (i.e.: three 2-itemsets for one 3-itemset)
     */
    private String[] generateItemSets(String[] items) {
    	String[] itemsets = new String[items.length];

    	// Generate queries
    	for (int i = 0; i < items.length; i++) {
            String s = "";
            for (int j = 0; j < items.length; j++) {
                if (i != j) {
                    s = s + items[j] + " ";
                }
            }
            itemsets[i] = s.trim();
    	}
    	return itemsets;
    }

    // TODO replace with Query.replaceOperator(...)
    private String replaceOrByAnd(String query) {
    	StringBuilder sb = new StringBuilder(query);
    	while(sb.indexOf("OR") > -1) {
    		sb.replace(sb.indexOf("OR"), sb.indexOf("OR") + 2, "AND");
    	}
    	return sb.toString();
    }
    
    /*
     * Generates the resulting tuple, containing the alterantive search requests and their hit counts.
     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>, <Query=NumberOfHits>:String*)
     */
    private Tuple generateTuple(String userId, String mission, String session, SearchResultRanking ranking) {
    	Tuple tuple = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), userId, SEARCH_TOOL, AGENT_NAME, mission, session);

    	for(Result result : ranking.getResults()) {
    		if(result != null) {
    			tuple.add(result.getQuery() + "=" + result.getResult().size());
    		}
    	}
    	return tuple;
    }
    
    /*
     * Performs a new search and responds with a key-value pair containing the 
     * search string as key and the number of hits as response.
     * Returns null if search timed out.
     */
    private List<ISearchResult> performSearch(String query) {    	
    	try {
			String uniqueID = new VMID().toString();
			Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME,
					ROOLO_AGENT_SEARCH, query);
			commandSpace.write(requestTuple);

			Tuple responseTuple = commandSpace.waitToTake(
					new Tuple(uniqueID, ROOLO_AGENT_RESPONSE,Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
			if(responseTuple != null) {
				return extractResult(responseTuple);
			}
    	} catch (TupleSpaceException ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    private List<ISearchResult> extractResult(Tuple responseTuple) {
    	String xmlResults = responseTuple.getField(2).getValue().toString();
    	return SearchResultUtils.createSearchResultsFromXML(xmlResults);
    }
    
}
