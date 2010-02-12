package eu.scy.agents.collaboration;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.Map;
import java.util.logging.Logger;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CollaborationAgent extends AbstractThreadedAgent {

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    private static final Logger logger = Logger.getLogger(CollaborationAgent.class.getName());

    private boolean isStopped;

    public CollaborationAgent(Map<String, Object> map) {
        super(CollaborationAgent.class.toString(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
            Callback cc = new CollaborationCallback();
            actionSpace.eventRegister(Command.WRITE, new Tuple("action", String.class, Long.class, "collaboration_request", Field.createWildCardField()), cc, false);
            actionSpace.eventRegister(Command.WRITE, new Tuple("action", String.class, Long.class, "collaboration_response", Field.createWildCardField()), cc, true);
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
    protected void doStop() {
        try {
            actionSpace.disconnect();
            commandSpace.disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        // TODO
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    class CollaborationCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
            String mission = a.getContext(ContextConstants.mission);
            String session = a.getContext(ContextConstants.session);
            String elouri = a.getAttribute("proposed_elo");
            if (a.getType().equals("collaboration_request")) {
                String proposedUser = a.getAttribute("proposed_user");
                String proposingUser = a.getUser();
                logger.fine("Got a collaboration request from user " + proposingUser + " to " + proposedUser + " for elo " + elouri);
                sendNotification(proposedUser, mission, session, "type=collaboration_request", "proposing_user=" + proposingUser, "elo=" + elouri);
            } else if (a.getType().equals("collaboration_response")) {
                boolean requestAccepted = Boolean.parseBoolean(a.getAttribute("request_accepted"));
                String proposedUser = a.getUser();
                String proposingUser = a.getAttribute("proposing_user");
                logger.fine("Got a collaboration response from user " + proposedUser + " to " + proposingUser + " for elo " + elouri + ", 'accepted' is " + requestAccepted);
                if (requestAccepted) {
                    try {
                        String id = new VMID().toString();
                        commandSpace.write(new Tuple(id, "datasync", "create_session"));
                        Tuple dataSyncResponse = commandSpace.waitToTake(new Tuple(id, String.class));
                        String mucId = dataSyncResponse.getField(1).getValue().toString();
                        sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=true", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "mucid=" + mucId);
                        sendNotification(proposedUser, mission, session, "type=collaboration_response", "accepted=true", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "mucid=" + mucId);
                    } catch (TupleSpaceException e) {
                        // in case of problems dump a stacktrace and do as if request has not been
                        // accepted ...
                        sendNotification(proposedUser, mission, session, "type=collaboration_response", "accepted=false");
                        e.printStackTrace();
                    }
                } else {
                    sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=false");
                }
            }
        }

    }

    private void sendNotification(String username, String mission, String session, String... params) {
        try {
            Tuple notificationTuple = new Tuple("notification", new VMID().toString(), username, "scylab", "collaboration agent", mission, session);
            for (String param : params) {
                notificationTuple.add(param);
            }
            commandSpace.write(notificationTuple);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

}
