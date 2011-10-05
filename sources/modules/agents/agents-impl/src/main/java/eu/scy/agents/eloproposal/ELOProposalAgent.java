package eu.scy.agents.eloproposal;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Send a proposal to open an ELO from one user to another
 * 
 * @author Adam Giemza
 */
public class ELOProposalAgent extends AbstractThreadedAgent {

    private static final String NAME = ELOProposalAgent.class.getName();

    private static final Object TYPE = "type=proposed_elo";

    private Tuple template;

    private ProposalCallback pc;

    private int registerId;

    private boolean isStopped;

    /**
     * Create a new ELOProposalAgent agent. The argument <code>map</code> is used to initialize special parameters. Never used here.
     * 
     * @param params
     *            Parameters needed to initialize the agent.
     */
    public ELOProposalAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
        if (params.containsKey(AgentProtocol.TS_HOST)) {
            this.host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if (params.containsKey(AgentProtocol.TS_PORT)) {
            this.port = (Integer) params.get(AgentProtocol.TS_PORT);
        }

        template = new Tuple("action", String.class, Long.class, "proposed_elo", Field.createWildCardField());
        pc = new ProposalCallback();
    }

    public void proposeEloToUser(IAction action, String proposingUserJid, String proposedEloUri, String proposedUserJid) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(proposedUserJid);
        notificationTuple.add("no specific elo");
        notificationTuple.add(NAME);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add(TYPE);
        notificationTuple.add("proposing_user=" + proposingUserJid);
        notificationTuple.add("proposed_elo=" + proposedEloUri);
        try {
            getCommandSpace().write(notificationTuple);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        registerId = getActionSpace().eventRegister(Command.WRITE, template, pc, true);
        isStopped = false;
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        try {
            getActionSpace().eventDeRegister(registerId);
            isStopped = true;
        } catch (TupleSpaceException e) {
            throw new AgentLifecycleException("Could not deregister callback", e);
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }
    
    private class ProposalCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            if (afterTuple.getField(0).getValue().toString().equals("action")) {
                IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
                String proposingUserJid = action.getAttribute("proposing_user");
                String proposedUserJid = action.getAttribute("proposed_user");
                String proposedEloUri = action.getAttribute("proposed_elo");
                proposeEloToUser(action, proposingUserJid, proposedEloUri, proposedUserJid);
            }
        }
        
    }

    // only for testing purpose, so DONT'T TOUCH IT
    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "ELOProposalAgent");
        map.put(AgentProtocol.TS_HOST, "scy.collide.info");
        map.put(AgentProtocol.TS_PORT, 2525);
        ELOProposalAgent ega = new ELOProposalAgent(map);
        ega.start();
    }
}
