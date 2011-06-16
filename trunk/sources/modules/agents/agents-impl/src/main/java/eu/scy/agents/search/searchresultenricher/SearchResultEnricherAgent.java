package eu.scy.agents.search.searchresultenricher;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
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
import java.util.concurrent.atomic.AtomicInteger;

/*
 * This agent suggests other search queries, if the users search provided too less or too  many results.
 * It waits for a search query + search results coming from the roolo-agent and uses heuristics to enrich the search.
 * The enriched search queries will be written to the command space.
 * 
 */
public class SearchResultEnricherAgent extends AbstractThreadedAgent{
	
	private static final String ACTION_LOG_ATTRIBUTE_QUERY = "query";
	private static final String ACTION_LOG_ATTRIBUTE_RESULT = "search_results";

	// This
	private static final String AGENT_NAME = "search-result-enricher-agent";
	private static final String SEARCH_TOOL = "scy-simple-search";
	private static final String SEARCH_TYPE = "search-query";
	
	// RooloAccessorAgent
    private static final String ROOLO_AGENT_NAME = "roolo-agent";
    private static final String ROOLO_AGENT_RESPONSE = "roolo-response";
	private static final String ROOLO_AGENT_SEARCH = "search";
    private static final String ROOLO_AGENT_HIT_COUNT = "hit-count";
    private static final int ROOLO_AGENT_TIMEOUT = 5000;
    
    // TODO insert correct values
	private static final String ONTOLOGY_AGENT_NAME = "ontology-agent";
	private static final String ONTOLOGY_AGENT_COMMAND = "get synonyms";
	
	public static final String MAXIMAL_PROPOSAL_NUMBER = "max_proposal_number";
	public static final String MAXIMAL_REPLACE_NUMBER = "max_replace_number";
	public static final String INFIMUM_GOOD_SEARCH_INVERVAL = "inf_good_search_inverval";
	public static final String SUPREMUM_GOOD_SEARCH_INVERVAL = "sup_good_search_inverval";
	public static final String FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE = "fuzzy_search_sim_tolerance";
	public static final String FUZZY_SEARCH_RESULT_TOLERANCE = "fuzzy_search_result_tolerance";
        
    private static final Tuple TEMPLATE_FOR_SEARCH_QUERY = new Tuple("action", String.class, Long.class, SEARCH_TYPE, String.class, SEARCH_TOOL, Field.createWildCardField());
    
    
//    private static final Logger logger = Logger.getLogger(SearchResultEnricherAgent.class.getName());
    private Map<String, Object> config;

    private TupleSpace actionSpace;
    private TupleSpace commandSpace;
    
