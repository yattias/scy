package eu.scy.agents.keywords;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractDecisionAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.WebresourceExtractor;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtractKeywordsDecisionMakerAgent extends AbstractDecisionAgent
        implements IRepositoryAgent {

    private static final String UNSAVED_ELO = "unsavedELO";

    class ContextInformation {

        public String mission;

        public String session;

        public String user;

        public boolean webresourcerStarted = false;

        public boolean scyMapperStarted = false;

        public long lastAction;

        public int numberOfConcepts = 0;

        public URI webresourcerELO = null;

        public long lastNotification;

        public URI scyMapperElo;

        @Override
        public String toString() {
            String webresourcerURL = this.webresourcerELO != null ? this.webresourcerELO
                    .toString() : "null";
            return this.user + "|web:" + this.webresourcerStarted + "|map:"
                    + this.scyMapperStarted + "|url:" + webresourcerURL + "|"
                    + new Date(this.lastAction).toString() + "|"
                    + this.numberOfConcepts;
        }
    }

    private static final Logger LOGGER = Logger
            .getLogger(ExtractKeywordsDecisionMakerAgent.class.getName());

    static final String NAME = ExtractKeywordsDecisionMakerAgent.class
            .getName();

    static final Object SCYMAPPER = "scymapper";
    static final Object CONCEPTMAP = "conceptmap";
    static final Object WEBRESOURCER = "webresource";
    public static final String IDLE_TIME_INMS = "idleTime";
    public static final String MINIMUM_NUMBER_OF_CONCEPTS = "minimumNumberOfConcepts";
    public static final String TIME_AFTER_USERS_ARE_REMOVED = "timeAfterUserIsRemoved";

    private int listenerId = -1;

    private ConcurrentHashMap<String, ContextInformation> user2Context;

    private IRepository repository;

    private KeywordExtractor extractor;

    public ExtractKeywordsDecisionMakerAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));

        this.setParameter(params);

        this.registerToolStartedListener();
        this.user2Context = new ConcurrentHashMap<String, ContextInformation>();
        extractor = new WebresourceExtractor();
        extractor.setTupleSpace(getCommandSpace());
    }

    private void setParameter(Map<String, Object> params) {
        if (params.containsKey(AgentProtocol.TS_HOST)) {
            this.host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if (params.containsKey(AgentProtocol.TS_PORT)) {
            this.port = (Integer) params.get(AgentProtocol.TS_PORT);
        }

        configuration.addAllParameter(params);
        if (!configuration.containsParameter(IDLE_TIME_INMS)) {
            configuration
                    .setParameter(IDLE_TIME_INMS, 5 * AgentProtocol.MINUTE);
        }
        if (!configuration.containsParameter(TIME_AFTER_USERS_ARE_REMOVED)) {
            configuration.setParameter(TIME_AFTER_USERS_ARE_REMOVED,
                    120 * AgentProtocol.MINUTE);
        }
        if (!configuration.containsParameter(MINIMUM_NUMBER_OF_CONCEPTS)) {
            configuration.setParameter(MINIMUM_NUMBER_OF_CONCEPTS, 5);
        }
    }

    private void registerToolStartedListener() {
        try {
            this.listenerId = this.getActionSpace().eventRegister(
                    Command.WRITE, this.getActionTupleTemplate(), this, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStop() {
        try {
            this.getActionSpace().eventDeRegister(this.listenerId);
            this.listenerId = -1;
        } catch (TupleSpaceException e) {
            LOGGER.fatal(
                    "Could not deregister tuple space listener: "
                            + e.getMessage(), e);
        }
    }

    private Tuple getActionTupleTemplate() {
        return new Tuple(ActionConstants.ACTION, String.class, Long.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, Field.createWildCardField());
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple,
                     Tuple beforeTuple) {
        if (this.listenerId != seq) {
            LOGGER.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        } else {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(afterTuple);
            if (ActionConstants.ACTION_TOOL_STARTED.equals(action.getType())) {
                this.handleToolStarted(action);
            } else if (ActionConstants.ACTION_TOOL_OPENED.equals(action
                    .getType())) {
                this.handleToolStarted(action);
            } else if (ActionConstants.ACTION_NODE_ADDED.equals(action
                    .getType())) {
                this.handleNodeAdded(action);
            } else if (ActionConstants.ACTION_NODE_REMOVED.equals(action
                    .getType())) {
                this.handleNodeRemoved(action);
            } else if (ActionConstants.ACTION_TOOL_CLOSED.equals(action
                    .getType())) {
                this.handleToolStopped(action);
            } else if (ActionConstants.ACTION_ELO_LOADED.equals(action
                    .getType())) {
                this.handleELOLoaded(action);
            }
        }
    }

    private void handleELOLoaded(IAction action) {
        if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
            ContextInformation contextInfo = this.getContextInformation(action);
            contextInfo.lastAction = action.getTimeInMillis();
            String eloUri = action.getContext(ContextConstants.eloURI);
            if (eloUri.startsWith(UNSAVED_ELO)) {
                LOGGER.warn("eloUri not present");
                contextInfo.webresourcerELO = null;
            } else {
                try {
                    contextInfo.webresourcerELO = new URI(eloUri);
                    contextInfo.webresourcerStarted = true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        if (SCYMAPPER.equals(action.getContext(ContextConstants.tool))) {
            ContextInformation contextInfo = this.getContextInformation(action);
            contextInfo.lastAction = action.getTimeInMillis();
            String eloUri = action.getContext(ContextConstants.eloURI);
            if (eloUri.startsWith(UNSAVED_ELO)) {
                LOGGER.warn("eloUri not present");
                contextInfo.scyMapperElo = null;
            } else {
                try {
                    contextInfo.scyMapperElo = new URI(eloUri);
                    contextInfo.scyMapperStarted = true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleNodeRemoved(IAction action) {
        LOGGER.debug("node_removed");
        ContextInformation contextInfo = this.getContextInformation(action);
        contextInfo.numberOfConcepts--;
    }

    private void handleNodeAdded(IAction action) {
        LOGGER.debug("node_added");
        ContextInformation contextInfo = this.getContextInformation(action);
        contextInfo.numberOfConcepts++;
        contextInfo.lastAction = action.getTimeInMillis();
    }

    private void handleToolStopped(IAction action) {
        ContextInformation contextInfo = this.getContextInformation(action);
        if (CONCEPTMAP.equals(action.getContext(ContextConstants.tool))) {
            LOGGER.debug(CONCEPTMAP + " stopped");
            contextInfo.scyMapperStarted = false;
        }
        if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
            LOGGER.debug(WEBRESOURCER + " stopped");
            contextInfo.webresourcerStarted = false;
        }
        contextInfo.lastAction = action.getTimeInMillis();
    }

    private void handleToolStarted(IAction action) {
        ContextInformation contextInfo = this.getContextInformation(action);
        if (CONCEPTMAP.equals(action.getContext(ContextConstants.tool))) {
            LOGGER.debug(CONCEPTMAP + " started by " + action.getUser()
                    + ". Recognized by ExtractKeywordsDecisionAgent");
            contextInfo.scyMapperStarted = true;
            try {
                contextInfo.scyMapperElo = new URI(
                        action.getContext(ContextConstants.eloURI));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (WEBRESOURCER.equals(action.getContext(ContextConstants.tool))) {
            LOGGER.debug(WEBRESOURCER + " started  by " + action.getUser()
                    + ". Recognized by ExtractKeywordsDecisionAgent");
            try {
                contextInfo.webresourcerELO = new URI(
                        action.getContext(ContextConstants.eloURI));
                contextInfo.webresourcerStarted = true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        contextInfo.lastAction = action.getTimeInMillis();
    }

    private synchronized ContextInformation getContextInformation(IAction action) {
        ContextInformation result = this.user2Context.get(action.getUser());
        if (result == null) {
            result = new ContextInformation();
            result.user = action.getUser();
            result.mission = action.getContext(ContextConstants.mission);
            result.session = action.getContext(ContextConstants.session);
            result.lastAction = action.getTimeInMillis();
            result.lastNotification = action.getTimeInMillis();
            this.user2Context.put(result.user, result);
        }
        return result;
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException {
        try {
            HashSet<String> toRemove = new HashSet<String>();
            while (this.status == Status.Running) {
                toRemove.clear();
                long currentTime = System.currentTimeMillis();
                HashSet<String> users = new HashSet<String>(
                        this.user2Context.keySet());
                for (String user : users) {
                    ContextInformation contextInformation = this.user2Context
                            .get(user);
                    if (this.userNeedsToBeNotified(currentTime,
                            contextInformation)) {
                        this.notifyUser(currentTime, user, contextInformation);
                    }
                    if (this.userIsIdleForTooLongTime(currentTime,
                            contextInformation)) {
                        toRemove.add(user);
                    }
                }
                for (String user : toRemove) {
                    this.user2Context.remove(user);
                }
                this.sendAliveUpdate();
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
            }
        } catch (Exception e) {
            LOGGER.fatal("*************** " + e.getMessage(), e);
            e.printStackTrace();
            throw new AgentLifecycleException(e.getMessage(), e);
        }
        if (this.status != Status.Stopping) {
            LOGGER.warn("************** Agent not stopped but run loop terminated *****************1");
        }
    }

    private boolean userIsIdleForTooLongTime(long currentTime,
                                             ContextInformation contextInformation) {
        long timeSinceLastAction = currentTime - contextInformation.lastAction;
        Integer timeAfterThatItIsSaveToRemoveUser = configuration.getParameter(
                contextInformation.mission, contextInformation.user,
                TIME_AFTER_USERS_ARE_REMOVED);
        return timeSinceLastAction > timeAfterThatItIsSaveToRemoveUser;
    }

    private void notifyUser(final long currentTime, final String user,
                            final ContextInformation contextInformation) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                LOGGER.debug("Notifiying " + user);
                IELO elo = getELO(contextInformation);
                if (elo == null) {
                    LOGGER.fatal("ELO " + contextInformation.webresourcerELO
                            + " was null");
                    return;
                }

                Mission mission = getSession().getMission(user);
                String language = getSession().getLanguage(user);

                extractor.setMission(mission);
                extractor.setLanguage(language);
                List<String> keywords = extractor.getKeywords(elo);

                LOGGER.debug("found keywords to send to " + user + ": "
                        + keywords);
                ExtractKeywordsDecisionMakerAgent.this.sendNotification(
                        contextInformation, keywords);
                contextInformation.lastNotification = currentTime;
            }

            private IELO getELO(final ContextInformation contextInformation) {
                if (ExtractKeywordsDecisionMakerAgent.this.repository == null) {
                    LOGGER.fatal("repository is null");
                    return null;
                }
                IELO elo = ExtractKeywordsDecisionMakerAgent.this.repository.retrieveELO(contextInformation.webresourcerELO);
                return elo;
            }
        }, "NotifyUser").start();

    }

    private boolean userNeedsToBeNotified(long currentTime,
                                          ContextInformation contextInformation) {
        boolean userNeedsToBeNotified = contextInformation.webresourcerStarted;
        userNeedsToBeNotified &= contextInformation.scyMapperStarted;
        long timeSinceLastAction = currentTime
                - contextInformation.lastNotification;

        Integer idleTimeInMS = configuration.getParameter(
                contextInformation.mission, contextInformation.user,
                IDLE_TIME_INMS);
        Integer minimumNumberOfConcepts = configuration.getParameter(
                contextInformation.mission, contextInformation.user,
                MINIMUM_NUMBER_OF_CONCEPTS);

        LOGGER.debug(timeSinceLastAction + " : " + idleTimeInMS);
        userNeedsToBeNotified &= timeSinceLastAction > idleTimeInMS;
        userNeedsToBeNotified &= contextInformation.numberOfConcepts < minimumNumberOfConcepts;
        userNeedsToBeNotified &= contextInformation.webresourcerELO != null;
        return userNeedsToBeNotified;
    }

    private void sendNotification(ContextInformation contextInformation,
                                  List<String> keywords) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(contextInformation.user);
        notificationTuple.add(contextInformation.scyMapperElo.toString());
        notificationTuple.add(NAME);
        notificationTuple.add(contextInformation.mission);
        notificationTuple.add(contextInformation.session);

        notificationTuple.add("type=concept_proposal");
        for (String keyword : keywords) {
            notificationTuple.add("keyword=" + keyword);
        }
        try {
            if (this.getCommandSpace().isConnected()) {
                this.getCommandSpace().write(notificationTuple);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return new Tuple(AgentProtocol.RESPONSE, queryId, getId(), getName(),
                AgentProtocol.MESSAGE_IDENTIFY, IDLE_TIME_INMS,
                MINIMUM_NUMBER_OF_CONCEPTS, TIME_AFTER_USERS_ARE_REMOVED);
    }

    @Override
    protected Tuple getListParameterTuple(String queryId) {
        return AgentProtocol.getListParametersTupleResponse(getName(), queryId,
                Arrays.asList(IDLE_TIME_INMS, MINIMUM_NUMBER_OF_CONCEPTS,
                        TIME_AFTER_USERS_ARE_REMOVED));
    }

    @Override
    public boolean isStopped() {
        return (this.listenerId == -1) && (this.status == Status.Stopping);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        // not needed
    }

    @Override
    public void setRepository(IRepository rep) {
        LOGGER.debug("Setting repository ");
        this.repository = rep;
    }
}
