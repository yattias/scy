package eu.scy.agents.collaboration;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.TupleSpaceException.TupleSpaceError;
import info.collide.sqlspaces.commons.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Now the collaboration agent interprets multiple proposed users separated with comma and can be
 * triggered using such a tuple in the commandspace:
 * 
 * (<ID>:String, "collaboration":String, "collaboration_request":String, <ProposedUser>:String,
 * <ELOURI>:String, <Mission>:String, <Session>:String)
 * 
 * 
 */
public class CollaborationAgent extends AbstractThreadedAgent {

    private static final int COLLABORATION_REQUEST_TIMEOUT = 30 * 1000;

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    private static final Logger logger = Logger.getLogger(CollaborationAgent.class.getName());

    private boolean isStopped;

    private Map<String, String> mucids;

    private Map<String, Timer> timeoutTimer;

    public CollaborationAgent(Map<String, Object> map) {
        super(CollaborationAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            mucids = new ConcurrentHashMap<String, String>();
            timeoutTimer = new ConcurrentHashMap<String, Timer>();
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
            Callback cc = new CollaborationCallback();
            commandSpace.eventRegister(Command.WRITE, new Tuple(String.class, "collaboration", "collaboration_request", Field.createWildCardField()), cc, false);
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
            String proposedUser;
            String proposingUser;
            String mucid;
            boolean requestAccepted;
            String mission;
            String session;
            String elouri;
            String type;

            // determine if triggered by user or by agent
            if (afterTuple.getField(0).getValue().toString().equals("action")) {
                Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
                mission = a.getContext(ContextConstants.mission);
                session = a.getContext(ContextConstants.session);
                elouri = a.getAttribute("proposed_elo");
                type = a.getType();
                if (type.equals("collaboration_request")) {
                    proposedUser = a.getAttribute("proposed_user");
                    proposingUser = a.getUser();
                    mucid = a.getAttribute("mucid");
                    handleCollaborationRequest(proposedUser, proposingUser, elouri, mission, session, mucid);
                } else if (type.equals("collaboration_response")) {
                    proposedUser = a.getUser();
                    proposingUser = a.getAttribute("proposing_user");
                    requestAccepted = Boolean.parseBoolean(a.getAttribute("request_accepted"));
                    handleCollaborationResponse(proposedUser, proposingUser, elouri, mission, session, requestAccepted);
                    return;
                }
            } else {
                type = afterTuple.getField(2).getValue().toString();
                proposedUser = afterTuple.getField(3).getValue().toString();
                elouri = afterTuple.getField(4).getValue().toString();
                mission = afterTuple.getField(5).getValue().toString();
                session = afterTuple.getField(6).getValue().toString();
                handleCollaborationRequest(proposedUser, "collaboration agent", elouri, mission, session, null);
            }
        }

        private void handleCollaborationResponse(String proposedUser, String proposingUser, String elouri, String mission, String session, boolean requestAccepted) {
            logger.debug("Got a collaboration response from user " + proposedUser + " to " + proposingUser + " for elo " + elouri + ", 'accepted' is " + requestAccepted);
            Timer t = timeoutTimer.remove(proposingUser + proposedUser);
            if (t != null) {
                t.stop();
            }
            if (requestAccepted) {
                try {
                    String mucId = mucids.remove(elouri);
                    if (mucId == null) {
                        String id = new VMID().toString();
                        commandSpace.write(new Tuple(id, "datasync", "create_session", elouri));
                        logger.debug("Requesting a new mucid from datasync (ID " + id + ")");
                        Tuple dataSyncResponse = commandSpace.waitToRead(new Tuple(id, String.class), 5000);
                        if (dataSyncResponse == null) {
                            throw new TupleSpaceException(TupleSpaceError.UNKNOWN, "Data sync did not answer within defined timeout");
                        }
                        mucId = dataSyncResponse.getField(1).getValue().toString();
                        logger.debug("Fetching datasync response for ID " + id + ", mucid is " + mucId);
                        sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=true", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "mucid=" + mucId, "proposed_elo=" + elouri);
                    }
                    sendNotification(proposedUser, mission, session, "type=collaboration_response", "accepted=true", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "mucid=" + mucId, "proposed_elo=" + elouri);
                } catch (TupleSpaceException e) {
                    // in case of problems dump a stacktrace and do as if
                    // request has not been
                    // accepted ...
                    sendNotification(proposedUser, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                    e.printStackTrace();
                }
            } else {
                sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
            }
        }

        private void handleCollaborationRequest(final String proposedUser, final String proposingUser, final String elouri, final String mission, final String session, String mucid) {
            if (mucid != null) {
                mucids.put(elouri, mucid);
            }
            if (proposedUser.contains(",")) {
                for (final String user : proposedUser.split(",")) {
                    logger.debug("Got a collaboration request from user " + proposingUser + " to " + user + " for elo " + elouri);
                    sendNotification(user, mission, session, "type=collaboration_request", "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                    final Timer t = new Timer(COLLABORATION_REQUEST_TIMEOUT, null);
                    t.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + user, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                            sendNotification(user, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + user, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                            t.stop();
                            timeoutTimer.remove(proposingUser + user);
                        }

                    });
                    t.start();
                    timeoutTimer.put(proposingUser + user, t);
                }
            } else {
                logger.debug("Got a collaboration request from user " + proposingUser + " to " + proposedUser + " for elo " + elouri);
                sendNotification(proposedUser, mission, session, "type=collaboration_request", "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                final Timer t = new Timer(COLLABORATION_REQUEST_TIMEOUT, null);
                t.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendNotification(proposedUser, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                        sendNotification(proposingUser, mission, session, "type=collaboration_response", "accepted=false", "proposed_user=" + proposedUser, "proposing_user=" + proposingUser, "proposed_elo=" + elouri);
                        t.stop();
                        timeoutTimer.remove(proposingUser + proposedUser);
                    }

                });
                t.start();
                timeoutTimer.put(proposingUser + proposedUser, t);
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
            logger.debug("Sending notification to " + username + ", params are " + Arrays.toString(params));
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

}