    private int maxProposalNumber;
    private int maxReplaceNumber;
    private int infGoodSearchInverval;
    private int supGoodSearchInterval;
    private double fuzzySearchTermSimilarityTolerance;
    private double fuzzySearchResultTolerance;

    
    public SearchResultEnricherAgent(Map<String, Object> map) {
        super(SearchResultEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        this.config = map;
        configure(map);

        try {
        	actionSpace = getActionSpace();
            commandSpace = getCommandSpace();
        	
            Callback sqc = new SearchQueryCallback();
            actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_SEARCH_QUERY, sqc, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    private void configure(Map<String, Object> config) {
        if(config.get(MAXIMAL_PROPOSAL_NUMBER) != null) {
        	this.maxProposalNumber = (Integer) config.get(MAXIMAL_PROPOSAL_NUMBER); 
        } else {
        	this.maxProposalNumber = 10;
        }
        if(config.get(MAXIMAL_REPLACE_NUMBER) != null) {
        	this.maxReplaceNumber= (Integer) config.get(MAXIMAL_REPLACE_NUMBER); 
        } else {
        	this.maxReplaceNumber = 5;
        }
        if(config.get(INFIMUM_GOOD_SEARCH_INVERVAL) != null) {
        	this.infGoodSearchInverval = (Integer) config.get(INFIMUM_GOOD_SEARCH_INVERVAL); 
        } else {
        	this.infGoodSearchInverval = 5;
        }
        if(config.get(SUPREMUM_GOOD_SEARCH_INVERVAL) != null) {
        	this.supGoodSearchInterval = (Integer) config.get(SUPREMUM_GOOD_SEARCH_INVERVAL); 
        } else {
        	this.supGoodSearchInterval = 20;
        }
        if(config.get(FUZZY_SEARCH_RESULT_TOLERANCE) != null) {
        	this.fuzzySearchResultTolerance = (Double) config.get(FUZZY_SEARCH_RESULT_TOLERANCE); 
        } else {
        	this.fuzzySearchResultTolerance = 0.8;
        }
        if(config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE) != null) {
        	this.fuzzySearchTermSimilarityTolerance = (Double) config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE); 
        } else {
        	this.fuzzySearchTermSimilarityTolerance = 0.8;
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
            evaluateSearchResult(afterTuple);
        }
    }
    
    /*
     * Evaluate a user search result and decides whether to propose an optimized search query.
     * If nothing needs to be enriched, an empty array will be returned.
     */
	public void evaluateSearchResult(Tuple tuple) {
		String userId = tuple.getField(1).getValue().toString();
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
//            logger.warn("Received action log with missing values! Query = " +
//                            props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) + " | Result = " +
//                            props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));
            System.out.println("Received action log with missing values! Query = " +
                            props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) + " | Result = " +
                            props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));
            return;
        }

        // Split search query in several items
        String searchQuery = props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY).trim();

        // use the results of the user query as reference
        List<ISearchResult> referenceResults = SearchResultUtils.createSearchResultsFromXML(props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));

        // Check search terms for spelling mistakes
    	try {
			List<String> misspelledTerms = checkSpelling(searchQuery.split(" "));
			if(misspelledTerms.size() > 0) {
				System.out.println("Misspelled terms: " + misspelledTerms.toString());
//				Tuple t = generateMisspelledTermsTuple(userId, mission, session, misspelledTerms);
//				commandSpace.write(tuple);
			}
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        SearchResultRanking ranking = null;
        // Choose strategy
        if (referenceResults.size() < this.infGoodSearchInverval) {
            ranking = extendSearchResult(user, eloUri, searchQuery, referenceResults);

        } else if (referenceResults.size() > this.supGoodSearchInterval) {
            ranking = pruneSearchResult(searchQuery, referenceResults);

        } else {
            // Number of results is ok, so we do nothing
        }
    
        // Propose search queries, if found
        if (ranking != null && ranking.getResults().length > 0) {
            System.out.println(ranking.toString());
            System.out.println(generateAlternativeQueriesTuple(user, mission, session, ranking));

//            try {
//                    commandSpace.write(generateResponseTuple(user, mission, session, ranking.getResults()));
//            } catch (TupleSpaceException e) {
//                    System.out.println(e);
//            }
        }
    }
    
	/*
	 * Checks for each term of the search query, whether we get much more search results 
	 * by performing a fuzzy search. This may indicate a spelling mistake. 
	 */
    private List<String> checkSpelling(String[] items) throws TupleSpaceException{
    	List<String> misspelledTerms = new ArrayList<String>();
    	for(String term : items) {
    		int exact = performSearchForHitCount(term);
    		int fuzzy = performSearchForHitCount(term + "~" + this.fuzzySearchTermSimilarityTolerance);
    		System.out.println("Term: " + term + " | Exact: " + exact + " | Fuzzy: " + fuzzy);
    		if(exact == -1 || fuzzy == -1) {
    			// RooloAccessorAgent down
    			break;
    		}
    		// Check if we get much more results while performing a fuzzy search.
    		// acceleration is the percent value, the hit number was boosted because of the fuzzy operator.
    		double acceleration = (((double)fuzzy + 1) / exact + 1);
    		if(acceleration > this.fuzzySearchResultTolerance) {
    			misspelledTerms.add(term);
    		}
    	}
    	return misspelledTerms;
    }

    /*
     * Generates a tuple, containing the probably misspelled terms.
     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String, <terms=term1 term2 term3>:String)
     */
    private Tuple generateMisspelledTermsTuple(String userId, String mission, String session, List<String> terms) {
    	String s = "terms=";
    	for(String term : terms) {
    		s = s + term + " ";
    	}
    	return new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), userId, SEARCH_TOOL, AGENT_NAME, mission, session, s.trim());
    }
    
    /*
     * Tries to create another search query, that will provide more results.
     */
    private SearchResultRanking extendSearchResult(String userId, String eloUri, String searchQuery, List<ISearchResult> referenceResults) {
        SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);

        try
        {
            // Replace AND by OR
            Query query = Query.parse(searchQuery);
            String last = "";
            for(int i = 1; i <= this.maxReplaceNumber; i++) {
                String newQuery = query.replaceOperator("AND", "OR", new AtomicInteger(i));
                if(newQuery.equals(last)) {
                    // if a replace operation does not change the query anymore, stop
                    break;
                } else {
                    last = newQuery;
                    List<ISearchResult> result =  performSearch(newQuery);
                    if(result != null) {
                        ranking.add(newQuery, result);
                    }
                }
            }
            
        	// TODO ask ontology for synonyms
        	askOntologyForSynonym(userId, eloUri, "keyword");
        } catch (TupleSpaceException e) {
        	e.printStackTrace();
        }
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
    	SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);
    	String[] items = searchQuery.split(" ");

    	try {
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
	            Query query = Query.parse(searchQuery);
	            String last = "";
	            AtomicInteger ai = new AtomicInteger(0);
	            for(int i = 1; i <= this.maxReplaceNumber; i++) {
	                ai.set(i);
	                String newQuery = query.replaceOperator("OR", "AND", ai);
	                if(newQuery.equals(last)) {
	                    // if a replace operation does not change the query anymore, stop
	                    break;
	                } else {
	                    last = newQuery;
	                    List<ISearchResult> result =  performSearch(newQuery);
	                    if(result != null) {
	                        ranking.add(newQuery, result);
	                    }
	                }
	            }
	    	} else {
	            // Only one item and too less results... this must be handled separately
	
	            // TODO: this is just a stub
	    	}
    	} catch (TupleSpaceException e) {
    		e.printStackTrace();
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
    
    /*
     * Generates the resulting tuple, containing the alterantive search requests and their hit counts.
     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>, <Query=NumberOfHits>:String*)
     */
    private Tuple generateAlternativeQueriesTuple(String userId, String mission, String session, SearchResultRanking ranking) {
    	Tuple tuple = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), userId, SEARCH_TOOL, AGENT_NAME, mission, session);

    	for(Result result : ranking.getResults()) {
    		if(result != null) {
    			tuple.add(result.getQuery() + "=" + result.getResult().size());
    		}
    	}
    	return tuple;
    }
    
    private int performSearchForHitCount(String query) throws TupleSpaceException {
		String uniqueID = new VMID().toString();
		Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_HIT_COUNT, query);
		commandSpace.write(requestTuple);

		Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID,
				ROOLO_AGENT_RESPONSE, Field.createWildCardField()),
				ROOLO_AGENT_TIMEOUT);
		if (responseTuple != null) {
			return Integer.parseInt(responseTuple.getField(2).getValue().toString());
		}
		return -1;
    }
    
    /*
     * Performs a new search and responds with a key-value pair containing the 
     * search string as key and the number of hits as response.
     * Returns null if search timed out.
     */
    private List<ISearchResult> performSearch(String query) throws TupleSpaceException {    	
		String uniqueID = new VMID().toString();
		Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_SEARCH, query);
		commandSpace.write(requestTuple);

		Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, 
				Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
		if (responseTuple != null) {
			String xmlResults = responseTuple.getField(2).getValue().toString();
			return SearchResultUtils.createSearchResultsFromXML(xmlResults);
		} else {
			return null;
		}
    }
}
