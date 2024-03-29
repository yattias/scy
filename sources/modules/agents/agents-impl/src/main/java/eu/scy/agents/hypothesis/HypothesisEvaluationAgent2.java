package eu.scy.agents.hypothesis;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.AbstractELOSavedAgent;
import eu.scy.agents.ActionConstants;
import eu.scy.agents.AgentRooloServiceImpl;
import eu.scy.agents.EloTypes;
import eu.scy.agents.IRepositoryAgent;
import eu.scy.agents.ModelStorage;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.hypothesis.workflow.EvalHypothesisWorkflow;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.OntologyKeywordsAgent;
import eu.scy.agents.util.Utilities;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.ScyElo;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Jörg Kindermann
 *         <p/>
 *         Workflow: extract Keywords based only on topic models and ontology (don't use the meta-data keywords, because they also contain
 *         key words found by the TFIDF workflow AND keywords from other parts of the ScyED ELOs) - tokenize text into sentences -
 *         determine
 *         keyword-per-sentence histogram: the idea is to check for co-occurring keywords in sentences. Output is a hashmap that stores the
 *         number of sentences which contain 0,1,2... keywords.
 */
public class HypothesisEvaluationAgent2 extends AbstractELOSavedAgent implements
        IRepositoryAgent {

    public static final String NAME = HypothesisEvaluationAgent2.class.getName();
    public static final String EVAL = "EvalHypothesis";

    private static final String SCYED_XPATH = "//learner_proc/proc_hypothesis/hypothesis";
    private static final Logger logger = Logger.getLogger(HypothesisEvaluationAgent2.class.getName());

    private AgentRooloServiceImpl rooloServices;
    private ModelStorage modelStorage;
    private static final String FIXED_KEYWORDS_MODEL = "fixedKeywordModel";
    private int eloFinishedListener;

    public HypothesisEvaluationAgent2(Map<String, Object> map) {
        super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID),
                (String) map.get(AgentProtocol.TS_HOST), (Integer) map
                .get(AgentProtocol.TS_PORT));
        rooloServices = new AgentRooloServiceImpl();
        modelStorage = new ModelStorage(getCommandSpace());
        try {
            eloFinishedListener = getActionSpace().eventRegister(Command.WRITE,
                    new Tuple(ActionConstants.ACTION, String.class, Long.class, ActionConstants.ELO_FINISHED,
                            Field.createWildCardField()), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {
        while ( status == Status.Running ) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }
    }

    @Override
    protected void doStop() {
        try {
            getActionSpace().disconnect();
            getCommandSpace().disconnect();
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStopped() {
        return status != Status.Running;
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        if ( seq != eloFinishedListener ) {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }
        IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
        String user = action.getUser();
        String tool = action.getContext(ContextConstants.tool);
        String session = action.getContext(ContextConstants.session);
        String eloUri = action.getContext(ContextConstants.eloURI);

        try {
            String missionName = getSession().getMission(user).getName();
            HashMap<Integer, Integer> histogram = runHypothesisChecking(user, eloUri, missionName);

            if ( histogram == null ) {
                return;
            }
            int scaffoldLevel = getScaffoldLevel(user);
            if ( scaffoldLevel == AgentProtocol.SCAFFOLD_LEVEL_MEDIUM ) {
                sendNotification(user, tool, session, eloUri, missionName, histogram);
            }
        } catch ( IOException e ) {
            logger.error(e);
        } catch ( TupleSpaceException e ) {
            logger.error(e);
        }
    }

    @Override
    public void processELOSavedAction(String actionId, String user,
                                      long timeInMillis, String tool, String mission, String session,
                                      String eloUri, String eloType) {
        try {
            if ( eloType == null ) {
                logger.warn(eloUri + " has no type");
            }
            String missionName = getSession().getMission(user).getName();
            HashMap<Integer, Integer> histogram = runHypothesisChecking(user, eloUri, missionName);

            if ( histogram == null ) {
                return;
            }
            int scaffoldLevel = getScaffoldLevel(user);
            if ( scaffoldLevel == AgentProtocol.SCAFFOLD_LEVEL_HIGH ) {
                sendNotification(user, tool, session, eloUri, missionName, histogram);
            }
        } catch ( IOException e ) {
            logger.error(e);
        } catch ( TupleSpaceException e ) {
            logger.error(e);
        }
    }

    private int getScaffoldLevel(String user) {
        String missionName = getSession().getMission(user).getName();
        String scaffoldLevelAsString = (String) configuration.getParameter(missionName, AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL);
        if ( scaffoldLevelAsString == null ) {
            logger.warn("Scaffold level not set assuming 1");
            scaffoldLevelAsString = "1";
        }
        return Integer.parseInt(scaffoldLevelAsString);
    }

    private HashMap<Integer, Integer> runHypothesisChecking(String user, String eloUri,
                                                            String missionName) throws IOException, TupleSpaceException {
        String language = getSession().getLanguage(user);


        if ( eloUri == null ) {
            logger.warn("The eloUri in the elo_saved action was null. Hypothesis agent could not run.");
            return null;
        }
        ScyElo elo = ScyElo.loadElo(URI.create(eloUri), rooloServices);
        if ( !isCorrectEloType(elo) ) {
            return null;
        }

        String text = getText(elo);
        Set<String> keywords = getSimpleKeywords(missionName, language);
        if ( keywords.isEmpty() ) {
            keywords = getComplexKeywords(text, missionName, language);
        }

        Document document = Utilities.convertTextToDocument(text);
        ArrayList<String> kwList = new ArrayList<String>(keywords);
        document.setFeature(Features.WORDS, kwList);
        Operator cmpHistogramOp = new EvalHypothesisWorkflow()
                .getOperator("Main");
        cmpHistogramOp.setInputParameter(ObjectIdentifiers.DOCUMENT,
                document);
        Container result = cmpHistogramOp.run();
        Document docResult = (Document) result
                .get(ObjectIdentifiers.DOCUMENT);
        HashMap<Integer, Integer> histogram = docResult
                .getFeature(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);

        addSentenceHistogramToMetadata(elo, histogram);
        return histogram;
    }

    private void addSentenceHistogramToMetadata(ScyElo elo, HashMap<Integer, Integer> hist) {
        IMetadataKey hypoValueKey = rooloServices.getMetadataTypeManager()
                .getMetadataKey(CoreRooloMetadataKeyIds.HYPO_VALUE.getId());
        IMetadataValueContainer hypoValueContainer = elo.getMetadata()
                .getMetadataValueContainer(hypoValueKey);
        StringBuilder histogramString = new StringBuilder();
        for ( Entry<Integer, Integer> entry : hist.entrySet() ) {
            histogramString.append(entry.getKey());
            histogramString.append("=");
            histogramString.append(entry.getValue());
            histogramString.append(",");
        }
        hypoValueContainer.setValue(histogramString.toString());

        // repository.updateELO(elo); //XXX this is important because an update
        // would change the version number of the elo, which will also change
        // the URI
        rooloServices.getRepository().addMetadata(elo.getUri(), elo.getMetadata());
    }

    private void sendNotification(String user, String tool, String session, String eloUri, String mission, HashMap<Integer,
            Integer> histogram) throws IOException, TupleSpaceException {
        // write HashMap histogram as a ByteArray object
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytesOut);
        out.writeObject(histogram);
        // write a tuple with Byte version of histogram to trigger the
        // HypothesisEvaluation decision
        // maker
        Tuple activateDecisionMakerTuple = new Tuple(EVAL, user, mission,
                session, tool, eloUri, bytesOut.toByteArray());
        getCommandSpace().write(activateDecisionMakerTuple);
    }

    private String getText(ScyElo elo) {
        String text = "";
        if ( isRichtextElo(elo) ) {
            text = getRichtextEloText(elo.getElo());
        } else {
            text = Utilities.getEloText(elo.getElo(), SCYED_XPATH, logger);
        }
        return text;
    }


    private Set<String> getComplexKeywords(String text, String missionName, String language) {
        Set<String> topicKeywords = callKeywordsAgent(
                ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS,
                text, missionName, language, AgentProtocol.SECOND * 30);
        logger.debug("found in tm keywords: " + topicKeywords);
        Set<String> ontologyKeywords = callKeywordsAgent(
                OntologyKeywordsAgent.EXTRACT_ONTOLOGY_KEYWORDS, text,
                missionName, language, AgentProtocol.SECOND * 10);
        logger.debug("found in ont keywords: " + ontologyKeywords);
        Set<String> keyPharses = callKeywordsAgent(
                ExtractKeyphrasesAgent.EXTRACT_KEYPHRASES, text, missionName, language,
                AgentProtocol.SECOND * 30);
        logger.debug("found in keyphrases: " + keyPharses);

        Set<String> keywords = new HashSet<String>();
        keywords.addAll(topicKeywords);
        keywords.addAll(ontologyKeywords);
        keywords.addAll(keyPharses);
        logger.info("found keywords: " + keywords);
        return keywords;
    }

    private Set<String> getSimpleKeywords(String missionName, String language) {
        Set<String> keywords = modelStorage.get(missionName, language, FIXED_KEYWORDS_MODEL);
        if ( keywords == null ) {
            logger.fatal("Fixd keywords are not present for mission " + missionName
                    + ", " + language);
            return Collections.emptySet();
        }
        Set<String> trimmedKeywords = new HashSet<String>();
        for ( String keyword : keywords ) {
            trimmedKeywords.add(keyword.trim());
        }
        return trimmedKeywords;
    }


    private String getRichtextEloText(IELO elo) {
        Element rootElement = getContentAsXML(elo);
        if ( rootElement == null ) {
            return "";
        }
        return rootElement.getTextTrim();
    }

    private Element getContentAsXML(IELO elo) {
        IContent content = elo.getContent();
        if ( content == null ) {
            logger.fatal("Content of elo is null");
            return null;
        }
        String contentText = content.getXmlString();
        SAXBuilder builder = new SAXBuilder();
        try {
            org.jdom.Document document = builder.build(new StringReader(contentText));
            return document.getRootElement();
        } catch ( JDOMException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isRichtextElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if ( EloTypes.SCY_RICHTEXT.equals(technicalFormat) ) {
            if ( elo.getFunctionalRole() == EloFunctionalRole.HYPOTHESIS ) {
                return true;
            }
        }
        return false;
    }

    private boolean isScyEdElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if ( EloTypes.SCY_XPROC.equals(technicalFormat) ) {
            return true;
        }
        return false;
    }

    private boolean isCorrectEloType(ScyElo elo) {
        return isRichtextElo(elo) || isScyEdElo(elo);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        rooloServices.setMetadataTypeManager(manager);
    }

    @Override
    public void setRepository(IRepository rep) {
        rooloServices.setRepository(rep);
    }

    private Set<String> callKeywordsAgent(String agent, String text,
                                          String mission, String language, int waitTime) {
        String queryId = new VMID().toString();
        Set<String> result = new HashSet<String>();
        try {
            getCommandSpace().write(
                    new Tuple(agent, AgentProtocol.QUERY, queryId, text,
                            mission, language));
            Tuple response = getCommandSpace().waitToTake(
                    new Tuple(agent, AgentProtocol.RESPONSE, queryId,
                            String.class), waitTime);
            if ( response == null ) {
                return result;
            }
            String keywordString = (String) response.getField(3).getValue();
            StringTokenizer tokenizer = new StringTokenizer(keywordString, ";");
            while ( tokenizer.hasMoreTokens() ) {
                result.add(tokenizer.nextToken());
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return result;
    }

}
