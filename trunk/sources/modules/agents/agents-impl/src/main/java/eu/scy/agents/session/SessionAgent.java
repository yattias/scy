package eu.scy.agents.session;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.rmi.dgc.VMID;
import java.util.Map;

/**
 * An agent that tracks the locations of users. Which mission and which las they are in.
 * <p/>
 * The agent can be queried for: <ul> <li>Which LAS a user is in: ("userInfoRequest","query", <QueryId>:String, <Mission>:String,
 * "lasOfUser", <UserName>:String) -> ("userInfoRequest", "response", <QueryId>:String, <Las>:String)</li> <li>What users are in a LAS:
 * ("userInfoRequest","query", <QueryId>:String, <Mission>:String, "userInLas", <LASId>:String) -> ("userInfoRequest", "response",
 * <QueryId>:String, <User>:String*)</li> <li>What users are in a Mission: ("userInfoRequest","query", <QueryId>:String, <Mission>:String,
 * "userInMission") -> ("userInfoRequest", "response", <QueryId>:String, <User>:String*)</li> </ul>
 *
 * @author fschulz
 */
public class SessionAgent extends AbstractRequestAgent {

    private static final int SESSION_TUPLE_EXPIRATION = AgentProtocol.HOUR * 4;
    private static final String MISSION_NAME = "missionName";
    private static final String MISSION_ID = "missionId";
    private static final String MISSION_SPECIFICATION = "missionSpecification";

    public static final String NAME = SessionAgent.class.getName();

    private static final Logger LOGGER = Logger.getLogger(SessionAgent.class);

    private int actionTupleListenerId;

