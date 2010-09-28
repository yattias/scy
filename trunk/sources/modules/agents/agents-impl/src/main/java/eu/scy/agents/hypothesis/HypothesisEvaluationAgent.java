package eu.scy.agents.hypothesis;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import util.Utilities;
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
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.extractors.CopexExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractorFactory;

/**
 * @author Joerg Kindermann
 * 
 *         Workflow: extract Keywords (or read them from meta-data of ELO) - tokenize text into
 *         sentences - determine keyword-per-sentence histogram: the idea is to check for
 *         co-occurring keywords in sentences. Output is a hashmap that stores the number of
 *         sentences which contain 0,1,2... keywords.
 */
public class HypothesisEvaluationAgent extends AbstractELOSavedAgent implements IRepositoryAgent {

  public static final String NAME = HypothesisEvaluationAgent.class.getName();

  public static final Object EVAL = "EvalHypothesis";

  // private static final String TOOL_NAME = "copex";

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
      List<String> keywords = new ArrayList<String>();
      if (!metadata.metadataKeyExists(metadataKey)) {
        // get keywords extracted and store them in ELO meta data section!
        KeywordExtractorFactory factory = new KeywordExtractorFactory();
        KeywordExtractor keywordExtractor = factory.getKeywordExtractor(eloType);
        keywordExtractor.setTupleSpace(getCommandSpace());
        keywords = keywordExtractor.getKeywords(elo);
        IMetadata newMetadata = addKeywordsToMetadata(elo, keywords);
        if (newMetadata != null) {
          elo.setMetadata(newMetadata);
        }
      }

      // if keywords exist in ELO, we can go on extracting them and the hypothesis text:
      metadata = elo.getMetadata();
      IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(metadataKey);
      List<String> tmpKeywords = (List<String>) metadataValueContainer.getValueList();
      if (!tmpKeywords.isEmpty()) {
        keywords = tmpKeywords;
      }
      String text = Utilities.getEloText(elo, CopexExtractor.XMLPATH, logger);
      // make OBWIOUS document and process in workflow:
      Document document = Utilities.convertTextToDocument(text);
      document.setFeature(Features.WORDS, keywords);
      Operator cmpHistogramOp = new EvalHypothesisWorkflow().getOperator("Main");
      cmpHistogramOp.setInputParameter(ObjectIdentifiers.DOCUMENT, document);
      Container result = cmpHistogramOp.run();
      Document docResult = (Document) result.get(ObjectIdentifiers.DOCUMENT);
      HashMap<Integer, Integer> hist = docResult.getFeature(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);
      addSentenceHistogramToMetadata(elo, hist);

      // write HashMap histogram as a ByteArray object
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bytesOut);
      out.writeObject(hist);
      // write a tuple with Byte version of histogram to trigger the HypothesisEvaluation decision
      // maker
      Tuple activateDecisionMakerTuple = new Tuple(EVAL, user, mission, session, tool, eloUri,
                                                   bytesOut.toByteArray());
      getCommandSpace().write(activateDecisionMakerTuple);
    } catch (URISyntaxException e) {
      // TODO: handle exception
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TupleSpaceException e) {
      e.printStackTrace();
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

  private void addSentenceHistogramToMetadata(IELO elo, HashMap<Integer, Integer> histogram) {
    if (histogram.isEmpty()) {
      return;
    }
    IMetadataKey keywordSentenceHistogramKey = metadataTypeManager.getMetadataKey(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);
    IMetadataValueContainer keywordSentenceHistogramContainer = elo.getMetadata().getMetadataValueContainer(keywordSentenceHistogramKey);
    keywordSentenceHistogramContainer.setValue(histogram);

    repository.updateELO(elo);
  }

  private IMetadata addKeywordsToMetadata(IELO elo, List<String> keywords) {
    if (keywords.isEmpty()) {
      return null;
    }
    IMetadataKey keywordKey = metadataTypeManager.getMetadataKey(KeywordConstants.AGENT_KEYWORDS);
    IMetadataValueContainer agentKeywordsContainer = elo.getMetadata().getMetadataValueContainer(keywordKey);
    agentKeywordsContainer.setValueList(keywords);

    IMetadata updateELO = repository.updateELO(elo);
    return updateELO;
  }

}
