package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

import edu.emory.mathcs.backport.java.util.Collections;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractDecisionAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ExtractKeywordsDecisionMakerAgent extends AbstractDecisionAgent implements IRepositoryAgent {

	class ContextInformation {

		public String mission;
		public String session;
		public String user;

		public boolean webresourcerStarted = false;
		public boolean scyMapperStarted = false;

		public long lastAdded;
		public int numberOfConcepts = 0;
		public URI webresourcerELO = null;

		@Override
		public String toString() {
			String webresourcerURL = webresourcerELO != null ? webresourcerELO.toString() : "null";
			return user + "|web:" + webresourcerStarted + "|map:" + scyMapperStarted + "|url:" + webresourcerURL + "|"
					+ lastAdded + "|" + numberOfConcepts;
		}
	}

	private static final Logger logger = Logger.getLogger(ExtractKeywordsDecisionMakerAgent.class.getName());

	static final String NAME = ExtractKeywordsDecisionMakerAgent.class.getName();
	static final Object SCYMAPPER = "scymapper";
	static final Object CONCEPTMAP = "conceptmap";
	static final Object WEBRESOURCER = "webresource";

	public static final String IDLE_TIME_INMS = "idleTime";
	public static final String MINIMUM_NUMBER_OF_CONCEPTS = "minimumNumberOfConcepts";

	private long idleTime = 120000;
	private int minimumNumberOfConcepts = 5;
	private int listenerId = -1;
	private Map<String, ContextInformation> user2Context;

	private IMetadataTypeManager metadataTypeManager;
	private IRepository repository;

	@SuppressWarnings("unchecked")
	public ExtractKeywordsDecisionMakerAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));

		setParameter(params);

		registerToolStartedListener();
		user2Context = Collections.synchronizedMap(new HashMap<String, ContextInformation>());
	}

	private void setParameter(Map<String, Object> params) {
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		if (params.containsKey(IDLE_TIME_INMS)) {
			idleTime = (Long) params.get(IDLE_TIME_INMS);
		}
		if (params.containsKey(MINIMUM_NUMBER_OF_CONCEPTS)) {
			minimumNumberOfConcepts = (Integer) params.get(MINIMUM_NUMBER_OF_CONCEPTS);
		}
	}

	private void registerToolStartedListener() {
		try {
			listenerId = getActionSpace().eventRegister(Command.WRITE, getActionTupleTemplate(), this, false);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doStop() {
		try {
			getActionSpace().eventDeRegister(listenerId);
			listenerId = -1;
		} catch (TupleSpaceException e) {
			logger.fatal("Could not deregister tuple space listener: " + e.getMessage());
		}
	}

	private Tuple getActionTupleTemplate() {
		return new Tuple(AgentProtocol.ACTION, String.class, Long.class, String.class, String.class, String.class,
				String.class, String.class, Field.createWildCardField());
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
			logger.info("Found following proerties in the action: " + action.getAttributes());
			if (AgentProtocol.ACTION_TOOL_STARTED.equals(action.getType())) {
				handleToolStarted(action);
			} else if (AgentProtocol.ACTION_NODE_ADDED.equals(action.getType())) {
				handleNodeAdded(action);
			} else if (AgentProtocol.ACTION_NODE_REMOVED.equals(action.getType())) {
				handleNodeRemoved(action);
			} else if (AgentProtocol.ACTION_TOOL_CLOSED.equals(action.getType())) {
				handleToolStopped(action);
			} else if (AgentProtocol.ACTION_ELO_LOADED.equals(action.getType())) {
				handleELOLoaded(action);
			}
		}
	}

	private void handleELOLoaded(IAction action) {
		if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
			logger.info(WEBRESOURCER + " elo loaded " + action.getAttribute(AgentProtocol.ACTIONLOG_ELO_URI));
			ContextInformation contextInfo = getContextInformation(action);
			try {
				contextInfo.webresourcerELO = new URI(action.getAttribute(AgentProtocol.ACTIONLOG_ELO_URI));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleNodeRemoved(IAction action) {
		logger.info("node removed");
		ContextInformation contextInfo = getContextInformation(action);
		contextInfo.numberOfConcepts--;
	}

	private void handleNodeAdded(IAction action) {
		logger.info("node added");
		ContextInformation contextInfo = getContextInformation(action);
		contextInfo.numberOfConcepts++;
		contextInfo.lastAdded = action.getTimeInMillis();
	}

	private void handleToolStopped(IAction action) {
		if (CONCEPTMAP.equals(action.getContext(ContextConstants.tool))) {
			logger.info(CONCEPTMAP + " stopped");
			ContextInformation contextInfo = getContextInformation(action);
			contextInfo.scyMapperStarted = false;
		}
		if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
			logger.info(WEBRESOURCER + " stopped");
			ContextInformation contextInfo = getContextInformation(action);
			contextInfo.webresourcerStarted = false;
			contextInfo.webresourcerELO = null;
		}
	}

	private void handleToolStarted(IAction action) {
		if (CONCEPTMAP.equals(action.getContext(ContextConstants.tool))) {
			logger
					.info(CONCEPTMAP + " started by " + action.getUser()
							+ ". Recognized by ExtractKeywordsDecisionAgent");
			ContextInformation contextInfo = getContextInformation(action);
			contextInfo.scyMapperStarted = true;
		}
		if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
			logger.info(WEBRESOURCER + " started  by " + action.getUser()
					+ ". Recognized by ExtractKeywordsDecisionAgent");
			ContextInformation contextInfo = getContextInformation(action);
			contextInfo.webresourcerStarted = true;
		}
	}

	public ContextInformation getContextInformation(IAction action) {
		ContextInformation result = user2Context.get(action.getUser());
		if (result == null) {
			result = new ContextInformation();
			result.user = action.getUser();
			result.mission = action.getContext(ContextConstants.mission);
			result.session = action.getContext(ContextConstants.session);
			result.lastAdded = action.getTimeInMillis();
			user2Context.put(result.user, result);
		}
		return result;
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		while (status == Status.Running) {
			long currentTime = System.currentTimeMillis();
			for (String user : user2Context.keySet()) {
				ContextInformation contextInformation = user2Context.get(user);
				logger.info(contextInformation);
				if (userNeedsToBeNotified(currentTime, contextInformation)) {
					logger.info(user + " needs to be notified");
					String text = getEloText(contextInformation.webresourcerELO);
					if (!"".equals(text)) {
						List<String> keywords = getKeywords(text);
						logger.info("found keywords to send to " + user + ": " + keywords);
						sendNotification(contextInformation, keywords);
						contextInformation.lastAdded = currentTime;
					}
				}
			}
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.COMMAND_EXPIRATION);
		}
	}

	private boolean userNeedsToBeNotified(long currentTime, ContextInformation contextInformation) {
		boolean userNeedsToBeNotified = contextInformation.webresourcerStarted;
		userNeedsToBeNotified &= contextInformation.scyMapperStarted;
		userNeedsToBeNotified &= (currentTime - contextInformation.lastAdded) > idleTime;
		userNeedsToBeNotified &= contextInformation.numberOfConcepts < minimumNumberOfConcepts;
		userNeedsToBeNotified &= contextInformation.webresourcerELO != null;
		return userNeedsToBeNotified;
	}

	private void sendNotification(ContextInformation contextInformation, List<String> keywords) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(new VMID().toString());
		notificationTuple.add(contextInformation.user);
		notificationTuple.add(SCYMAPPER);
		notificationTuple.add(NAME);
		notificationTuple.add(contextInformation.mission);
		notificationTuple.add(contextInformation.session);
		notificationTuple.add("type=concept_proposal");
		for (String keyword : keywords) {
			notificationTuple.add("keyword=" + keyword);
		}
		try {
			if (getCommandSpace().isConnected()) {
				getCommandSpace().write(notificationTuple);
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private String getEloText(URI webresourcerELO) {
		logger.info("Getting elo: " + webresourcerELO);
		if (repository == null) {
			logger.fatal("repository is null");
			return "";
		}
		IELO elo = repository.retrieveELO(webresourcerELO);
		if (elo == null) {
			logger.fatal("ELO " + webresourcerELO + " was null");
			return "";
		}
		IContent content = elo.getContent();
		if (content == null) {
			logger.fatal("Content of elo is null");
			return "";
		}
		String text = new String(content.getXml());
		return text;
	}

	private List<String> getKeywords(String text) {
		try {
			String queryId = new VMID().toString();
			Tuple extractKeywordsTriggerTuple = new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS, AgentProtocol.QUERY,
					queryId, text);
			extractKeywordsTriggerTuple.setExpiration(2 * AgentProtocol.COMMAND_EXPIRATION);
			Tuple responseTuple = null;
			if (getCommandSpace().isConnected()) {
				getCommandSpace().write(extractKeywordsTriggerTuple);
				responseTuple = getCommandSpace().waitToTake(
						new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS, AgentProtocol.RESPONSE, queryId, Field
								.createWildCardField()));
			}
			if (responseTuple != null) {
				ArrayList<String> keywords = new ArrayList<String>();
				for (int i = 3; i < responseTuple.getNumberOfFields(); i++) {
					String keyword = (String) responseTuple.getField(i).getValue();
					keywords.add(keyword);
				}
				return keywords;
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return (listenerId == -1) && (status == Status.Stopping);
	}

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		metadataTypeManager = manager;
	}

	@Override
	public void setRepository(IRepository rep) {
		logger.debug("Setting repository ");
		repository = rep;
	}
}
