package eu.scy.agents.hypothesis;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tika.metadata.Metadata;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import util.Utilities;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractorFactory;

/**
 * @author Joerg Kindermann
 * 
 *         Workflow: extract Keywords (or read them from meta-data of ELO) - tokenize text into
 *         sentences - determine keyword-per-sentence histogram: the idea is to check for
 *         co-occurrgin keywords in sentences
 */
public class HypothesisEvaluationAgent extends AbstractELOSavedAgent implements IRepositoryAgent {
  public static final String NAME = HypothesisEvaluationAgent.class.getName();

  public static final Object EVAL = "EvalHypothesis";

  private static final String TOOL_NAME = "copex";

  private static final Logger logger = Logger.getLogger(HypothesisEvaluationAgent.class.getName());

  private boolean isStopped;

  private IRepository repository;

  private IMetadataTypeManager metadataTypeManager;

  public HypothesisEvaluationAgent(Map<String, Object> map) {
    super(HypothesisEvaluationAgent.class.getName(),
          (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST),
          (Integer) map.get(AgentProtocol.TS_PORT));
    // try {
    // Callback hypothesisCallback = new HypothesisCallback();
    // getActionSpace().eventRegister(
    // Command.WRITE,
    // new Tuple(AgentProtocol.ACTION_SPACE_NAME, String.class,
    // Long.class, AgentProtocol.ACTION_TOOL_STARTED,
    // Field.createWildCardField(), TOOL_NAME,
    // Field.createWildCardField()), hypothesisCallback,
    // true);
    // } catch (TupleSpaceException e) {
    // e.printStackTrace();
    // }
  }

  @Override
  protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
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
    // TODO
    return null;
  }

  @Override
  public boolean isStopped() {
    return isStopped;
  }

  //
  // class HypothesisCallback implements Callback {
  //
  // @Override
  // public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
  // Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
  // String mission = a.getContext(ContextConstants.mission);
  // String session = a.getContext(ContextConstants.session);
  // String elouri = a.getContext(ContextConstants.eloURI);
  //      
  // // determine keywords per sentence histogram
  //      
  //      
  // // TODO Here the logic must be inserted ;-)
  // // Wait till saved -> then its easy but maybe we want to evaluate intermediate
  // // results....
  // }
  // }

  @Override
  protected void processELOSavedAction(String actionId, String user, long timeInMillis,
                                       String tool, String mission, String session, String eloUri,
                                       String eloType) {
    // // TODO Here the logic must be inserted ;-)
    try {
      if (!eloType.equals("scy/copex")) {
        return;
      }
      URI eloURI = new URI(eloUri);
      IELO elo = repository.retrieveELO(eloURI);
      IMetadataKey metadataKey = metadataTypeManager.getMetadataKey(KeywordConstants.AGENT_KEYWORDS);
      IMetadata metadata = elo.getMetadata();
      List<String> keywords;
      if (!metadata.metadataKeyExists(metadataKey)) {
        // get keywords extracted and store them in ELO meta data section!
        KeywordExtractor keywordExtractor = KeywordExtractorFactory.getKeywordExtractor(eloType);
        keywords = keywordExtractor.getKeywords(elo);
        addKeywordsToMetadata(elo, keywords);
      }

      // if keywords exist in ELO, we can go on extracting them and the hypothesis text:
      IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(metadataKey);
      keywords = (List<String>) metadataValueContainer.getValueList();
      String text = Utilities.getEloText(elo, logger);
      // make OBWIOUS document and process in workflow:
      Document document = convertTextToDocument(text);
      document.setFeature(Features.WORDS, keywords);
      
      //Operator evalHypothesisOperator = new Spl 
    } catch (URISyntaxException e) {
      // TODO: handle exception
    }

  }

  @Override
  public void setMetadataTypeManager(IMetadataTypeManager manager) {
    metadataTypeManager = manager;
  }

  @Override
  public void setRepository(IRepository rep) {
    // injects RoOLO repository to provide access to ELOs
    repository = rep;
  }

  private void addKeywordsToMetadata(IELO elo, List<String> keywords) {
    if (keywords.isEmpty()) {
      return;
    }
    IMetadataKey keywordKey = metadataTypeManager.getMetadataKey(eu.scy.agents.keywords.KeywordConstants.AGENT_KEYWORDS);
    IMetadataValueContainer agentKeywordsContainer = elo.getMetadata().getMetadataValueContainer(
                                                                                                 keywordKey);
    agentKeywordsContainer.setValueList(keywords);

    repository.updateELO(elo);
  }

  private Document convertTextToDocument(String text) {
    Document doc = new Document("id");
    doc.setFeature(Features.TEXT, text);
    return doc;
  }

}