    public SessionAgent(Map<String, Object> params) {
        super(NAME, params);
        if ( params.containsKey(AgentProtocol.TS_HOST) ) {
            host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if ( params.containsKey(AgentProtocol.TS_PORT) ) {
            port = (Integer) params.get(AgentProtocol.TS_PORT);
        }
        try {
            actionTupleListenerId = getActionSpace().eventRegister(
                    Command.WRITE, getActivationTuple(), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    private Tuple getActivationTuple() {
        return new Tuple(ActionConstants.ACTION, String.class, Long.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, Field.createWildCardField());
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {
        while ( status == Status.Running ) {
            sendAliveUpdate();
            try {
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
            } catch ( InterruptedException e ) {
                throw new AgentLifecycleException(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        status = Status.Stopping;
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
    public void call(Command command, int seq, Tuple activationTuple,
                     Tuple beforeTuple) {
        if ( seq == actionTupleListenerId ) {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(activationTuple);
            String type = action.getType();
            if ( type.equals(ActionConstants.ACTION_TOOL_OPENED) ) {
                handleToolOpened(action);
            } else if ( type.equals(ActionConstants.ACTION_TOOL_CLOSED) ) {
                handleToolClosed(action);
            } else if ( type.equals(ActionConstants.ACTION_LAS_CHANGED) ) {
                handleLasChanged(action);
            } else if ( type.equals(ActionConstants.ACTION_LOG_IN) ) {
                handleLogin(action);
            } else if ( type.equals(ActionConstants.ACTION_LOG_OUT) ) {
                handleLogout(action);
            } else if ( type.equals(ActionConstants.ACTION_ELO_SAVED) ) {
                updateToolOpened(action);
            }
        } else {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, activationTuple, beforeTuple);
            return;
        }
    }

    private void updateToolOpened(IAction action) {
        String oldEloUri = action.getAttribute(ActionConstants.OLD_ELO_URI);
        String newEloUri = action.getAttribute(ActionConstants.ELO_URI);

        if ( oldEloUri == null ) {
            LOGGER.warn("elo_saved action does not contain oldUri: " + action);
            return;
        }
        if ( newEloUri == null ) {
            LOGGER.warn("elo_saved action does not contain newUri: " + action);
            return;
        }
        try {
            Tuple[] tuples = getSessionSpace().readAll(
                    new Tuple(Session.TOOL, action.getUser(), action
                            .getContext(ContextConstants.tool), oldEloUri));
            for ( Tuple tuple : tuples ) {
                Tuple toolTuple = new Tuple(Session.TOOL, action.getUser(),
                        action.getContext(ContextConstants.tool), newEloUri);
                toolTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
                getSessionSpace().update(tuple.getTupleID(), toolTuple);
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }


    private void handleLogout(IAction action) {
        try {
            cleanSession(action);
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("", e);
        }
    }

    private void cleanSession(IAction action) throws TupleSpaceException {
        getSessionSpace().deleteAll(
                new Tuple(String.class, action.getUser(), String.class));
        getSessionSpace().deleteAll(
                new Tuple(String.class, action.getUser(), String.class,
                        Field.createWildCardField()));
    }

    private void handleLogin(IAction action) {
        String user = action.getUser();

        try {
            cleanSession(action);
            String language = action.getAttribute(Session.LANGUAGE);
            if ( language != null ) {
                Tuple languageTuple = new Tuple(Session.LANGUAGE, user,
                        language);
                languageTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
                getSessionSpace().write(languageTuple);
            } else {
                LOGGER.warn("language is null");
            }
            String missionSpecification = action.getAttribute(MISSION_SPECIFICATION);
            if ( missionSpecification == null ) {
                LOGGER.warn("missionspecification is null");
            }
            String missionRuntime = action.getContext(ContextConstants.mission);
            if ( missionRuntime == null ) {
                LOGGER.warn("missionspecification is null");
            }
            String missionName = action.getAttribute(MISSION_NAME);
            if ( missionName == null ) {
                LOGGER.warn("missionName is null");
            }
            String missionId = action.getAttribute(MISSION_ID);
            if ( missionId == null ) {
                LOGGER.warn("missionName is null");
            }
            Tuple missionTuple = new Tuple(Session.MISSION, user, missionSpecification, missionName, missionRuntime, missionId);
            missionTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
            getSessionSpace().write(missionTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("", e);
        }
    }

    private void handleLasChanged(IAction action) {
        try {
            Tuple missionTuple = getSessionSpace().read(
                    new Tuple(Session.MISSION, action.getUser(), String.class,
                            String.class, String.class, String.class));
            String missionSpec = missionTuple.getField(Session.MISSION_SPECIFICATION_FIELD).getValue().toString();
            getSessionSpace().deleteAll(
                    new Tuple(Session.LAS, action.getUser(), missionSpec,
                            String.class));

            updateTuples(action);

            Tuple lasTuple = new Tuple(Session.LAS, action.getUser(),
                    missionSpec, action.getAttribute(ActionConstants.LAS));
            lasTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
            getSessionSpace().write(lasTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("", e);
        }
        sendNotification(action);
    }

    private void sendNotification(IAction action) {
        Tuple notification = new Tuple();
        notification.add(AgentProtocol.NOTIFICATION);
        notification.add(new VMID().toString());
        notification.add(action.getUser());
        notification.add(action.getContext(ContextConstants.eloURI));
        notification.add(NAME);
        notification.add(action.getContext(ContextConstants.mission));
        notification.add(action.getContext(ContextConstants.session));

        notification.add("type=authoring_notification");
        notification.add("oldLas="
                + action.getAttribute(ActionConstants.OLD_LAS));
        notification.add("newLas=" + action.getAttribute(ActionConstants.LAS));
        try {
            getCommandSpace().write(notification);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    private void handleToolClosed(IAction action) {
        try {
            getSessionSpace().deleteAll(
                    new Tuple(Session.TOOL, action.getUser(), action
                            .getContext(ContextConstants.tool), action
                            .getContext(ContextConstants.eloURI)));
            updateTuples(action);
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("", e);
        }
    }

    private void handleToolOpened(IAction action) {
        try {
            updateTuples(action);
            Tuple toolTuple = new Tuple(Session.TOOL, action.getUser(),
                    action.getContext(ContextConstants.tool),
                    action.getContext(ContextConstants.eloURI));
            toolTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
            getSessionSpace().write(toolTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.warn("", e);
        }
    }

    private void updateTuples(IAction action) throws TupleSpaceException {
        Tuple[] languageTuples = getSessionSpace().readAll(
                new Tuple(String.class, action.getUser(), String.class));
        for ( Tuple languageTuple : languageTuples ) {
            languageTuple.setExpiration(SESSION_TUPLE_EXPIRATION);
            getSessionSpace().update(languageTuple.getTupleID(), languageTuple);
        }

        Tuple[] tuples = getSessionSpace().readAll(
                new Tuple(String.class, action.getUser(), String.class,
                        String.class));
        for ( Tuple tuple : tuples ) {
            tuple.setExpiration(SESSION_TUPLE_EXPIRATION);
            getSessionSpace().update(tuple.getTupleID(), tuple);
        }
    }
}
