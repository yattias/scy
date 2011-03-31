package eu.scy.agents.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;

public abstract class AbstractELOSavedAgent extends AbstractThreadedAgent {

	private int listenerId;

	// ("action":String, <ID>:String, <Timestamp>:long, "elo_saved":String,
	// <User>:String, <Tool>:String,
	// <Mission>:String, <Session>:String, <Key=Value>:String*)
	private Tuple eloSavedTupleTemplate = new Tuple(ActionConstants.ACTION,
			String.class, Long.class, ActionConstants.ACTION_ELO_SAVED,
			Field.createWildCardField());

	private static final Logger logger = Logger
			.getLogger(AbstractELOSavedAgent.class.getName());

	protected AbstractELOSavedAgent(String name, String id) {
		this(name, id, DEFAULT_HOST, Configuration.getConfiguration()
				.getNonSSLPort());
	}

	public AbstractELOSavedAgent(String name, String id, String tsHost,
			int tsPort) {
		super(name, id, tsHost, tsPort);
		this.initTSListener();
	}

	private void initTSListener() {
		try {
			this.listenerId = this.getActionSpace().eventRegister(
					Command.WRITE, this.eloSavedTupleTemplate, this, true);
			logger.log(Level.INFO, "Callback registered");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void beforeStart() {
		super.beforeStart();
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (this.status == Status.Running) {
			this.sendAliveUpdate();
			Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
		}
	}

	@Override
	protected void doStop() {
		this.status = Status.Stopping;
		if (this.listenerId != 0) {
			try {
				this.getActionSpace().eventDeRegister(this.listenerId);
			} catch (TupleSpaceException e) {
				e.printStackTrace();
				logger.severe(e.getMessage());
			}
		}
		logger.log(Level.FINE, this.name + " stopped");
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return this.status == Status.Stopping;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (this.listenerId != seq) {
			// If a callback arrives here that wasn't registered from this class
			// it is passed to the
			// AbstractThreadedAgent.
			logger.log(Level.FINEST, "Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		}
		IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
		if (!ActionConstants.ACTION_ELO_SAVED.equals(action.getType())) {
			logger.warning("Trying to process action log that does not match elo_save signature. Type: "
					+ action.getType());
		} else {
			this.processELOSavedAction(
					action.getId(),
					action.getUser(),
					action.getTimeInMillis(),
					action.getContext(ContextConstants.tool),
					action.getContext(ContextConstants.mission),
					action.getContext(ContextConstants.session),
					// action.getContext(ContextConstants.eloURI),
					// getting the eloUri from the properties, not from the
					// context-constants
					action.getAttribute(ActionConstants.ACTIONLOG_ELO_URI),
					action.getAttribute(ActionConstants.ACTIONLOG_ELO_TYPE));
		}
	}

	public abstract void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType);

}
