package eu.scy.agents.search.searchresultenricher;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import roolo.search.ISearchResult;
import roolo.search.util.SearchResultUtils;
import eu.scy.agents.Mission;
import eu.scy.agents.SCYAbstractThreadedAgent;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

/*
 * This agent suggests other search queries, if the users search provided too less or too  many results.
 * It waits for a search query + search results coming from the roolo-agent and uses heuristics to enrich the search.
 * The enriched search queries will be written to the command space.
 * 
 */
public class SearchResultEnricherAgent extends SCYAbstractThreadedAgent {

    private static final String ACTION_LOG_ATTRIBUTE_QUERY = "query";

    private static final String ACTION_LOG_ATTRIBUTE_RESULT = "search_results";

    // This
    private static final String AGENT_NAME = "search-result-enricher-agent";

    private static final String SEARCH_TOOL = "search";

    private static final String SEARCH_TYPE = "search-query";

    // RooloAccessorAgent
    private static final String ROOLO_AGENT_NAME = "roolo-agent";

    private static final String ROOLO_AGENT_RESPONSE = "roolo-response";

    private static final String ROOLO_AGENT_SEARCH = "search";

    private static final String ROOLO_AGENT_HIT_COUNT = "hit-count";

    private static final int ROOLO_AGENT_TIMEOUT = 5000;

    // OntologyAgent
    private static final String ONTOLOGY_AGENT_NAME = "onto";

    private static final String ONTOLOGY_AGENT_COMMAND = "surrounding";

    private static final int ONTOLOGY_AGENT_TIMEOUT = 20000;

    private static final Tuple TEMPLATE_FOR_SEARCH_QUERY = new Tuple("action", String.class, Long.class, SEARCH_TYPE, String.class, SEARCH_TOOL, Field.createWildCardField());

    private static final Tuple TEMPLATE_FOR_SESSION_LANGUAGE = new Tuple("language", String.class, String.class);

    private static final Tuple TEMPLATE_FOR_MISSION = new Tuple("mission", String.class, String.class, String.class, String.class, String.class);

    private static final String OPERATOR_AND = "AND";

    private static final String OPERATOR_OR = "OR";

    private static final String DEFAULT_FIELD = "contents";

    private static final String LANGUAGE_PREFIX = "stopwords";

    // Configuration values
    /**
     * This value determines the number of alternative search queries, which will be proposed.
     */
    public static final String MAXIMAL_PROPOSAL_NUMBER = "max_proposal_number";

    /**
     * This configuration value determines the maximal number of operator replacements. It is used
     * to extend search results (by replacing AND by OR) as well as to prune search results (by
     * replacing OR by AND).
     */
    public static final String MAXIMAL_REPLACE_NUMBER = "max_replace_number";

    /**
     * This configuration value determines the lower bound of the good search result interval.
     * Search queries with less results than this value will be considered as bad queries and will
     * force a search result extension.
     */
    public static final String INFIMUM_GOOD_SEARCH_INVERVAL = "inf_good_search_inverval";

    /**
     * This configuration value determines the upper bound of the good search result interval.
     * Search queries with more results than this value will be considered as bad queries and will
     * force a search result pruning.
     */
    public static final String SUPREMUM_GOOD_SEARCH_INVERVAL = "sup_good_search_inverval";

    /**
     * This configuration value is used for misspell checking and configures the lucene fuzzy
     * operator. A value of 0.8 results in the lucene query: "term~0.8"
     */
    public static final String FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE = "fuzzy_search_sim_tolerance";

    /**
     * This configuration value is used for misspell checking. A term is checked both without and
     * with fuzzy operator and the hit results will be compared. This value sets the factor, the hit
     * number with fuzzy operator must be higher, to assume that the term is misspelled. The value
     * must be greater than 1.
     */
    public static final String FUZZY_SEARCH_RESULT_TOLERANCE = "fuzzy_search_result_tolerance";

    /**
     * This configuration value is used for extracting new keywords from the users search results.
     * This value must be >= 0 and determines the number of search results used for keyword
     * extraction. The agent will use the search results which are best rated by lucene.
     */
    public static final String EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION = "extract_keyword_result_number_used_for_extraction";

    /**
     * This configuration value is used for extracting new keywords from the users search results.
     * This value must be >= 0 and determines the number of extracted terms, which will be tested by
     * concatenating them with the users search query.
     */
    public static final String EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST = "extact_keyword_term_number_to_test";

