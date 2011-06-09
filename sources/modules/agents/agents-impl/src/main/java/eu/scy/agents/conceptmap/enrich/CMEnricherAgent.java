package eu.scy.agents.conceptmap.enrich;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMEnricherAgent extends AbstractThreadedAgent {

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "enricher id");
        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_PORT, 2525);
        CMEnricherAgent agent = new CMEnricherAgent(map);
        agent.start();
    }
    
    public CMEnricherAgent(Map<String, Object> map) {
        super(CMEnricherAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
            Callback cb = new CMEnricherCallback();
            actionSpace.eventRegister(Command.WRITE, new Tuple("action", String.class, Long.class, Field.createSemiformalField("*_help_requested"), String.class, "scymapper", String.class, String.class, String.class), cb, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun(){
        while (status == Status.Running) {
            try {
                Thread.sleep(5000);
                sendAliveUpdate();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        try {
            commandSpace.disconnect();
            actionSpace.disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    protected Tuple getIdentifyTuple(String arg0) {
        return null;
    }

    private String[] askForProposals(String elouri, String user) throws TupleSpaceException {
        String id = new VMID().toString();
        Tuple proposalRequestTuple = new Tuple(id, "CMProposer", "cm proposal", user, elouri, 3, "degree");
        commandSpace.write(proposalRequestTuple);
        Tuple proposalResponseTuple = commandSpace.waitToTake(new Tuple(id, "response", Field.createWildCardField()));
        String[] result = new String[proposalResponseTuple.getNumberOfFields() - 2];
        for (int i = 0; i < proposalResponseTuple.getNumberOfFields() - 2; i++) {
            result[i] = proposalResponseTuple.getField(i + 2).getValue().toString();
        }
        return result;
    }

    private void sendFeeback(String id, String elouri, String user, String mission, String session, String type, String... proposals) throws TupleSpaceException {
        Tuple notificationTuple = new Tuple("notification", id, user, elouri, "cmenricher agent", mission, session, "proposal_type=" + type);
        for (String proposal : proposals) {
            if (proposal.startsWith(type) || proposal.startsWith("error")) {
                notificationTuple.add(proposal);
            }
        }
        commandSpace.write(notificationTuple);
    }

    class CMEnricherCallback implements Callback {

        @Override
        public synchronized void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            String id = afterTuple.getField(1).getValue().toString();
            String request = afterTuple.getField(3).getValue().toString();
            String user = afterTuple.getField(4).getValue().toString();
            String mission = afterTuple.getField(6).getValue().toString();
            String session = afterTuple.getField(7).getValue().toString();
            String elouri = afterTuple.getField(8).getValue().toString();
            
            String type = request.split("_")[0];
            try {
                String[] proposals = askForProposals(elouri, user);
                sendFeeback(id, elouri, user, mission, session, type, proposals);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }

    }

}