package eu.scy.agents.hypothesis;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.KeyValuePair;
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
import eu.scy.agents.impl.EloTypes;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.OntologyKeywordsAgent;
import eu.scy.agents.keywords.extractors.CopexExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractorFactory;
import eu.scy.agents.util.Utilities;

/**
 * @author Jörg Kindermann
 * 
 *         Workflow: extract Keywords based only on topic models and ontology
 *         (don't use the meta-data keywords, because they also contain key
 *         words found by the TFIDF workflow AND keywords from other parts of
 *         the ScyED ELOs) - tokenize text into sentences - determine
 *         keyword-per-sentence histogram: the idea is to check for co-occurring
 *         keywords in sentences. Output is a hashmap that stores the number of
 *         sentences which contain 0,1,2... keywords.
 */
public class HypothesisEvaluationAgent extends AbstractELOSavedAgent implements
		IRepositoryAgent {

	public static final String NAME = HypothesisEvaluationAgent.class.getName();

	public static final Object EVAL = "EvalHypothesis";
	private static String XMLPATH = "//learner_proc/proc_hypothesis/hypothesis";

	private static final Logger logger = Logger
			.getLogger(HypothesisEvaluationAgent.class.getName());

	private boolean isStopped;

	private IRepository repository;

	private IMetadataTypeManager metadataTypeManager;

	public HypothesisEvaluationAgent(Map<String, Object> map) {
		super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID),
				(String) map.get(AgentProtocol.TS_HOST), (Integer) map
						.get(AgentProtocol.TS_PORT));
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
		// TODO
		return null;
	}

	@Override
	public boolean isStopped() {
		return isStopped;
	}

	@Override
	public void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType) {
		try {
			if (!eloType.equals(EloTypes.SCY_XPROC)) {
				return;
			}
			URI eloURI = new URI(eloUri);
			IELO elo = repository.retrieveELO(eloURI);
			String text = Utilities.getEloText(elo, XMLPATH, logger);
			Set<String> topicKeywords = callKeywordsAgent(
					ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS,
					text, mission, AgentProtocol.MINUTE * 3);
			logger.debug("found in tm keywords: " + topicKeywords);
			Set<String> ontologyKeywords = callKeywordsAgent(
					OntologyKeywordsAgent.EXTRACT_ONTOLOGY_KEYWORDS, text,
					mission, AgentProtocol.MINUTE);
			logger.debug("found in ont keywords: " + ontologyKeywords);
			Set<String> keyPharses = callKeywordsAgent(
					ExtractKeyphrasesAgent.EXTRACT_KEYPHRASES, text, mission,
					AgentProtocol.MINUTE * 3);
			logger.debug("found in keyphrases: " + keyPharses);

			Set<String> keywords = new HashSet<String>();
			keywords.addAll(topicKeywords);
			keywords.addAll(ontologyKeywords);
			keywords.addAll(keyPharses);
			logger.info("found keywords: " + keywords);

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
		} catch (URISyntaxException e) {
			e.printStackTrace();
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

	// TODO: extract this method because it is a duplicate from
	// ExtractKeywordsAgent
	private Set<String> callKeywordsAgent(String agent, String text,
			String mission, int waitTime) {
		String queryId = new VMID().toString();
		Set<String> result = new HashSet<String>();
		try {
			getCommandSpace().write(
					new Tuple(agent, AgentProtocol.QUERY, queryId, text,
							mission, "en"));
			Tuple response = getCommandSpace().waitToTake(
					new Tuple(agent, AgentProtocol.RESPONSE, queryId,
							String.class), waitTime);
			if (response == null) {
				return result;
			}
			String keywordString = (String) response.getField(3).getValue();
			StringTokenizer tokenizer = new StringTokenizer(keywordString, ";");
			while (tokenizer.hasMoreTokens()) {
				result.add(tokenizer.nextToken());
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return result;
	}

}
