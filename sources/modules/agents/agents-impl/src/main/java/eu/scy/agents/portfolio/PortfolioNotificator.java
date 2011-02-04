package eu.scy.agents.portfolio;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * An agent that listens for e-portfolio events and writes notification tuples
 * for the webfrontend. It reacts on three events
 * <ol>
 * <li>ELO is added to portfolio</li>
 * <li>ELO is assessed</li>
 * <li>ELO assessment has been finished</li>
 * </ol>
 * it writes following tuples respectively
 * <ol>
 * <li>("elo_added_to_portfolio":String, <Mission>:String, <User>:String,
 * <EloURI>:String)</li>
 * <li>("elo_is_assessed":String, <Mission>:String, <User>:String,
 * <EloURI>:String)</li>
 * <li>("elo_assessment_finished":String, <Mission>:String, <User>:String,
 * <EloURI>:String)</li>
 * </ol>
 * 
 * @author fschulz
 * 
 */
public class PortfolioNotificator extends AbstractThreadedAgent {

	private static final String ELO_ASSESSMENT_FINISHED = "elo_assessment_finished";
	private static final String ELO_ADDED_TO_PORTFOLIO = "elo_added_to_portfolio";
	private static final String ELO_IS_ASSESSED = "elo_is_assessed";
	private static final String EPORTFOLIO_SUBMITTED_FOR_ASSESSMENT = "eportfolio_submitted_for_assessment";
	private static final String SCY_EPORTFOLIO_TOOL = "scy-eportfolio";
	private static final String PORTFOLIO_ASSESS_VIEW_FINISHED = "portfolio_assess_view_finished";
	private static final String SCY_ASSESSMENT_TOOL = "scy-assessment";

	private static final String NAME = PortfolioNotificator.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NAME);

	private int eloIsAssessedlistenerId;
	private int eloAssesmentFinishedListenerId;
	private int eloAddedToPortfolioListener;

	public PortfolioNotificator(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		this.setParameter(params);

		registerEloAddedToPortfolioListener();
		registerEloIsAssessedListener();
		registerAssessmentFinishedListener();
	}

	private void registerAssessmentFinishedListener() {
		try {
			this.eloAssesmentFinishedListenerId = this.getActionSpace()
					.eventRegister(
							Command.WRITE,
							new Tuple(AgentProtocol.ACTION, String.class,
									Long.class, PORTFOLIO_ASSESS_VIEW_FINISHED,
									String.class, SCY_ASSESSMENT_TOOL,
									String.class, String.class, String.class,
									Field.createWildCardField()), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void registerEloIsAssessedListener() {
		try {
			this.eloIsAssessedlistenerId = this.getActionSpace().eventRegister(
					Command.WRITE,
					new Tuple(AgentProtocol.ACTION, String.class, Long.class,
							AgentProtocol.ACTION_TOOL_STARTED, String.class,
							SCY_ASSESSMENT_TOOL, String.class, String.class,
							String.class, Field.createWildCardField()), this,
					true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void registerEloAddedToPortfolioListener() {
		try {
			this.eloAddedToPortfolioListener = this.getActionSpace()
					.eventRegister(
							Command.WRITE,
							new Tuple(AgentProtocol.ACTION, String.class,
									Long.class,
									EPORTFOLIO_SUBMITTED_FOR_ASSESSMENT,
									String.class, SCY_EPORTFOLIO_TOOL,
									String.class, String.class, String.class,
									Field.createWildCardField()), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void setParameter(Map<String, Object> params) {
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		try {
			while (this.status == Status.Running) {
				this.sendAliveUpdate();
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
			}
		} catch (Exception e) {
			LOGGER.fatal("*************** " + e.getMessage());
			e.printStackTrace();
			throw new AgentLifecycleException(e.getMessage(), e);
		}
		if (this.status != Status.Stopping) {
			LOGGER
					.warn("************** Agent not stopped but run loop terminated *****************1");
		}
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		try {
			this.getActionSpace().eventDeRegister(
					this.eloAddedToPortfolioListener);
			this.eloAddedToPortfolioListener = -1;

			this.getActionSpace().eventDeRegister(
					this.eloAssesmentFinishedListenerId);
			this.eloAssesmentFinishedListenerId = -1;

			this.getActionSpace().eventDeRegister(this.eloIsAssessedlistenerId);
			this.eloIsAssessedlistenerId = -1;
		} catch (TupleSpaceException e) {
			LOGGER.fatal("Could not deregister tuple space listener: "
					+ e.getMessage());
		}
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
		if (seq != eloAddedToPortfolioListener
				&& seq != eloAssesmentFinishedListenerId
				&& seq != eloIsAssessedlistenerId) {
			LOGGER.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			IAction action = ActionTupleTransformer
					.getActionFromTuple(afterTuple);

			if (seq == eloAddedToPortfolioListener) {
				handleEloAddedToPortfolio(action);
			}
			if (seq == eloIsAssessedlistenerId) {
				handleEloIsAssessed(action);
			}
			if (seq == eloAssesmentFinishedListenerId) {
				handleAssessmentFinished(action);
			}
		}
	}

	private void handleEloAddedToPortfolio(IAction action) {
		Tuple eloAddedTuple = createAssessmentNotificationTuple(
				ELO_ADDED_TO_PORTFOLIO, action);
		try {
			getCommandSpace().write(eloAddedTuple);
		} catch (TupleSpaceException e) {
			LOGGER.warn("could not write elo added to portfolio tuple");
			e.printStackTrace();
		}
	}

	private void handleEloIsAssessed(IAction action) {
		Tuple eloAddedTuple = createAssessmentNotificationTuple(
				ELO_ADDED_TO_PORTFOLIO, action);
		try {
			if (!getCommandSpace().delete(eloAddedTuple)) {
				LOGGER
						.info("tuple " + eloAddedTuple
								+ " could not be deleted.");
			}
			Tuple eloIsAssessedTuple = createAssessmentNotificationTuple(
					ELO_IS_ASSESSED, action);
			getCommandSpace().write(eloIsAssessedTuple);
		} catch (TupleSpaceException e) {
			LOGGER.warn("could not write elo added to portfolio tuple");
			e.printStackTrace();
		}

	}

	private void handleAssessmentFinished(IAction action) {
		Tuple eloIsAssessedTuple = createAssessmentNotificationTuple(
				ELO_IS_ASSESSED, action);
		try {
			if (!getCommandSpace().delete(eloIsAssessedTuple)) {
				LOGGER.info("tuple " + eloIsAssessedTuple
						+ " could not be deleted.");
			}
			Tuple eloAssessmentFinishedTuple = createAssessmentNotificationTuple(
					ELO_ASSESSMENT_FINISHED, action);
			getCommandSpace().write(eloAssessmentFinishedTuple);
		} catch (TupleSpaceException e) {
			LOGGER.warn("could not write elo added to portfolio tuple");
			e.printStackTrace();
		}
	}

	private Tuple createAssessmentNotificationTuple(String type, IAction action) {
		return new Tuple(type, action.getContext(ContextConstants.mission),
				action.getUser(), action.getContext(ContextConstants.eloURI));
	}

}
