package eu.scy.agents.general;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.rmi.dgc.VMID;
import java.util.Map;

public class ScaffoldingNotificatorAgent extends AbstractThreadedAgent {

    public static final String GLOBAL_SCAFFOLDING_LEVEL = "globalScaffoldingLevel";
    public static final String NAME = ScaffoldingNotificatorAgent.class.getName();

    private static final Logger LOGGER = Logger.getLogger(NAME);

    public ScaffoldingNotificatorAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
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
        if (GLOBAL_SCAFFOLDING_LEVEL.equals(agentParameter.getParameterName())) {
            Tuple notificationTuple = new Tuple();
            notificationTuple.add(AgentProtocol.NOTIFICATION);
            notificationTuple.add(new VMID().toString());
            notificationTuple.add(IAction.NOT_AVAILABLE);
            notificationTuple.add(IAction.NOT_AVAILABLE);
            notificationTuple.add(NAME);
            notificationTuple.add(agentParameter.getMission());
            notificationTuple.add(IAction.NOT_AVAILABLE);
            notificationTuple.add("type=scaffold");
            notificationTuple.add("level=" + agentParameter.getParameterValue());

            try {
                getCommandSpace().write(notificationTuple);
            } catch (TupleSpaceException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

}
