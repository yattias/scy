package eu.scy.agents.feedback;

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
import java.util.Set;

public class FeedbackAgent extends AbstractThreadedAgent {

    public static final String NAME = FeedbackAgent.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private static final String FEEDBACK_GIVEN = "feedback_given";
    private static final String FEEDBACK_ASKED = "feedback_asked";

    private int feedbackAskedSeq;
    private int feedbackGivenSeq;

    public FeedbackAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));

        try {
            this.feedbackAskedSeq = this.getActionSpace()
                    .eventRegister(
                            Command.WRITE,
                            new Tuple(ActionConstants.ACTION, String.class,
                                    Long.class, FEEDBACK_ASKED,
                                    Field.createWildCardField()), this, true);
            this.feedbackGivenSeq = this.getActionSpace()
                    .eventRegister(
                            Command.WRITE,
                            new Tuple(ActionConstants.ACTION, String.class,
                                    Long.class, FEEDBACK_GIVEN,
                                    Field.createWildCardField()), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
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
                    this.feedbackGivenSeq);
            this.feedbackGivenSeq = -1;

            this.getActionSpace().eventDeRegister(this.feedbackAskedSeq);
            this.feedbackAskedSeq = -1;
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
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
        if ( seq == feedbackAskedSeq ) {
            IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
            handleFeedbackAsked(action);
        }
        if ( seq == feedbackGivenSeq ) {
            IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
            handleFeedbackGiven(action);
        } else {
            LOGGER.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }
    }

    private void handleFeedbackAsked(IAction action) {
        try {
            String missionRT = action.getContext(ContextConstants.mission);
            Set<String> usersInMission = getSession().getUsersInMissionFromRuntime(missionRT);
            String missionSpec = getSession().getMissionSpecification(missionRT);
            usersInMission.remove(action.getUser());

            for ( String user : usersInMission ) {
                Tuple notification = createNotification(user, missionSpec, action, FEEDBACK_ASKED);
                getCommandSpace().write(notification);
            }
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("could not write elo added to portfolio tuple");
            e.printStackTrace();
        }
    }

    private void handleFeedbackGiven(IAction action) {
        try {
            String user = action.getUser();
            String mission = getSession().getMissionRuntimeURI(user);
            this.getCommandSpace().write(createNotification(user, mission, action, FEEDBACK_GIVEN));
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("could not write elo added to portfolio tuple");
            e.printStackTrace();
        }
    }

    private Tuple createNotification(String user, String missionSpec, IAction action, String type) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(user);
        notificationTuple.add(action.getContext(ContextConstants.eloURI));
        notificationTuple.add(NAME);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));

        notificationTuple.add("type=" + type);
        return notificationTuple;
    }
}

