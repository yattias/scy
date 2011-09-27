package eu.scy.agents.hypothesis;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.hypothesis.workflow.EvalHypothesisWorkflow;
import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import eu.scy.agents.impl.EloTypes;
import eu.scy.agents.impl.ModelStorage;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.util.Utilities;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.ScyElo;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public HypothesisEvaluationAgent2(Map<String, Object> map) {
        super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID),
                (String) map.get(AgentProtocol.TS_HOST), (Integer) map
                .get(AgentProtocol.TS_PORT));
        rooloServices = new AgentRooloServiceImpl();
        modelStorage = new ModelStorage(getCommandSpace());
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }
    }

    @Override
    protected void doStop() {
        try {
            getActionSpace().disconnect();
            getCommandSpace().disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return status != Status.Running;
    }

    @Override
    public void processELOSavedAction(String actionId, String user,
                                      long timeInMillis, String tool, String mission, String session,
                                      String eloUri, String eloType) {
        try {
            if (eloType == null) {
                logger.warn(eloUri + " has no type");
            }
            mission = getSession().getMission(user).getName();
            String language = getSession().getLanguage(user);
            ScyElo elo = ScyElo.loadElo(URI.create(eloUri), rooloServices);


            if (!isCorrectEloType(elo)) {
                return;
            }

            String text = "";
            if (isRichtextElo(elo)) {
                text = getRichtextEloText(elo.getElo());
            } else {
                text = Utilities.getEloText(elo.getElo(), SCYED_XPATH, logger);
            }


            Set<String> keywords = getKeywordModel(mission, language);

            // make OBWIOUS document and process in workflow:
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
            HashMap<Integer, Integer> hist = docResult
                    .getFeature(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);

            // addSentenceHistogramToMetadata(elo, hist);

            // write HashMap histogram as a ByteArray object
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytesOut);
            out.writeObject(hist);
            // write a tuple with Byte version of histogram to trigger the
            // HypothesisEvaluation decision
            // maker
            Tuple activateDecisionMakerTuple = new Tuple(EVAL, user, mission,
                    session, tool, eloUri, bytesOut.toByteArray());
            getCommandSpace().write(activateDecisionMakerTuple);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

    }

    private Set<String> getKeywordModel(String missionName, String language) {
        Set<String> keywords = modelStorage.get(missionName, language, FIXED_KEYWORDS_MODEL);
        if (keywords == null) {
            logger.fatal("Fixd keywords are not present for mission " + missionName
                    + ", " + language);
            keywords = Collections.emptySet();
        }
        return keywords;
    }


    private String getRichtextEloText(IELO elo) {
        Element rootElement = getContentAsXML(elo);
        if (rootElement == null) {
            return "";
        }
        return rootElement.getTextTrim();
    }

    private Element getContentAsXML(IELO elo) {
        IContent content = elo.getContent();
        if (content == null) {
            logger.fatal("Content of elo is null");
            return null;
        }
        String contentText = content.getXmlString();
        SAXBuilder builder = new SAXBuilder();
        Element rootElement = new Element("empty");
        try {
            org.jdom.Document document = builder.build(new StringReader(contentText));
            return document.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isRichtextElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if (EloTypes.SCY_RICHTEXT.equals(technicalFormat)) {
            if (elo.getFunctionalRole() == EloFunctionalRole.HYPOTHESIS) {
                return true;
            }
        }
        return false;
    }

    private boolean isScyEdElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if (EloTypes.SCY_XPROC.equals(technicalFormat)) {
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


}
