package eu.scy.agents.groupformation;

import eu.scy.actionlogging.ActionTupleTransformer;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    private static final String MIN_GROUP_SIZE_PARAMETER = "MinGroupSize";
    private static final String MAX_GROUP_SIZE_PARAMETER = "MaxGroupSize";
    private static final double NEEDED_PERCENT_OF_PRESENT_USERS = 2.0 / 3.0;

    private int listenerId;
    private AgentRooloServiceImpl rooloServices;
    private GroupFormationStrategyFactory factory;

    private GroupFormationNotificationHelper notificationHelper;

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

        notificationHelper = new GroupFormationNotificationHelper(getCommandSpace(), getActionSpace(), NAME);

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
            IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
            String missionUri = action.getContext(ContextConstants.mission);
            String type = action.getType();

            // las was changed groupformation could become active
            if ( type.equals(ActionConstants.ACTION_LAS_CHANGED) ) {
                String las = action.getAttribute(LAS);
                GroupFormationActivation groupFormationActivation = getGroupFormationActivation(URI.create(missionUri), action.getUser());

                if ( groupFormationActivation.shouldActivate(las) ) {
                    // groupformation should be triggered for this las
                    try {
                        runGroupFormation(action, groupFormationActivation.getGroupFormationInfo(las));
                    } catch ( TupleSpaceException e ) {
                        LOGGER.warn("", e);
                    }
                } else {
                    // user leaves a potential las that contained groupformation set status to something else and disable filtering.
                    notificationHelper.setStatus(action, las, action.getUser());
                    notificationHelper.enableFiltering(action, "", action.getUser(), false);
                }
            }
            if ( type.equals(ActionConstants.ACTION_LOG_OUT) ) {
                // user quits scy-lab set status to something else and disable filtering and remove user from the group cache.
                notificationHelper.setStatus(action, "", action.getUser());
                notificationHelper.enableFiltering(action, "", action.getUser(), false);
                missionGroupsCache.removeUser(action.getUser());
            }
        }
    }

    private GroupFormationActivation getGroupFormationActivation(URI missionUri, String user) {
        Mission mission = getSession().getMission(user);
        String missionSpecification = getSession().getMissionSpecification(missionUri.toString());

        GroupFormationActivation groupformationActivation = missionSpecsMap
                .get(mission);
        if ( groupformationActivation == null ) {
            groupformationActivation = readGroupFormationActivation(URI.create(missionSpecification));
        }
        return groupformationActivation;
    }

    private GroupFormationActivation readGroupFormationActivation(URI missionUri) {
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
        Set<String> availableUsers = getAvailableUsersNotInGroups(mission, las);
        double numberOfUsersInMission = getSession().getUsersInMissionFromName(mission.getName()).size();

        if ( missionGroupsCache.contains(mission, las, user) ) {
            handleUserWasAlreadyAssigned(action, user, mission, las, language);
        } else {
            Collection<Group> groups = missionGroupsCache.getGroups(mission, las);
            if ( groups.isEmpty() ) {
                formNewGroupIfPossible(action, groupFormationInfo, mission, las, language, referenceElo, availableUsers,
                        numberOfUsersInMission);

            } else {
                putIntoExistingGroup(action, groupFormationInfo, user, mission, las, language, referenceElo, availableUsers);
            }
        }
    }

    private void formNewGroupIfPossible(IAction action, GroupFormationActivation.GroupFormationInfo groupFormationInfo, Mission mission,
                                        String las, String language, IELO referenceElo, Set<String> availableUsers,
                                        double numberOfUsersInMission) {
        // no groups formed yet
        double fractionOfPresentUsers = (double) availableUsers.size() / numberOfUsersInMission;
        if ( ( fractionOfPresentUsers < NEEDED_PERCENT_OF_PRESENT_USERS )
                || ( availableUsers.size() < groupFormationInfo.getMinimumUsers() ) ) {
            // to few user available to assign to a group  -> wait
            notificationHelper.sendWaitNotification(action, language);
        } else {
            // enough users available assign a new group
            GroupFormationStrategy groupFormationStrategy = factory.getStrategy(groupFormationInfo.getStrategy());
            groupFormationStrategy.setGroupFormationCache(missionGroupsCache.get(mission, las));
            groupFormationStrategy.setLas(las);
            groupFormationStrategy.setMission(mission.getName());
            groupFormationStrategy.setMinimumGroupSize(groupFormationInfo.getMinimumUsers());
            groupFormationStrategy.setMaximumGroupSize(groupFormationInfo.getMaximumUsers());
            groupFormationStrategy.setAvailableUsers(availableUsers);
            groupFormationStrategy.setRooloServices(rooloServices);

            Collection<Group> formedGroups = groupFormationStrategy.formGroup(referenceElo);
            missionGroupsCache.addGroups(mission, las, formedGroups);
            synchronized ( lock ) {
                notificationHelper.sendGroupNotification(action, formedGroups, language, las);
            }
        }
    }

    /*
     * already formed groups -> put into existing group
     */
    private void putIntoExistingGroup(IAction action, GroupFormationActivation.GroupFormationInfo groupFormationInfo, String user,
                                      Mission mission, String las, String language, IELO referenceElo, Set<String> availableUsers) {
        GroupFormationStrategy groupFormationStrategy = factory.getStrategy(groupFormationInfo.getStrategy());
        groupFormationStrategy.setGroupFormationCache(missionGroupsCache.get(mission, las));
        groupFormationStrategy.setLas(las);
        groupFormationStrategy.setMission(mission.getName());
        groupFormationStrategy.setMinimumGroupSize(groupFormationInfo.getMinimumUsers());
        groupFormationStrategy.setMaximumGroupSize(groupFormationInfo.getMaximumUsers());
        groupFormationStrategy.setAvailableUsers(availableUsers);
        groupFormationStrategy.setRooloServices(rooloServices);

        Collection<Group> newGroups = groupFormationStrategy.assignToExistingGroups(user, referenceElo);
        missionGroupsCache.addGroups(mission, las, newGroups);

        synchronized ( lock ) {
            notificationHelper.sendStudentAddedToGroupNotification(action, user, newGroups, language);
        }
    }

    /*
     * user was already assigned to a group in this las
     */
    private void handleUserWasAlreadyAssigned(IAction action, String user, Mission mission, String las,
                                              String language) {

        Group group = missionGroupsCache.getGroup(mission, las, user);
        Set<String> availableUsers = getAvailableUsersInLas(mission, las, user);
        if ( availableUsers.containsAll(group.asSet()) ) {
            // all users present -> send form group notification
            Set<Group> groups = new HashSet<Group>();
            groups.add(group);
            notificationHelper.sendGroupNotification(action, groups, language, las);
        } else {
            // not all present -> send "wait for group" notification
            notificationHelper.sendWaitForExistingGroupNotification(action, language, group);
        }
    }


    private Set<String> getAvailableUsersNotInGroups(Mission mission, String las) {
        Set<String> availableUsers = getSession().getUsersInLas(mission.getName(), las);
        Collection<Group> groups = missionGroupsCache.getGroups(mission, las);
        for ( Group group : groups ) {
            availableUsers.removeAll(group.asSet());
        }
        return availableUsers;
    }

    private Set<String> getAvailableUsersInLas(Mission mission, String las, String thisUser) {
        Set<String> availableUsers = getSession().getUsersInLas(mission.getName(), las);
        return availableUsers;
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