    /**
     * This configuration value is used for extraction new keyword by asking the ontology. This
     * value must be >= 0 and determines the maximal number of words to use.
     */
    public static final String MAX_ONTOLOGY_KEYWORDS = "max_ontology_keywords";

    private static final Logger logger = Logger.getLogger(SearchResultEnricherAgent.class.getName());

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Map<String, Object> config;

    private Map<String, Set<String>> stopwordMap = null;

    private TupleSpace actionSpace;

    private TupleSpace commandSpace;

    private TupleSpace sessionSpace;

    private int maxProposalNumber;

    private int maxReplaceNumber;

    private int infGoodSearchInverval;

    private int supGoodSearchInterval;

    private double fuzzySearchTermSimilarityTolerance;

    private double fuzzySearchResultTolerance;

    private int maxOntologyKeywordNumber;

    private int numberOfResultsUsedForExtension;

    private int numberOfTermsToTest;

    private HashMap<String, String> currentSearches;

    public SearchResultEnricherAgent(Map<String, Object> map) {
        super(SearchResultEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        this.stopwordMap = new HashMap<String, Set<String>>();
        this.config = map;

        currentSearches = new HashMap<String, String>();
        // TODO keyword files should be set by configuration
        this.config.put(LANGUAGE_PREFIX + "_en", "/en_stopwords.txt");
        this.config.put(LANGUAGE_PREFIX + "_de", "/de_stopwords.txt");
        this.config.put(LANGUAGE_PREFIX + "_es", "/et_stopwords.txt");
        this.config.put(LANGUAGE_PREFIX + "_fr", "/fr_stopwords.txt");

        configure(map);

        try {
            actionSpace = getActionSpace();
            commandSpace = getCommandSpace();
            sessionSpace = getSessionSpace();

            Callback sqc = new SearchQueryCallback();
            actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_SEARCH_QUERY, sqc, true);
        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        System.out.println("Search enricher started up");
    }

    private void configure(Map<String, Object> config) {
        if (config.get(MAXIMAL_PROPOSAL_NUMBER) != null) {
            this.maxProposalNumber = (Integer) config.get(MAXIMAL_PROPOSAL_NUMBER);
        } else {
            this.maxProposalNumber = 10;
        }
        if (config.get(MAXIMAL_REPLACE_NUMBER) != null) {
            this.maxReplaceNumber = (Integer) config.get(MAXIMAL_REPLACE_NUMBER);
        } else {
            this.maxReplaceNumber = 10;
        }
        if (config.get(INFIMUM_GOOD_SEARCH_INVERVAL) != null) {
            this.infGoodSearchInverval = (Integer) config.get(INFIMUM_GOOD_SEARCH_INVERVAL);
        } else {
            this.infGoodSearchInverval = 10;
        }
        if (config.get(SUPREMUM_GOOD_SEARCH_INVERVAL) != null) {
            this.supGoodSearchInterval = (Integer) config.get(SUPREMUM_GOOD_SEARCH_INVERVAL);
        } else {
            this.supGoodSearchInterval = 60; // XXX deactivated
        }
        if (config.get(FUZZY_SEARCH_RESULT_TOLERANCE) != null) {
            this.fuzzySearchResultTolerance = (Double) config.get(FUZZY_SEARCH_RESULT_TOLERANCE);
            if (this.fuzzySearchResultTolerance < 1) {
                this.fuzzySearchResultTolerance = 1.5;
            }
        } else {
            this.fuzzySearchResultTolerance = 1.5;
        }
        if (config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE) != null) {
            this.fuzzySearchTermSimilarityTolerance = (Double) config.get(FUZZY_SEARCH_TERM_SIMILARITY_TOLERANCE);
        } else {
            this.fuzzySearchTermSimilarityTolerance = 0.8;
        }
        if (config.get(EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION) != null) {
            this.numberOfResultsUsedForExtension = (Integer) config.get(EXTRACT_KEYWORD_RESULT_NUMBER_USED_FOR_EXTRACTION);
        } else {
            this.numberOfResultsUsedForExtension = 10;
        }
        if (config.get(EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST) != null) {
            this.numberOfTermsToTest = (Integer) config.get(EXTRACT_KEYWORD_TERM_NUMBER_TO_TEST);
        } else {
            this.numberOfTermsToTest = 5;
        }
        if (config.get(MAX_ONTOLOGY_KEYWORDS) != null) {
            this.maxOntologyKeywordNumber = (Integer) config.get(MAX_ONTOLOGY_KEYWORDS);
        } else {
            this.maxOntologyKeywordNumber = 5;
        }
        for (String lang : Locale.getISOLanguages()) {
            try {
                if (config.containsKey(LANGUAGE_PREFIX + "_" + lang)) {
                    this.stopwordMap.put(lang, readStopwords((String) config.get(LANGUAGE_PREFIX+ "_" + lang)));
                }
            } catch (IOException e) {
                logger.debug("Could not open stopword file for language: " + lang);
                // System.out.println("Could not open stopword file for language: " + lang);
            }
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        // System.out.println(AGENT_NAME + " started");
        logger.debug(AGENT_NAME + " started");
        while (status == Status.Running) {
            try {
                sendAliveUpdate();
                Thread.sleep(AgentProtocol.COMMAND_EXPIRATION);
            } catch (TupleSpaceException e) {
                // e.printStackTrace();
                logger.error("Connection to TupleSpace lost!", e);
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
            // System.out.println(AGENT_NAME + " stopped");
            logger.debug(AGENT_NAME + " stopped");
        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
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

    /**
     * Performs a new search and responds with a key-value pair containing the search string as key
     * and the number of hits as response. Returns null if search timed out.
     * 
     * @param commandSpace
     *            the command space
     * @param query
     *            the query
     * @return the hits
     * @throws TupleSpaceException
     *             the tuple space exception
     */
    private static List<ISearchResult> getHits(TupleSpace commandSpace, String query) throws TupleSpaceException {
        String uniqueID = new VMID().toString();
        Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_SEARCH, query);
        commandSpace.write(requestTuple);

        Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
        if (responseTuple != null) {
            String xmlResults = responseTuple.getField(2).getValue().toString();
            List<ISearchResult> result = SearchResultUtils.createSearchResultsFromXML(xmlResults);
            System.out.println("Query '" + query + "' got " + result.size());
            return result;
        } else {
            return null;
        }
    }

    /**
     * Performs a new search responds with the number of hits. Returns -1 if RooloAccessorAgent is
     * down.
     * 
     * @param commandSpace
     *            the command space
     * @param query
     *            the query
     * @return the hit count
     * @throws TupleSpaceException
     *             the tuple space exception
     */
    private static int getHitCount(TupleSpace commandSpace, String query) throws TupleSpaceException {
        String uniqueID = new VMID().toString();
        Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_HIT_COUNT, query);
        commandSpace.write(requestTuple);

        Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
        if (responseTuple != null) {
            return Integer.parseInt(responseTuple.getField(2).getValue().toString());
        } else {
            // RooloAccessorAgent down
            return -1;
        }
    }

    private String getLanguage(String user) {
        String language = null;
        try {
            for (Tuple languageTuple : sessionSpace.readAll(TEMPLATE_FOR_SESSION_LANGUAGE)) {
                if (languageTuple.getFields().length >= 3 && languageTuple.getField(1).getValue().toString().startsWith(user)) {
                    language = languageTuple.getField(2).getValue().toString();
                    break;
                }
            }
        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        return language;
    }

    // necessary for ontology agent
    private String getMission(String user) {
        String mission = null;
        try {
            for (Tuple languageTuple : sessionSpace.readAll(TEMPLATE_FOR_MISSION)) {
                if (languageTuple.getFields().length >= 4 && languageTuple.getField(1).getValue().toString().startsWith(user)) {
                    mission = languageTuple.getField(3).getValue().toString();
                    break;
                }
            }
        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        return mission;
    }

    /**
     * Parses the query, that was received from TupleSpaces (in Lucene syntax) and extracts the
     * user-entered query
     * 
     * @param rawQuery
     * @return
     */
    private String parseQuery(String rawQuery) {
        // TODO: parsing won't work on contents:(bla bla)
        String startMark = "contents:(*";
        String finishMark = "*)";
        int queryStartPos = rawQuery.indexOf(startMark);
        int queryStopPos = rawQuery.lastIndexOf(finishMark);
        if (queryStartPos == -1 || queryStartPos == queryStopPos) {
            return rawQuery.replaceAll("\"", "");
        }
        String result = rawQuery.substring(queryStartPos + startMark.length(), queryStopPos);
        return result;
    }

    /**
     * Reads a set of stopwords from a file.
     * 
     * @param stopwordFileName
     * @return
     * @throws IOException
     */
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

    /**
     * Evaluate a user search result and decides whether to propose an optimized search query. If
     * nothing needs to be enriched, an empty array will be returned.
     * 
     * @param tuple
     *            the tuple
     */
    private void evaluateSearchResult(Tuple tuple) {
        // String userId = tuple.getField(1).getValue().toString();
        // String eloUri = tuple.getField(8).getValue().toString();
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
        if (props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY) == null || props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY).equals("") || props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY).equals("contents:\"\"") || props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT) == null) {

            logger.warn("Received action log but some values are missing! Query = " + props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));
            // System.out.println("Received action log but some values are missing! Query = " +
            // props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));
            return;
        }
        // Deserialize search results
        String searchQuery = parseQuery(props.getProperty(ACTION_LOG_ATTRIBUTE_QUERY));
        currentSearches.put(user, searchQuery);
        System.out.println("SEARCH RECEIVED: " + searchQuery);// XXX remove me
        List<ISearchResult> referenceResults = SearchResultUtils.createSearchResultsFromXML(props.getProperty(ACTION_LOG_ATTRIBUTE_RESULT));

