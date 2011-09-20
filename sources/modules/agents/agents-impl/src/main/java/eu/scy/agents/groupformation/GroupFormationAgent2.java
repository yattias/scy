package eu.scy.agents.groupformation;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.MissionGroupCache;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import eu.scy.common.mission.GroupformationStrategyType;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsEloContent;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class GroupFormationAgent2 extends AbstractRequestAgent implements IRepositoryAgent {

    static final String NAME = GroupFormationAgent2.class.getName();

    private static final Logger LOGGER = Logger
            .getLogger(GroupFormationAgent2.class);

    private static final String GROUPFORMATION_SETTING = "groupformation";
    private static final String GROUPFORMATION_STRATEGY_SETTING = "groupformation.strategy";
    private static final String GROUPFORMATION_REFERENCEELO_SETTING = "groupformation.referenceElo";
    private static final String GROUPFORMATION_MINUSER_SETTING = "groupformation.minUsers";
    private static final String GROUPFORMATION_MAXUSER_SETTING = "groupformation.maxUsers";

    private static final String LAS = "newLasId";
    private static final String OLD_LAS = "oldLasId";
    private static final String FORM_GROUP = "form_group";

    private static final String MIN_GROUP_SIZE_PARAMETER = "MinGroupSize";
    private static final String MAX_GROUP_SIZE_PARAMETER = "MaxGroupSize";
    private static final double NEEDED_PERCENT_OF_PRESENT_USERS = 2.0 / 3.0;

    private int listenerId;
    private AgentRooloServiceImpl rooloServices;
    private GroupFormationStrategyFactory factory;

    private final Object lock;

    private MissionGroupCache missionGroupsCache;
    private Map<Mission, GroupFormationActivation> missionSpecsMap;

    public GroupFormationAgent2(Map<String, Object> params) {
        super(NAME, params);
        lock = new Object();
        if ( params.containsKey(AgentProtocol.TS_HOST) ) {
            host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if ( params.containsKey(AgentProtocol.TS_PORT) ) {
            port = (Integer) params.get(AgentProtocol.TS_PORT);
        }
        configuration.addAllParameter(params);
        factory = new GroupFormationStrategyFactory();

        try {
            listenerId = getActionSpace().eventRegister(Command.WRITE,
                    getActivationTuple(), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }

        missionGroupsCache = new MissionGroupCache();
        rooloServices = new AgentRooloServiceImpl();
        missionSpecsMap = new HashMap<Mission, GroupFormationActivation>();
    }

    private Tuple getActivationTuple() {
        return new Tuple(ActionConstants.ACTION, String.class, Long.class, String.class, Field.createWildCardField());
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
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
    public void call(Command command, int seq, Tuple afterTuple,
                     Tuple beforeTuple) {
        if ( listenerId != seq ) {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        } else {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(afterTuple);
            String missionUri = action.getContext(ContextConstants.mission);
            String type = action.getType();
            if ( type.equals(ActionConstants.ACTION_LAS_CHANGED) ) {
                String las = action.getAttribute(LAS);
                GroupFormationActivation groupFormationActivation = getGroupFormationActivation(
                        URI.create(missionUri), action.getUser());
                if ( groupFormationActivation.shouldActivate(las) ) {
                    try {
                        runGroupFormation(action, groupFormationActivation.getGroupFormationInfo(las));
                    } catch ( TupleSpaceException e ) {
                        LOGGER.warn("", e);
                    }
                } else {
                    // TODO implement: think about different cases
                }
            }
            if ( type.equals(ActionConstants.ACTION_LOG_OUT) ) {
                missionGroupsCache.removeUser(action.getUser());
            }
        }
    }

    private GroupFormationActivation getGroupFormationActivation(
            URI missionUri, String user) {
        Mission mission = getSession().getMission(user);
        String missionSpecification = getSession().getMissionSpecification(missionUri.toString());

        GroupFormationActivation groupformationActivation = missionSpecsMap
                .get(mission);
        if ( groupformationActivation == null ) {
            groupformationActivation = readGroupFormationActivation(URI.create(missionSpecification),
                    mission);
        }
        return groupformationActivation;
    }

    private GroupFormationActivation readGroupFormationActivation(
            URI missionUri, Mission mission) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo
                .loadElo(missionUri, rooloServices);
        URI runtimeSettingsEloUri = missionSpecificationElo.getTypedContent()
                .getRuntimeSettingsEloUri();
        RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo.loadElo(
                runtimeSettingsEloUri, rooloServices);
        RuntimeSettingsEloContent runtimeSettingsEloContent = runtimeSettingsElo
                .getTypedContent();

        GroupFormationActivation activation = new GroupFormationActivation();

        List<RuntimeSetting> settings = runtimeSettingsEloContent
                .getAllSettings();
        for ( RuntimeSetting setting : settings ) {
            RuntimeSettingKey key = setting.getKey();
            if ( key.getName().equals(GROUPFORMATION_STRATEGY_SETTING) ) {
                String las = key.getLasId();
                GroupformationStrategyType strategy = GroupformationStrategyType
                        .valueOf(setting.getValue());
                activation.addStrategy(las, strategy);
            }
            if ( key.getName().equals(GROUPFORMATION_MAXUSER_SETTING) ) {
                String las = key.getLasId();
                int maxUsers = Integer.parseInt(setting.getValue());
                activation.addMaximumUsers(las, maxUsers);
            }
            if ( key.getName().equals(GROUPFORMATION_MINUSER_SETTING) ) {
                String las = key.getLasId();
                int minUsers = Integer.parseInt(setting.getValue());
                activation.addMinimumUsers(las, minUsers);
            }
            if ( key.getName().equals(GROUPFORMATION_REFERENCEELO_SETTING) ) {
                String las = key.getLasId();
                URI referenceElo = URI.create(setting.getValue());
                activation.addReferenceElo(las, referenceElo);
            }
        }
        return activation;
    }

    private void runGroupFormation(IAction action, GroupFormationActivation.GroupFormationInfo groupFormationInfo) throws
            TupleSpaceException {
        String user = action.getUser();
        Mission mission = getSession().getMission(user);
        String las = action.getAttribute(LAS);
        String language = getSession().getLanguage(user);
        IELO referenceElo = rooloServices.getRepository().retrieveELO(groupFormationInfo.getReferenceElo());
        Set<String> availableUsers = getAvailableUsersNotInGroups(mission, las, user);
        double numberOfUsersInMission = getSession().getUsersInMissionFromName(mission.getName()).size();

        if ( missionGroupsCache.contains(mission, las, user) ) {
            // user was already assigned to a group in this las
            Group group = missionGroupsCache.getGroup(mission, las, user);
            availableUsers = getAvailableUsersInLas(mission, las, user);
            if ( availableUsers.containsAll(group.asSet()) ) {
                // all users present -> send form group notification
                Set<Group> groups = new HashSet<Group>();
                groups.add(group);
                sendGroupNotification(action, groups, language);
            } else {
                // not all present -> send "wait for group" notification
                sendWaitForExistingGroupNotification(action, language, group);
            }
        } else {
            // not assigned -> get already existing groups
            Collection<Group> groups = missionGroupsCache.getGroups(mission, las);

            if ( groups.isEmpty() ) {
                // no groups formed yet
                double fractionOfPresentUsers = (double) availableUsers.size() / numberOfUsersInMission;
                if ( ( fractionOfPresentUsers < NEEDED_PERCENT_OF_PRESENT_USERS )
                        || ( availableUsers.size() < groupFormationInfo.getMinimumUsers() ) ) {
                    // to few user available to assign to a group  -> wait
                    sendWaitNotification(action, language);
                } else {
                    // enough users available assign a new group
                    GroupFormationStrategy groupFormationStrategy = factory.getStrategy(groupFormationInfo.getStrategy());
                    groupFormationStrategy.setGroupFormationCache(missionGroupsCache.get(mission, las));
                    groupFormationStrategy.setLas(las);
                    groupFormationStrategy.setMission(mission.getName());
                    groupFormationStrategy.setMinimumGroupSize(groupFormationInfo.getMinimumUsers());
                    groupFormationStrategy.setMaximumGroupSize(groupFormationInfo.getMaximumUsers());
                    groupFormationStrategy.setAvailableUsers(availableUsers);
                    groupFormationStrategy.setRepository(rooloServices.getRepository());

                    Collection<Group> formedGroups = groupFormationStrategy.formGroup(referenceElo);
                    missionGroupsCache.addGroups(mission, las, formedGroups);
                    try {
                        synchronized ( lock ) {
                            sendGroupNotification(action, formedGroups, language);
                        }
                    } catch ( TupleSpaceException e ) {
                        LOGGER.error("Could not write into Tuplespace", e);
                    }
                }
            } else {
                // already formed groups -> put into existing group
                GroupFormationStrategy groupFormationStrategy = factory.getStrategy(groupFormationInfo.getStrategy());
                groupFormationStrategy.setGroupFormationCache(missionGroupsCache.get(mission, las));
                groupFormationStrategy.setLas(las);
                groupFormationStrategy.setMission(mission.getName());
                groupFormationStrategy.setMinimumGroupSize(groupFormationInfo.getMinimumUsers());
                groupFormationStrategy.setMaximumGroupSize(groupFormationInfo.getMaximumUsers());
                groupFormationStrategy.setAvailableUsers(availableUsers);
                groupFormationStrategy.setRepository(rooloServices.getRepository());
                Collection<Group> newGroups = groupFormationStrategy.assignToExistingGroups(user, referenceElo);
                missionGroupsCache.addGroups(mission, las, newGroups);
                try {
                    synchronized ( lock ) {
                        sendStudentAddedToGroupNotification(action, user, newGroups, language);
                    }
                } catch ( TupleSpaceException e ) {
                    LOGGER.error("Could not write into Tuplespace", e);
                }
            }
        }
    }

    private void sendStudentAddedToGroupNotification(IAction action, String newUser, Collection<Group> newGroups,
                                                     String language) throws TupleSpaceException {

        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));

        Group newGroup = null;
        // buddify group toNewUser
        for ( Group group : newGroups ) {
            if ( !group.contains(newUser) ) {
                continue;
            } else {
                newGroup = group;
                for ( String user : group ) {
                    if ( !user.equals(newUser) ) {
                        // buddify newUser to group
                        String id = createId();
                        Tuple buddifyNotification = createBuddifyNotificationTuple(action, id, user, newUser);
                        getCommandSpace().write(buddifyNotification);

                        // inform other users that somebody entered their group
                        StringBuilder addUserToGroupMessage = new StringBuilder();
                        addUserToGroupMessage.append(messages.getString("GF_ADD_USER_TO_GROUP"));
                        addUserToGroupMessage.append(" ");
                        addUserToGroupMessage.append(newUser);

                        createMessageNotificationTuple(action, id, addUserToGroupMessage.toString(), user);

                        waitForNotificationProcessedAction(id, "Buddify " + user + " -> " + newUser + " notification was not processed");
                    }
                }
            }
        }
        // send special notification to newUser
        if ( newGroup != null ) {
            String userListString = createUserListString(newUser, newGroup);
            StringBuilder addUserToGroupMessage = new StringBuilder();
            addUserToGroupMessage.append(messages.getString("GF_USER_ADDED_TO_GROUP"));
            addUserToGroupMessage.append("\n");
            addUserToGroupMessage.append(userListString);
            createMessageNotificationTuple(action, createId(), addUserToGroupMessage.toString(), newUser);
        } else {
            LOGGER.error("New user " + newUser + " not added to any group");
        }
    }

    private void sendWaitForExistingGroupNotification(IAction action, String language, Group group) {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));
        StringBuilder message = new StringBuilder();
        message.append(messages.getString("GF_WAIT_GROUP_MESSAGE"));
        message.append("\n");
        message.append(createUserListString(action.getUser(), group));
        Tuple notificationTuple = createMessageNotificationTuple(action, createId(), message.toString(), action.getUser());
        try {
            getCommandSpace().write(notificationTuple);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    private Set<String> getAvailableUsersNotInGroups(Mission mission, String las, String thisUser) throws TupleSpaceException {
        Set<String> availableUsers = getSession().getUsersInLas(mission.getName(), las);
        Collection<Group> groups = missionGroupsCache.getGroups(mission, las);
        for ( Group group : groups ) {
            availableUsers.removeAll(group.asSet());
        }
        return availableUsers;
    }

    private Set<String> getAvailableUsersInLas(Mission mission, String las, String thisUser) throws TupleSpaceException {
        Set<String> availableUsers = getSession().getUsersInLas(mission.getName(), las);
        return availableUsers;
    }

    private void sendGroupNotification(IAction action, Collection<Group> formedGroups, String language) throws TupleSpaceException {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));
        for ( Group group : formedGroups ) {
            for ( String user : group ) {
                String notificationId = createId();
                Tuple removeAllBuddiesTuple = createRemoveAllBuddiesNotification(
                        action, notificationId, user);
                getCommandSpace().write(removeAllBuddiesTuple);

                waitForNotificationProcessedAction(notificationId, "remove all buddies for " + user + " notification " + "was not " +
                        "processed");
            }

            for ( String user : group ) {
                StringBuilder message = new StringBuilder();
                message.append(messages.getString("GF_COLLABORATE"));
                message.append("\n");

                String userListString = createUserListString(user, new Group(group));
                message.append(userListString);

                String messageNotificationId = createId();
                Tuple messageNotificationTuple = createMessageNotificationTuple(
                        action, messageNotificationId, message.toString(), user);
                logGroupFormation(action, userListString, user);

                for ( String userToBuddify : group ) {
                    if ( !user.equals(userToBuddify) ) {
                        String buddifyNotificationId = messageNotificationId;
                        Tuple buddifyNotification = createBuddifyNotificationTuple(action, buddifyNotificationId, user, userToBuddify);
                        getCommandSpace().write(buddifyNotification);

                        waitForNotificationProcessedAction(buddifyNotificationId, "Buddify " + user + " -> " + userToBuddify + " " +
                                "notification was not processed");
                    }
                }
                getCommandSpace().write(messageNotificationTuple);
                waitForNotificationProcessedAction(messageNotificationId, "Message about group notification was not processed");
            }
        }
    }

    private void waitForNotificationProcessedAction(String notificationId,
                                                    String message) throws TupleSpaceException {
        Tuple notificationProcessedTuple = getActionSpace().waitToRead(
                new Tuple(ActionConstants.ACTION, notificationId, Long.class,
                        String.class, Field.createWildCardField()),
                AgentProtocol.MILLI_SECOND * 10);
        if ( notificationProcessedTuple == null ) {
            logger.warn(message);
        }
    }

    private String createId() {
        return new VMID().toString();
    }

    private Tuple createRemoveAllBuddiesNotification(IAction action, String notificationId, String user) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(NAME);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("type=remove_all_buddies");
        notificationTuple.add("user=" + user);
        return notificationTuple;
    }

    private Tuple createBuddifyNotificationTuple(IAction action, String notificationId, String user, String userToBuddify) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(NAME);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("type=add_buddy");
        notificationTuple.add("user=" + userToBuddify);
        return notificationTuple;
    }

    private void logGroupFormation(IAction action, String userListString, String user) {
        Action groupFormationAction = new Action();
        groupFormationAction.setContext(new Context(NAME, action
                .getContext(ContextConstants.mission), action
                .getContext(ContextConstants.session), action
                .getContext(ContextConstants.eloURI)));
        groupFormationAction.setId(createId());
        groupFormationAction.setTimeInMillis(System.currentTimeMillis());
        groupFormationAction.setUser(action.getUser());
        groupFormationAction.setType(FORM_GROUP);
        groupFormationAction.addAttribute("user", user);
        groupFormationAction.addAttribute("group", userListString);
        Tuple actionAsTuple = ActionTupleTransformer
                .getActionAsTuple(groupFormationAction);
        try {
            getActionSpace().write(actionAsTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.error("Could not write action into Tuplespace", e);
        }
    }

    private Tuple createMessageNotificationTuple(IAction action,
                                                 String notificationId, String message, String user) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(NAME);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("text=" + message.toString());
        notificationTuple.add("title=Groupformation");
        notificationTuple.add("type=message_dialog_show");
        notificationTuple.add("modal=false");
        notificationTuple.add("dialogType=OK_DIALOG");
        return notificationTuple;
    }

    String createUserListString(String userToNotify, Group group) {
        StringBuilder message = new StringBuilder();
        int i = 0;
        for ( String user : group ) {
            if ( user.equals(userToNotify) ) {
                i++;
                continue;
            }
            message.append(sanitizeName(user));
            if ( i != group.size() - 1 ) {
                message.append("; ");
            }
            i++;
        }
        return message.toString();
    }

    private String sanitizeName(String user) {
        int indexOf = user.indexOf("@");
        if ( indexOf != -1 ) {
            return user.substring(0, indexOf);
        }
        return user;
    }

    private IELO getElo(String eloUri) {
        try {
            return rooloServices.getRepository().retrieveELO(new URI(eloUri));
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendWaitNotification(IAction action, String language) {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));
        Tuple notificationTuple = createMessageNotificationTuple(action, createId(),
                messages.getString("GF_WAIT_MESSAGE"), action.getUser());
        try {
            getCommandSpace().write(notificationTuple);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        rooloServices.setMetadataTypeManager(manager);
    }

    @Override
    public void setRepository(IRepository rep) {
        rooloServices.setRepository(rep);
    }

    public GroupFormationStrategyFactory getFactory() {
        return factory;
    }

    public void setFactory(GroupFormationStrategyFactory factory) {
        this.factory = factory;
    }
}
