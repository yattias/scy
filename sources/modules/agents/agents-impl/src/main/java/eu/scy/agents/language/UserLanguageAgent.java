package eu.scy.agents.language;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsDecisionMakerAgent;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This agent keeps track of the language a student uses when he logs into scy
 * lab. The language is recorded via action logs and can be requested by
 * following tuple. <br/>
 * <p>
 * ("language", "query", <QueryId>:String, <Mission>:String, <User>:String)
 * </p>
 * the answer is sent in this format. <br/>
 * <p>
 * ("language", "response", <QueryID>:String, <Language>:String)
 * </p>
 * 
 * @author Florian Schulz
 * 
 */
public class UserLanguageAgent extends AbstractRequestAgent {

	private static final Logger logger = Logger
			.getLogger(ExtractKeywordsDecisionMakerAgent.class.getName());

	static final String LANGUAGE = "language";
	public static final String NAME = UserLanguageAgent.class.getName();
	private int listenerId;
	private int actionLogId;

	private Map<String, String> languageMap;

	public UserLanguageAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		registerListener();

		languageMap = new ConcurrentHashMap<String, String>();
	}

	private void registerListener() {
		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE,
					getActivationTuple(), this, true);
			actionLogId = getActionSpace().eventRegister(Command.WRITE,
					getActionLogTupleTemplate(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private Tuple getActionLogTupleTemplate() {
		return new Tuple(AgentProtocol.ACTION, String.class, Long.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, Field.createWildCardField());
	}

	/*
	 * ("language", "query", <QueryId>:String, <Mission>:String, <User>:String)
	 */
	private Tuple getActivationTuple() {
		return new Tuple(LANGUAGE, AgentProtocol.QUERY, String.class,
				String.class, String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			} catch (InterruptedException e) {
				throw new AgentLifecycleException(e.getMessage(), e);
			}
		}
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if ((listenerId != seq) && (seq != actionLogId)) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			if (afterTuple.getField(0).getValue().equals(LANGUAGE)) {
				handleRequest(afterTuple);
			} else if (afterTuple.getField(0).getValue()
					.equals(AgentProtocol.ACTION)) {
				handleUserLoggedInAction(ActionTupleTransformer
						.getActionFromTuple(afterTuple));
			} else {
				logger.warn("wrong tuple activated UserLanguageAgent: "
						+ afterTuple);
			}
		}
	}

	private void handleRequest(Tuple afterTuple) {
		String queryId = (String) afterTuple.getField(2).getValue();
		String mission = (String) afterTuple.getField(3).getValue();
		String user = (String) afterTuple.getField(4).getValue();

		String language = languageMap.get(mission + user);
		if (language != null) {
			sendResponse(queryId, language);
		}

	}

	/*
	 * ->("language", "response", <Language>:String)
	 */
	private void sendResponse(String queryId, String language) {
		Tuple responseTuple = new Tuple(LANGUAGE, AgentProtocol.RESPONSE,
				queryId, language);
		try {
			getCommandSpace().write(responseTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}

	// TODO adapt to action log provided by Lars
	private void handleUserLoggedInAction(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		String user = action.getUser();
		String language = action.getAttribute(LANGUAGE);
		String key = mission + user;

		languageMap.put(key, language);
	}
}