        // Check search terms for spelling mistakes... this task is independent from the rest
//        SpellingChecker sc = new SpellingChecker(this.commandSpace, user, mission, session, searchQuery.split(" "), this.fuzzySearchTermSimilarityTolerance, this.fuzzySearchResultTolerance);
//        new Thread(sc).start();

        // Choose strategy
        SearchResultRanking ranking = null;
        if (referenceResults.size() < this.infGoodSearchInverval) {
            String language = getLanguage(user);
            ranking = extendSearchResult(user, language, searchQuery, referenceResults);
        } else if (referenceResults.size() > this.supGoodSearchInterval) {
            String language = getLanguage(user);
            ranking = pruneSearchResult(user, language, searchQuery, referenceResults);
        } else {
            // Number of results is ok, so we do nothing
            // System.out.println("Search result for query: " + searchQuery +
            // " performed well, no enrichment will be forced.");
            System.out.println("Search result for query: " + searchQuery + " performed well, no enrichment will be forced.");
        }

        // Propose search queries, if found
        if (ranking != null && ranking.getResults().length > 0) {
            System.out.println("RESULT: " + ranking.toString()); // XXX remove me
            // System.out.println(generateAlternativeQueriesTuple(user, mission, session, ranking));
            try {
                System.out.println("Returning proposal tuple");
                commandSpace.write(generateAlternativeQueriesTuple(user, mission, session, ranking));
            } catch (TupleSpaceException e) {
                logger.error("Connection to TupleSpace lost!", e);
                // System.out.println("Connection to TupleSpace lost!");
            }
        } else {
            System.out.println("No better suggestion found");
        }
    }

    //
    // STRATEGY: EXTEND SEARCH RESULTS (TOO LESS RESULTS)
    //

    /**
     * Tries to create another search query, that will provide more results.
     * 
     * @param eloUri
     *            the elo uri
     * @param mission
     *            the mission
     * @param language
     *            the users language
     * @param searchQuery
     *            the search query
     * @param referenceResults
     *            the reference results
     * @return the search result ranking
     */
    private SearchResultRanking extendSearchResult(String user, String language, String searchQuery, List<ISearchResult> referenceResults) {

        SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);
        try {
            Query query = Query.parse(searchQuery);

            Set<String> items = new HashSet<String>();
            items.addAll(Arrays.asList(searchQuery.split(" ")));

            items.remove(OPERATOR_AND);
            items.remove(OPERATOR_OR);
            
            // Replace AND by OR
            String last = query.toLuceneString();
            for (int i = 1; i <= this.maxReplaceNumber; i++) {
                String luceneQuery = query.replaceOperator(OPERATOR_AND, OPERATOR_OR, new AtomicInteger(i));
                if (luceneQuery.equals(last)) {
                    // if a replace operation does not change the query anymore, stop
                    if (i == 1) {
                        // System.out.println("Could not replace operator, because the query does not contain any AND operator.");
                        logger.debug("Could not replace operator, because the query does not contain any AND operator.");
                    }
                    break;
                } else {
                    last = luceneQuery;
                    if (!currentSearches.get(user).equals(searchQuery)) {
                        System.out.println("New search arrived, canceling '" + searchQuery + "'");
                        return null;
                    }
                    List<ISearchResult> result = getHits(commandSpace, luceneQuery);
                    if (!currentSearches.get(user).equals(searchQuery)) {
                        System.out.println("New search arrived, canceling '" + searchQuery + "'");
                        return null;
                    }

                    if (result != null && result.size() > referenceResults.size()) {
                        ranking.add(luceneQuery, result);
                        // System.out.println("Added new query by AND -> OR replacement: " +
                        // luceneQuery);
                        logger.debug("Added new query by AND -> OR replacement: " + luceneQuery);
                    } else {
                        // RooloAccessorAgent down, so stop
                        return ranking;
                    }
                }
            }

            // Both extraction methods need a language to extract keywords...
            if (language != null) {
                // Add new keywords extracted from the search results.
                if (referenceResults.size() > 0) {
                    boolean resultsReceived = extractAndTestKeywords(ranking, items, query, language, OPERATOR_OR, referenceResults.size());
                    if (!resultsReceived) {
                        // RooloAccessorAgent down, so stop
                        return ranking;
                    }

                } else {
                    // System.out.println("Could not extract keywords, because no search results available for extraction.");
                    logger.info("Could not extract keywords, because no search results available for extraction.");
                }
                if (!currentSearches.get(user).equals(searchQuery)) {
                    System.out.println("New search arrived, canceling '" + searchQuery + "'");
                    return null;
                }
                extractAndTestOntologyKeywords(ranking, getMission(user), items, language, query, OPERATOR_OR, referenceResults.size());
            } else {
                // System.out.println("Could not extract keywords, because no language information available.");
                logger.info("Could not extract keywords, because no language information available.");
            }

        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        return ranking;
    }

    /**
     * Extracts a list of keywords out of the reference results titles. After extraction, those
     * results are evaluated.
     * 
     * @param ranking
     *            the SearchResultRanking
     * @param query
     *            the Query object
     * @param searchQuery
     *            user entered search query
     * @param language
     *            the users language
     * @return false if the RooloAccessorAgent didn't respond in appropriate time
     * @throws TupleSpaceException
     *             the tuple space exception
     */
    private boolean extractAndTestKeywords(SearchResultRanking ranking, Set<String> items, Query query, String language, String operator, int referenceResultCount) throws TupleSpaceException {
        List<TermEntry> termList = extractKeywordsFromSearchResults(items, language, ranking.getReferenceResults());

        String userQuery = query.toLuceneString();
        for (int i = 0; i < Math.min(this.numberOfTermsToTest, termList.size()); i++) {
            // String queryWithNewKeyword = userQuery + " " + operator + " " + DEFAULT_FIELD + ":\""
            // + termList.get(i).getTerm() + "\"";
            String term = removeBadChars(termList.get(i).getTerm());
            String queryWithNewKeyword = userQuery + " " + operator + " " + "\"" + term + "\"";
            List<ISearchResult> result = getHits(commandSpace, queryWithNewKeyword);
            if (result != null) {
                if (result.size() > referenceResultCount) {
                    ranking.add(queryWithNewKeyword, result);
                    // System.out.println("Added new query by keyword extension: " +
                    // queryWithNewKeyword);
                    logger.debug("Added new query by keyword extension: " + queryWithNewKeyword);
                }
            } else {
                // RooloAccessorAgent down, so stop
                return false;
            }
        }
        return true;
    }

    private String removeBadChars(String term) {
        return term.replaceAll("\\(|\\)", "");
    }

    private boolean extractAndTestOntologyKeywords(SearchResultRanking ranking, String mission, Set<String> items, String language, Query query, String operator, int referenceResultCount) throws TupleSpaceException {
        List<String> keywords = new ArrayList<String>();

        // Collect keywords
        for (String item : items.toArray(EMPTY_STRING_ARRAY)) {
            Set<String> newKeywords = askOntologyForSurroundingWords(mission, language, item);
            if (newKeywords == null) {
                return false;
            }
            for (String newKeyword : newKeywords) {
                if (newKeyword.trim().isEmpty()) {
                    continue;
                }
                if (newKeyword.indexOf(' ') == -1) {
                    keywords.add(newKeyword);
                } else {
                    String[] split = newKeyword.split(" ");
                    keywords.addAll(Arrays.asList(split));
                }
            }
            if (keywords.size() >= this.maxOntologyKeywordNumber) {
                break;
            }
        }

        // Test words
        String userQuery = query.toLuceneString();
        for (String keyword : keywords) {
            // String queryWithNewKeyword = userQuery + " " + operator + " " + DEFAULT_FIELD + ":\""
            // + keyword + "\"";
            String queryWithNewKeyword = userQuery + " " + operator + " " + "\"" + keyword + "\"";
            List<ISearchResult> result = getHits(commandSpace, queryWithNewKeyword);
            if (result != null) {
                if (result.size() > referenceResultCount) {
                    ranking.add(queryWithNewKeyword, result);
                    // System.out.println("Added new query by AND -> OR replacement: " +
                    // queryWithNewKeyword);
                    logger.debug("Added new query by AND -> OR replacement: " + queryWithNewKeyword);
                }
            } else {
                // RooloAccessorAgent down, so stop
                return false;
            }
        }
        return true;
    }

    /**
     * Asks the ontology for keywords, that are directly connected to a specific word.
     * 
     * @param mission
     *            the user's mission
     * @param eloUri
     *            the eloUri of currently opened elo
     * @param language
     *            the user's language
     * @param word
     *            the word used to search for surroundings
     * @return a list of keywords or null if the ontology agent is down
     */
    private Set<String> askOntologyForSurroundingWords(String mission, String language, String word) {
        // (<ID>:String, "onto":String, "surrounding":String, <OntName>:String, <OntLabel>:String,
        // <Language>:String) -> (<ID>:String, <OntTerm>:String, <Surrounding>:String)

        HashSet<String> synonyms = new HashSet<String>();
        try {
            String id = new VMID().toString();
            Tuple requestTuple = new Tuple(id, ONTOLOGY_AGENT_NAME, ONTOLOGY_AGENT_COMMAND, Mission.getForName(mission).getNamespace(), word, language);
            System.out.println("ASK ONTO FOR '" + word + "' in " + language + " (" + id + ")");// XXX
                                                                                               // remove
                                                                                               // me
            this.commandSpace.write(requestTuple);
            Tuple responseTuple = this.commandSpace.waitToTake(new Tuple(id, "response", String.class), ONTOLOGY_AGENT_TIMEOUT);

            if (responseTuple != null) {
                // No third tuple when no keywords were found.
                if (responseTuple.getFields().length > 2) {
                    String surrounding = (String) responseTuple.getField(2).getValue();
                    String[] surroundKeywords = surrounding.split(",");

                    for (String ontKeyword : surroundKeywords) {
                        synonyms.add(ontKeyword);
                    }
                    System.out.println("SYNONYMS: " + synonyms);// XXX remove me
                } else {
                    System.out.println("NO SYNONYMS");// XXX remove me
                    // System.out.println("No keywords found in the ontology for term " + word);
                    logger.info("No keywords found in the ontology for term " + word);
                }
            } else {
                System.out.println("NO RESPONSE");// XXX remove me
                // OntologyAgent is down
                return null;
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        return synonyms;
    }

    /**
     * Uses the search results of the origin search to extract terms, that might be of interest.
     * 
     * @param items
     *            the items
     * @param language
     *            the users language
     * @param results
     *            the results
     * @return the list
     */
    @SuppressWarnings("unchecked")
    private List<TermEntry> extractKeywordsFromSearchResults(Set<String> items, String language, List<ISearchResult> results) {
        List<TermEntry> termList = new LinkedList<TermEntry>();

        // Grab all terms and put them in a bag.
        HashMap<String, Integer> bag = new HashMap<String, Integer>();
        int resultsUsed = 0;
        for (int i = 0; i < results.size(); i++) {
            String keywords = null;
            for (Locale loc : results.get(i).getTitles().keySet()) {
                if (language.equals(loc.getLanguage())) {
                    keywords = results.get(i).getTitles().get(loc);
                    break;
                }
            }
            if (keywords == null || keywords.equals("")) {
                continue;
            }

            // Keyword list may contains relevance entries, so we need to delete them
            // keywords = keywords.replaceAll(" [0-9]\\.[0-9]", "");

            keywords = keywords.toLowerCase();
            List<String> terms = Arrays.asList(keywords.split(" "));
            for (String t : terms) {
                Integer c = bag.get(t);
                if (c == null) {
                    c = 0;
                }
                c++;
                bag.put(t, c);
            }
            resultsUsed++;
            if (resultsUsed == this.numberOfResultsUsedForExtension) {
                break;
            }
        }
        String last = "";
        for (Entry<String, Integer> e : bag.entrySet()) {
            String term = (String) e.getKey();

            if (term.equals(last)) {
                // Bag contains duplicate entries...
                continue;
            }
            if (stopwordMap.get(language) != null && stopwordMap.get(language).contains(term)) {
                // Keyword is in stopword list
                continue;
            }
            if (items.contains(term)) {
                // Keyword is in user query
                continue;
            }

            last = term;
            int termCount = e.getValue();
            termList.add(new TermEntry(term, termCount));
        }
        Collections.sort(termList);
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
     * Tries to prune the search results.
     * 
     * @param language
     *            the users language
     * @param searchQuery
     *            the search query
     * @param referenceResults
     *            the reference results
     * @return the search result ranking
     */
    private SearchResultRanking pruneSearchResult(String user, String language, String searchQuery, List<ISearchResult> referenceResults) {
        SearchResultRanking ranking = new SearchResultRanking(this.maxProposalNumber, referenceResults, this.config, this.infGoodSearchInverval, this.supGoodSearchInterval);
        Set<String> items = new HashSet<String>();
        items.addAll(Arrays.asList(searchQuery.split(" ")));

        try {
            Query query = Query.parse(searchQuery);

            if (items.size() > 1) {

                // Itemset generation
                String[] itemsets = generateItemSets(items);
                for (int i = 0; i < itemsets.length; i++) {

                    List<ISearchResult> result = getHits(commandSpace, itemsets[i]);
                    if (result != null) {
                        ranking.add(itemsets[i], result);
                    } else {
                        // RooloAccessorAgent down, so stop
                        return ranking;
                    }
                }

                // Replace OR by AND
                String last = query.toLuceneString();
                AtomicInteger ai = new AtomicInteger(0);
                for (int i = 1; i <= this.maxReplaceNumber; i++) {
                    ai.set(i);
                    String luceneQuery = query.replaceOperator(OPERATOR_OR, OPERATOR_AND, ai);
                    if (luceneQuery.equals(last)) {
                        // if a replace operation does not change the query anymore, stop
                        if (i == 1) {
                            // System.out.println("Could not replace operator, because the query does not contain any OR operator.");
                            logger.info("Could not replace operator, because the query does not contain any OR operator.");
                        }
                        break;
                    } else {
                        last = luceneQuery;
                        List<ISearchResult> result = getHits(commandSpace, luceneQuery);
                        if (result != null) {
                            ranking.add(luceneQuery, result);
                        } else {
                            // RooloAccessorAgent down, so stop
                            return ranking;
                        }
                    }
                }
            } else {
                // System.out.println("The user has entered only one term! No term set generation and operator replacement available.");
                logger.info("The user has entered only one term! No term set generation and operator replacement available.");
            }

            if (language != null) {
                boolean resultsReceived = extractAndTestKeywords(ranking, items, query, language, OPERATOR_AND, 0); // XXX
                                                                                                                    // interpret
                                                                                                                    // 0
                                                                                                                    // as
                                                                                                                    // max,
                                                                                                                    // not
                                                                                                                    // as
                                                                                                                    // min
                if (!resultsReceived) {
                    // RooloAccessorAgent down, so stop
                    return ranking;
                }

                // extractAndTestOntologyKeywords(ranking, getMission(user), items, language, query,
                // OPERATOR_AND);
            } else {
                // System.out.println("Could not extract keywords, because no language information available.");
                logger.info("Could not extract keywords, because no language information available.");
            }
        } catch (TupleSpaceException e) {
            // e.printStackTrace();
            logger.error("Connection to TupleSpace lost!", e);
        }
        return ranking;
    }

    /**
     * Generate n * (n-1)-itemsets out of the n-itemset (i.e.: three 2-itemsets for one 3-itemset)
     * 
     * @param items
     * @return
     */
    private String[] generateItemSets(Set<String> items) {
        String[] itemsets = new String[items.size()];

        String[] array = items.toArray(EMPTY_STRING_ARRAY);
        // Generate queries
        for (int i = 0; i < array.length; i++) {
            String s = "";
            for (int j = 0; j < array.length; j++) {
                if (i != j) {
                    s = s + "\"" + array[j] + "\" ";
                    // s = s + DEFAULT_FIELD + ":\"" + array[j] + "\" ";
                }
            }
            itemsets[i] = s.trim();
        }
        return itemsets;
    }

    /**
     * Generates the resulting tuple, containing the alterantive search requests and their hit
     * counts. ("notification":String, <ID>:String, <User>:String, <Tool>:String, <Mission>:String,
     * <Session>, <Query=NumberOfHits>:String*)
     * 
     * @param userId
     * @param mission
     * @param session
     * @param ranking
     * @return
     */
    private Tuple generateAlternativeQueriesTuple(String userId, String mission, String session, SearchResultRanking ranking) {
        Tuple tuple = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), userId, SEARCH_TOOL, AGENT_NAME, mission, session, "type=search_proposal");

        for (Result result : ranking.getResults()) {
            if (result != null && result.getResult().size() > 0) {
                tuple.add("proposals=" + result.getQuery().replaceAll(DEFAULT_FIELD + ":", ""));
                tuple.add("counts=" + result.getResult().size());
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

        private double termFuzzyOperator;

        private double searchResultTolerance;

        SpellingChecker(TupleSpace commandSpace, String userId, String mission, String session, String[] items, double termSimilarityTolerance, double searchResultTolerance) {
            this.commandSpace = commandSpace;
            this.userId = userId;
            this.mission = mission;
            this.session = session;
            this.items = items;
            this.termFuzzyOperator = termSimilarityTolerance;
            this.searchResultTolerance = searchResultTolerance;
        }

        @Override
        public void run() {
            try {
                Map<String, Integer> misspelledTerms = checkSpelling(this.items);
                if (misspelledTerms.size() > 0) {

                    Tuple tuple = generateMisspelledTermsTuple(misspelledTerms);
                    System.out.println("Returning misspell tuple");
                    commandSpace.write(tuple);
                } else {
                    // System.out.println("No misspelled terms.");
                    logger.debug("No misspelled terms.");
                }
            } catch (TupleSpaceException e) {
                // e.printStackTrace();
                logger.error("Connection to TupleSpace lost!", e);
            }
        }

        /**
         * Checks for each term of the search query, whether we get much more search results by
         * performing a fuzzy search. This may indicate a spelling mistake.
         * 
         * @param items
         * @return
         * @throws TupleSpaceException
         */
        private Map<String, Integer> checkSpelling(String[] items) throws TupleSpaceException {
            Map<String, Integer> misspelledTerms = new HashMap<String, Integer>();
            for (String term : items) {

                if (term.equals(OPERATOR_AND) || term.equals(OPERATOR_OR)) {
                    continue;
                }

                String exactQuery = DEFAULT_FIELD + ":\"" + term + "\"";
                int exact = SearchResultEnricherAgent.getHitCount(commandSpace, exactQuery);
                if (exact == -1) {
                    // RooloAccessorAgent down, so we stop checking
                    return misspelledTerms;
                }

                String fuzzyQuery = DEFAULT_FIELD + ":\"" + term + "~" + this.termFuzzyOperator + "\"";
                int fuzzy = getHitCount(commandSpace, fuzzyQuery);
                if (fuzzy == -1) {
                    // RooloAccessorAgent down, so we stop checking
                    return misspelledTerms;
                }

                logger.debug("Term: " + term + " | Exact: " + exact + " | Fuzzy: " + fuzzy);
                // Check if we get much more results while performing a fuzzy search.
                // acceleration is the percent value, the hit number was boosted because of the
                // fuzzy operator.
                if (exact == 0 && fuzzy == 0) {
                    continue;
                } else if (exact == 0 && fuzzy > 0) {
                    misspelledTerms.put(term, fuzzy);
                } else {
                    double acceleration = (((double) fuzzy) / exact);
                    if (acceleration > this.searchResultTolerance) {
                        misspelledTerms.put(term, fuzzy);
                    }
                }
            }
            return misspelledTerms;
        }

        /**
         * Generates a tuple, containing the probably misspelled terms. ("notification":String,
         * <ID>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String,
         * <terms=term1 term2 term3>:String)
         * 
         * @param terms
         * @return
         */
        private Tuple generateMisspelledTermsTuple(Map<String, Integer> terms) {
            Tuple t = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), this.userId, SEARCH_TOOL, AGENT_NAME, this.mission, this.session, "type=search_proposal");
            for (Entry<String, Integer> e : terms.entrySet()) {
                t.add("misspelled=" + e.getKey());
                t.add("counts=" + e.getValue());
            }
            return t;
        }
    }
}
