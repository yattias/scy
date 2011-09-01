package eu.scy.agents.general;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.parameter.AgentParameter;
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

/**
 * TODO Get all users for mission. Send notification for all users.
 */
public class ScaffoldingNotificatorAgent extends AbstractThreadedAgent {

    public static final String NAME = ScaffoldingNotificatorAgent.class.getName();

    private static final Logger LOGGER = Logger.getLogger(NAME);
    private int loginSeq;

    public ScaffoldingNotificatorAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
        try {
            this.loginSeq = this.getActionSpace()
                    .eventRegister(
                            Command.WRITE,
                            new Tuple(ActionConstants.ACTION, String.class,
                                    Long.class, ActionConstants.ACTION_LOG_IN,
                                    Field.createWildCardField()), this, true);
        } catch (TupleSpaceException e) {
            LOGGER.warn("Couldn't register listener for log in actions.", e);
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        try {
            while (this.status == AbstractThreadedAgent.Status.Running) {
                this.sendAliveUpdate();
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
            }
        } catch (Exception e) {
            LOGGER.fatal("*************** " + e.getMessage(), e);
            e.printStackTrace();
            throw new AgentLifecycleException(e.getMessage(), e);
        }
        if (this.status != AbstractThreadedAgent.Status.Stopping) {
            LOGGER.warn("************** Agent not stopped but run loop terminated *****************1");
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        // do nothing
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return status == AbstractThreadedAgent.Status.Stopping;
    }


    @Override
    protected void parametersChanged(AgentParameter agentParameter) {
        if (AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL.equals(agentParameter.getParameterName())) {
            String mission = agentParameter.getMission();
            Set<String> usersInMission = getSession().getUsersInMissionFromRuntime(mission);

            for (String user : usersInMission) {
                sendScaffoldLevelNotification(agentParameter.getParameterValue(), mission, user);
            }
        }


    }

    private <T> void sendScaffoldLevelNotification(T parameterValue, String mission, String user) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(user);
        notificationTuple.add(IAction.NOT_AVAILABLE);
        notificationTuple.add(NAME);
        notificationTuple.add(mission);
        notificationTuple.add(IAction.NOT_AVAILABLE);
        notificationTuple.add("type=scaffold");
        notificationTuple.add("level=" + parameterValue);

        try {
            getCommandSpace().write(notificationTuple);
        } catch (TupleSpaceException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        if (seq != loginSeq) {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }

        IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);

        sendScaffoldLevelNotification(configuration.getParameter(AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL),
                action.getContext(ContextConstants.mission), action.getUser());
    }
}
