package eu.scy.agents.search.searchresultenricher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
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
	
    private static final Tuple TEMPLATE_FOR_SEARCH_QUERY = new Tuple("action", String.class, Long.class, SEARCH_TYPE, String.class, SEARCH_TOOL, Field.createWildCardField());
    
    private static final String OPERATOR_AND = "AND";
    private static final String OPERATOR_OR = "OR";
    
	// Configuration values
    
    /**
     * This value determines the number of alternative search queries, which will be proposed.
     */
	public static final String MAXIMAL_PROPOSAL_NUMBER = "max_proposal_number";
	
	/**
	 * This configuration value determines the maximal number of operator replacements.
	 * It is used to extend search results (by replacing AND by OR) as well as to prune search
	 * results (by replacing OR by AND).
	 */
	public static final String MAXIMAL_REPLACE_NUMBER = "max_replace_number";
	
	/**
	 * This configuration value determines the lower bound of the good search result interval.
	 * Search queries with less results than this value will be considered as bad queries
	 * and will force a search result extension.
	 */
	public static final String INFIMUM_GOOD_SEARCH_INVERVAL = "inf_good_search_inverval";
	
	/**
	 * This configuration value determines the upper bound of the good search result interval.
	 * Search queries with more results than this value will be considered as bad queries
	 * and will force a search result pruning.
	 */
	public static final String SUPREMUM_GOOD_SEARCH_INVERVAL = "sup_good_search_inverval";
	
	/**
	 * This configuration value is used for misspell checking and configures the lucene fuzzy operator.
	 * A value of 0.8 results in the lucene query: "term~0.8"
	 */
	public static final String FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE = "fuzzy_search_sim_tolerance";
	
	/**
	 * This configuration value is used for misspell checking.
	 * A term is checked both without and with fuzzy operator and the hit results will be compared.
	 * This value sets the factor, the hit number with fuzzy operator must be higher, to assume that the
	 * term is misspelled.
	 * The value must be greater than 1.
	 */
	public static final String FUZZY_SEARCH_RESULT_TOLERANCE = "fuzzy_search_result_tolerance";
	
	/**
	 * This configuration value is used for extracting new keywords from the users search results.
	 * This value must be >= 0 and determines the number of search results used for keyword extraction.
	 * The agent will use the search results which are best rated by lucene.
	 */
	public static final String EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION = "extract_keyword_result_number_used_for_extraction";
    
	/**
	 * This configuration value is used for extracting new keywords from the users search results.
	 * This value must be >= 0 and determines the number of extracted terms, which will be tested
	 * by concatenating them with the users search query.
	 */
	public static final String EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST = "extact_keyword_term_number_to_test";
    
	public static final Locale ESTONIA_LOCALE = new Locale("et", "EE");
	
