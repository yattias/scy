package eu.scy.agents.conceptmap.enrich;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMEnricherAgent extends AbstractThreadedAgent {

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    protected CMEnricherAgent(Map<String, Object> map) {
        super(CMEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
            // TODO register callback for help button action
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {}

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    protected Tuple getIdentifyTuple(String arg0) {
        return null;
    }

    private void askForProposals(String elouri, String user) throws TupleSpaceException {
        Tuple proposalRequestTuple = new Tuple(new VMID().toString(), "CMProposer", "cm proposal", user, elouri, 3, "degree");
        commandSpace.write(proposalRequestTuple);
    }
    
    private void sendFeeback(String elouri, String user, String mission, String session, String... proposals) throws TupleSpaceException {
        Tuple notificationTuple = new Tuple("notification", new VMID().toString(), user, "scymapper", "cmenricher agent", mission, session);
        for (String proposal : proposals) {
            notificationTuple.add("proposal=" + proposal);
        }
        commandSpace.write(notificationTuple);
    }
    
    
    
}