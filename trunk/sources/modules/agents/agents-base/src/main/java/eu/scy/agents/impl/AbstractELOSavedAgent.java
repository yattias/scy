package eu.scy.agents.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;

public abstract class AbstractELOSavedAgent extends AbstractThreadedAgent {

	private int listenerId;

	// ("action":String, <ID>:String, <Timestamp>:long, elo_saved:String, <User>:String, <Tool>:String,
	// <Mission>:String, <Session>:String, <Key=Value>:String*)
	private Tuple eloSavedTupleTemplate = new Tuple(AgentProtocol.ACTION, String.class, Long.class,
			AgentProtocol.ACTION_ELO_SAVED, String.class, String.class, String.class, String.class, String.class);

	private static final Logger logger = Logger.getLogger(AbstractELOSavedAgent.class.getName());

	protected AbstractELOSavedAgent(String name, String id) {
		super(name, id);
	}

	public AbstractELOSavedAgent(String name, String id, String tsHost, int tsPort) {
		super(name, id, tsHost, tsPort);
	}

	private void initTSListener() {
		try {
			listenerId = getActionSpace().eventRegister(Command.WRITE, eloSavedTupleTemplate, this, true);
			logger.log(Level.FINEST, "Callback registered");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void beforeStart() {
		super.beforeStart();
		initTSListener();
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.COMMAND_EXPIRATION);
		}
	}

	@Override
	protected void doStop() {
		status = Status.Stopping;
		if (listenerId != 0) {
			try {
				getActionSpace().eventDeRegister(listenerId);
			} catch (TupleSpaceException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		logger.log(Level.FINE, name + " stopped");
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
	public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
		System.err.println("Processing tuple");
		if (listenerId != seq) {
			// If a callback arrives here that wasn't registered from this class it is passed to the
			// AbstractThreadedAgent.
			logger.log(Level.FINEST, "Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		}
		IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
		if (!AgentProtocol.ACTION_ELO_SAVED.equals(action.getType())) {
			logger.warning("Trying to process action log that does not match elo_saved signature. Type: "
					+ action.getType());
		} else {
			processELOSavedAction(action.getId(), action.getUser(), action.getTimeInMillis(), action
					.getContext(ContextConstants.tool), action.getContext(ContextConstants.mission), action
					.getContext(ContextConstants.session), action.getContext(ContextConstants.eloURI), action
					.getAttribute(AgentProtocol.ACTIONLOG_ELO_TYPE));
		}
	}

	protected abstract void processELOSavedAction(String actionId, String user, long timeInMillis, String tool,
			String mission, String session, String eloUri, String eloType);

}
