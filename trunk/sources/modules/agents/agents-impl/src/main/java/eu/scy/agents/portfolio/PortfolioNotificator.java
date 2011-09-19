package eu.scy.agents.portfolio;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.rmi.dgc.VMID;
import java.util.Map;

/**
 * An agent that listens for e-portfolio events and writes notification tuples for the webfrontend. It reacts on three events <ol> <li>ELO
 * is added to portfolio</li> <li>ELO is assessed</li> <li>ELO assessment has been finished</li> </ol> it writes following tuples
 * respectively sends a notification <ol> <li>("elo_added_to_portfolio":String, <Mission>:String, <User>:String, <EloURI>:String)</li>
 * <li>("elo_is_assessed":String, <Mission>:String, <User>:String, <EloURI>:String)</li> <li>notification with
 * type=elo_assessment_finished</li> </ol>
 *
 * @author fschulz
 */
public class PortfolioNotificator extends AbstractThreadedAgent {

    private static final String PORTFOLIO_ASSESSED = "portfolio_assessed";
    private static final String ELO_ASSESSMENT_FINISHED = "elo_assessment_finished";

    private static final String NAME = PortfolioNotificator.class.getName();

    private static final Logger LOGGER = Logger.getLogger(NAME);
    private int assesmentFinishedListenerId;

    public PortfolioNotificator(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
        this.setParameter(params);

        registerAssessmentFinishedListener();
    }

    private void registerAssessmentFinishedListener() {
        try {
            this.assesmentFinishedListenerId = this.getActionSpace()
                    .eventRegister(Command.WRITE, new Tuple(ActionConstants.ACTION, String.class, Long.class, PORTFOLIO_ASSESSED,
                            Field.createWildCardField()), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    private void setParameter(Map<String, Object> params) {
        if ( params.containsKey(AgentProtocol.TS_HOST) ) {
            this.host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if ( params.containsKey(AgentProtocol.TS_PORT) ) {
            this.port = (Integer) params.get(AgentProtocol.TS_PORT);
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {
        try {
            while ( this.status == Status.Running ) {
                this.sendAliveUpdate();
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
            }
        } catch ( Exception e ) {
            LOGGER.fatal("*************** " + e.getMessage(), e);
            e.printStackTrace();
            throw new AgentLifecycleException(e.getMessage(), e);
        }
        if ( this.status != Status.Stopping ) {
            LOGGER.warn("************** Agent not stopped but run loop terminated *****************1");
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        try {
            this.getActionSpace().eventDeRegister(
                    this.assesmentFinishedListenerId);
            this.assesmentFinishedListenerId = -1;
        } catch ( TupleSpaceException e ) {
            LOGGER.fatal(
                    "Could not deregister tuple space listener: "
                            + e.getMessage(), e);
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
        if ( seq != assesmentFinishedListenerId ) {
            LOGGER.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        } else {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(afterTuple);

            if ( seq == assesmentFinishedListenerId ) {
                handleAssessmentFinished(action);
            }
        }
    }

    private void handleAssessmentFinished(IAction action) {
        try {
            String mission = getSession().getMissionSpecification(action.getContext(ContextConstants.mission));
            this.getCommandSpace().write(createNotification(action, mission, ELO_ASSESSMENT_FINISHED));
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("could not write elo added to portfolio tuple");
            e.printStackTrace();
        }
    }

    private Tuple createNotification(IAction action, String missionSpec, String type) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(action.getUser());
        notificationTuple.add(action.getContext(ContextConstants.eloURI));
        notificationTuple.add(NAME);
        notificationTuple.add(missionSpec);
        notificationTuple.add(action.getContext(ContextConstants.session));

        notificationTuple.add("type=" + type);
        return notificationTuple;
    }
}