//    private static final Logger logger = Logger.getLogger(SearchResultEnricherAgent.class.getName());
    private Map<String, Object> config;
    
    private Map<Locale, Set<String>> stopwordMap = null;

    private TupleSpace actionSpace;
    private TupleSpace commandSpace;
    
    private int maxProposalNumber;
    private int maxReplaceNumber;
    private int infGoodSearchInverval;
    private int supGoodSearchInterval;
    private double fuzzySearchTermSimilarityTolerance;
    private double fuzzySearchResultTolerance;
    
    private int numberOfResultsUsedForExtension;
    private int numberOfTermsToTest;

    
    public SearchResultEnricherAgent(Map<String, Object> map) {
        super(SearchResultEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), 
        		(String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        this.config = map;
        configure(map);

		// TODO: keyword files should be set by configuration
        this.stopwordMap = new HashMap<Locale, Set<String>>();
        try {
			this.stopwordMap.put(Locale.ENGLISH, readStopwords("/en_stopwords.txt"));
			this.stopwordMap.put(Locale.GERMAN, readStopwords("/ger_stopwords.txt"));
			this.stopwordMap.put(ESTONIA_LOCALE, readStopwords("/est_stopwords.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

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
        	if(this.fuzzySearchResultTolerance < 1) {
        		this.fuzzySearchResultTolerance = 1.5;
        	}
        } else {
        	this.fuzzySearchResultTolerance = 1.5;
        }
        if(config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE) != null) {
        	this.fuzzySearchTermSimilarityTolerance = (Double) config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE); 
        } else {
        	this.fuzzySearchTermSimilarityTolerance = 0.8;
        }
        if(config.get(EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION) != null) {
        	this.numberOfResultsUsedForExtension = (Integer) config.get(EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION); 
        } else {
        	this.numberOfResultsUsedForExtension = 10;
        }
        if(config.get(EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST) != null) {
        	this.numberOfTermsToTest = (Integer) config.get(EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST); 
        } else {
        	this.numberOfTermsToTest = 5;
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
     * Performs a new search and responds with a key-value pair containing the 
     * search string as key and the number of hits as response.
     * Returns null if search timed out.
     */
    private static List<ISearchResult> getHits(TupleSpace commandSpace, String query) throws TupleSpaceException {    	
		String uniqueID = new VMID().toString();
		String luceneQuery = "contents:\"" + query + "\"";
		Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_SEARCH, luceneQuery);
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

    private static int getHitCount(TupleSpace commandSpace, String query) throws TupleSpaceException {
		String uniqueID = new VMID().toString();
		String luceneQuery = "contents:\"" + query + "\"";
		Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_HIT_COUNT, luceneQuery);
		commandSpace.write(requestTuple);

		Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID,
				ROOLO_AGENT_RESPONSE, Field.createWildCardField()),
				ROOLO_AGENT_TIMEOUT);
		if (responseTuple != null) {
			return Integer.parseInt(responseTuple.getField(2).getValue().toString());
		} else {
        	// RooloAccessorAgent down
			return -1;
		}
    }

    /**
     * Parses the query, that was received from TupleSpaces (in Lucene syntax) and extracts the user-entered query 
     * @param rawQuery
     * @return
     */
    private String parseQuery(String rawQuery) {
        rawQuery = rawQuery.trim();
        int queryStartPos = rawQuery.indexOf('"') + 1;
        int queryStopPos = rawQuery.lastIndexOf('"');
        return rawQuery.substring(queryStartPos, queryStopPos);
    }
    
    private Set<String> readStopwords(String stopwordFileName) throws IOException {
    	HashSet<String> stopwords = new HashSet<String>();
    	InputStream is = getClass().getResourceAsStream(stopwordFileName);
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    String buffer = null;
	    while ((buffer = br.readLine()) != null) {
	        try {
	            stopwords.add(buffer.trim());
	        } catch (StringIndexOutOfBoundsException e) {
	            e.printStackTrace();
	        }
	    }
	    br.close();
    	return stopwords;
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
        	props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY).equals("contents:\"\"") || props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT) == null) {
        	
        	// TODO: Uncomment if finished
//            logger.warn("Received action log with missing values! Query = " +
//                            props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));
            System.out.println("Received action log but some values are missing! Query = " +
                            props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));
            return;
        }

        String searchQuery = parseQuery(props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));

        // Deserialize search results
        List<ISearchResult> referenceResults = SearchResultUtils.createSearchResultsFromXML(props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));

        // Spelling disabled, due to missing fuzzy operator support
        // Check search terms for spelling mistakes... this task is independent from the rest
		SpellingChecker sc = new SpellingChecker(this.commandSpace, user, mission,
				session, searchQuery.split(" "),
				this.fuzzySearchTermSimilarityTolerance,
				this.fuzzySearchResultTolerance);
        new Thread(sc).start();
    	
        // Choose strategy
        SearchResultRanking ranking = null;
        if (referenceResults.size() < this.infGoodSearchInverval) {
            ranking = extendSearchResult(user, eloUri, mission, searchQuery, referenceResults);
        } else if (referenceResults.size() > this.supGoodSearchInterval) {
            ranking = pruneSearchResult(searchQuery, referenceResults);
        } else {
            // Number of results is ok, so we do nothing
        }
    
        // Propose search queries, if found
        if (ranking != null && ranking.getResults().length > 0) {
            System.out.println(ranking.toString());
            System.out.println(generateAlternativeQueriesTuple(user, mission, session, ranking));

            // TODO: Uncomment if finished
//            try {
//                    commandSpace.write(generateAlternativeQueriesTuple(user, mission, session, ranking));
//            } catch (TupleSpaceException e) {
//                    System.out.println(e);
//            }
        }
    }
    

	//
	// STRATEGY: EXTEND SEARCH RESULTS (TOO LESS RESULTS)
	//
	
    /*
     * Tries to create another search query, that will provide more results.
     */
    private SearchResultRanking extendSearchResult(String userId, String eloUri, String mission, String searchQuery, List<ISearchResult> referenceResults) {
    	
        SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);
        try
        {
            // Replace AND by OR
            Query query = Query.parse(searchQuery);
            String last = "";
            for(int i = 1; i <= this.maxReplaceNumber; i++) {
                String newQuery = query.replaceOperator(OPERATOR_AND, OPERATOR_OR, new AtomicInteger(i));
                if(newQuery.equals(last)) {
                    // if a replace operation does not change the query anymore, stop
                    break;
                } else {
                    last = newQuery;
                    List<ISearchResult> result =  getHits(commandSpace, newQuery);
                    if(result != null) {
                        ranking.add(newQuery, result);
                    } else {
                    	// RooloAccessorAgent down, so stop
                    	return ranking;
                    }
                }
            }
            
            // Add new keywords extracted from the search results.
            Set<String> items = new HashSet<String>();
            items.addAll(Arrays.asList(searchQuery.split(" ")));
            List<TermEntry> termList = extractKeywordsFromSearchResults(items, Locale.ENGLISH, referenceResults);
            for(int i = 0; i < Math.min(this.numberOfTermsToTest, termList.size()); i++){
            	String newQuery = searchQuery + " " + termList.get(i).getCount();
            	List<ISearchResult> result = getHits(commandSpace, newQuery);
            	if(result != null) {
            		ranking.add(newQuery, result);
            	} else {
            		// RooloAccessorAgent down, so stop
                	return ranking;
            	}
            }
            	
            
        	// TODO ask ontology for synonyms
        	askOntologyForSynonym(mission, eloUri, "keyword");
        } catch (TupleSpaceException e) {
        	e.printStackTrace();
        }
        return ranking;
    }

    /**
     * Asks the ontology for synonyms
     * @param userId
     * @param eloUri
     * @param keyword
     * @return
     */
    private void askOntologyForSynonym(String mission, String eloUri, String keyword) {
    	// (<ID>:String, "onto":String, "surrounding":String, <OntName>:String, <OntLabel>:String, 
    	// <Language>:String) -> (<ID>:String, <OntTerm>:String, <Surrounding>:String)
    	System.out.println(mission);
//        try {
//            String id = new VMID().toString();
//            Tuple requestTuple = new Tuple(id, "onto", "surrounding", mission, keyword, "en");
//            this.commandSpace.write(requestTuple);
//            Tuple responseTuple = this.commandSpace.waitToTake(new Tuple(id, Field.createWildCardField()));
//            if(responseTuple != null) {
//            String ontTerm = (String)responseTuple.getField(1).getValue();
//            String surrounding = (String)responseTuple.getField(2).getValue();
//            System.out.println(ontTerm);
//            System.out.println(surrounding);
//            }
//        } catch (TupleSpaceException e) {
//            e.printStackTrace();
//        }
	}

    /*
     * Uses the search results of the origin search to extract terms, that might be of interest.
     */
    @SuppressWarnings("unchecked")
	private List<TermEntry> extractKeywordsFromSearchResults(Set<String> items, Locale locale, List<ISearchResult> results) {
    	List<TermEntry> termList = new LinkedList<TermEntry>();

    	// Grab all terms and put them in a bag.
    	Bag bag = new HashBag();
    	int resultsUsed = 0;
    	for(int i = 0; i < results.size() ; i++) {
    		// Keyword list may contains relevance entries, so we need to delete them

    		String keywords = results.get(i).getTitle(locale);
    		if(keywords == null || keywords.equals("")){
    			continue;
    		}
    		keywords = keywords.toLowerCase();
    		keywords = keywords.replaceAll(" [0-9]\\.[0-9]", "");
    		List<String> terms = Arrays.asList(keywords.split(" "));
    		bag.addAll(terms);
    		resultsUsed++;
    		if(resultsUsed == this.numberOfResultsUsedForExtension) {
    			break;
    		}
    	}
    	@SuppressWarnings("rawtypes")
		Iterator bagIterator = bag.iterator();
    	String last = "";
    	while(bagIterator.hasNext()) {
    		String term = (String)bagIterator.next();
    		
    		if(term.equals(last)) {
    			// Bag contains duplicate entries...
    			continue;
    		}
    		if(stopwordMap.get(locale).contains(term)) {
    			// Keyword is in stopword list
    			continue;
    		}
    		if(items.contains(term)) {
    			// Keyword is in user query
    			continue;
    		}
    		
    		last = term;
    		int termCount = bag.getCount(term);
    		termList.add(new TermEntry(term, termCount));
    	}
    	Collections.sort(termList);
    	System.out.println(termList);
    	return termList;
    }

    class TermEntry implements Comparable<TermEntry> {
    	
    	private String term;
    	private int count;
    	
    	TermEntry(String term, int count) {
    		this.term = term;
    		this.count = count;
    	}

		@Override
		public int compareTo(TermEntry o) {
			return o.getCount() - getCount();
		}

		public String getTerm() {
			return this.term;
		}

		public int getCount() {
			return this.count;
		}
		
		@Override
		public String toString() {
			return this.term + " (Count: " + this.count + ")";
		}
    }
    
	//
	// STRATEGY: PRUNE SEARCH RESULTS (TOO MANY RESULTS)
	//

    /**
     * Tries to prune the search result.
     */
	private SearchResultRanking pruneSearchResult(String searchQuery, List<ISearchResult> referenceResults) {
    	SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);
    	Set<String> items = new HashSet<String>();
    	items.addAll(Arrays.asList(searchQuery.split(" ")));

    	try {
	    	if(items.size() > 1) {
	    		// Itemset generation
	    		String[] itemsets = generateItemSets(items);
	        	for(int i = 0; i < itemsets.length; i++) {
	
	        		List<ISearchResult> result = getHits(commandSpace, itemsets[i].toString());
	        		if(result != null) {
	        			ranking.add(itemsets[i].toString(), result);
	        		} else {
                    	// RooloAccessorAgent down, so stop
                    	return ranking;
	        		}
	        	}
	        	
	            // Replace OR by AND
	            Query query = Query.parse(searchQuery);
	            String last = "";
	            AtomicInteger ai = new AtomicInteger(0);
	            for(int i = 1; i <= this.maxReplaceNumber; i++) {
	                ai.set(i);
	                String newQuery = query.replaceOperator(OPERATOR_OR, OPERATOR_AND, ai);
	                if(newQuery.equals(last)) {
	                    // if a replace operation does not change the query anymore, stop
	                    break;
	                } else {
	                    last = newQuery;
	                    List<ISearchResult> result =  getHits(commandSpace, newQuery);
	                    if(result != null) {
	                        ranking.add(newQuery, result);
	                    } else {
	                    	// RooloAccessorAgent down, so stop
	                    	return ranking;	                    	
	                    }
	                }
	            }
	    	}
            // Only one item and too less results... this must be handled separately

            List<TermEntry> termList = extractKeywordsFromSearchResults(items, Locale.ENGLISH, referenceResults);
            for(int i = 0; i < Math.min(this.numberOfTermsToTest, termList.size()); i++){
            	String newQuery = searchQuery + " " + OPERATOR_AND + " " + termList.get(i).getTerm();
            	List<ISearchResult> result = getHits(commandSpace, newQuery);
            	if(result != null) {
            		ranking.add(newQuery, result);
            	} else {
            		// RooloAccessorAgent down, so stop
                	return ranking;
            	}
            }
    	} catch (TupleSpaceException e) {
    		e.printStackTrace();
    	}
    	return ranking;
    }

	/**
     * Generate n * (n-1)-itemsets out of the n-itemset (i.e.: three 2-itemsets for one 3-itemset)
	 * @param items
	 * @return
	 */
    private String[] generateItemSets(Set<String> items) {
    	String[] itemsets = new String[items.size()];

    	String[] array = items.toArray(new String[0]);
    	// Generate queries
    	for (int i = 0; i < array.length; i++) {
            String s = "";
            for (int j = 0; j < array.length; j++) {
                if (i != j) {
                    s = s + array[j] + " ";
                }
            }
            itemsets[i] = s.trim();
    	}
    	return itemsets;
    }

    /**
     * Generates the resulting tuple, containing the alterantive search requests and their hit counts.
     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>, <Query=NumberOfHits>:String*)
     * @param userId
     * @param mission
     * @param session
     * @param ranking
     * @return
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
    
    
	/*
	 * Checks the spelling and writes a tuple to the TS if a word seems to be misspelled.
	 */
	class SpellingChecker implements Runnable {

		private TupleSpace commandSpace;
		private String userId;
		private String mission;
		private String session;
		private String[] items;
		private double termSimilarityTolerance;
		private double searchResultTolerance;
		
		SpellingChecker(TupleSpace commandSpace, String userId, String mission, String session, String[] items, double termSimilarityTolerance, double searchResultTolerance ) {
			this.commandSpace = commandSpace;
			this.userId = userId;
			this.mission = mission;
			this.session = session;
			this.items = items;
			this.termSimilarityTolerance = termSimilarityTolerance;
			this.searchResultTolerance = searchResultTolerance;
		}
				
		@Override
		public void run() {
	    	try {
				List<String> misspelledTerms = checkSpelling(this.items);
				if(misspelledTerms.size() > 0) {
					System.out.println("Misspelled terms: " + misspelledTerms.toString());
					
					// TODO: Uncomment if finished
//					Tuple tuple = generateMisspelledTermsTuple(misspelledTerms);
//					commandSpace.write(tuple);
				} else {
					System.out.println("No misspelled terms.");
				}
			} catch (TupleSpaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Checks for each term of the search query, whether we get much more search results 
		 * by performing a fuzzy search. This may indicate a spelling mistake. 
		 * @param items
		 * @return
		 * @throws TupleSpaceException
		 */
	    private List<String> checkSpelling(String[] items) throws TupleSpaceException{
	    	List<String> misspelledTerms = new ArrayList<String>();
	    	for(String term : items) {
	    		
	    		int exact = SearchResultEnricherAgent.getHitCount(commandSpace, term);
	    		if(exact == -1) {
	    			// RooloAccessorAgent down, so we stop checking
	    			return misspelledTerms;
	    		}

	    		int fuzzy = getHitCount(commandSpace, term + "~" + this.termSimilarityTolerance);
	    		if(fuzzy == -1) {
	    			// RooloAccessorAgent down, so we stop checking
	    			return misspelledTerms;
	    		}
	    		
	    		System.out.println("Term: " + term + " | Exact: " + exact + " | Fuzzy: " + fuzzy);
	    		// Check if we get much more results while performing a fuzzy search.
	    		// acceleration is the percent value, the hit number was boosted because of the fuzzy operator.
	    		if(exact == 0 && fuzzy == 0) {
	    			continue;
	    		} else if(exact == 0 && fuzzy > 0) {
	    			misspelledTerms.add(term);
	    		} else {
	    			double acceleration = (((double)fuzzy) / exact);
	    			if(acceleration > this.searchResultTolerance) {
	    				misspelledTerms.add(term);
	    			}
	    		}
	    	}
	    	return misspelledTerms;
	    }

	    /**
	     * Generates a tuple, containing the probably misspelled terms.
	     * ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String, <terms=term1 term2 term3>:String)
	     * @param terms
	     * @return
	     */
	    private Tuple generateMisspelledTermsTuple(List<String> terms) {
	    	String s = "terms=";
	    	for(String term : terms) {
	    		s = s + term + " ";
	    	}
	    	return new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), this.userId, SEARCH_TOOL, AGENT_NAME, this.mission, this.session, s.trim());
	    }
	}
}
