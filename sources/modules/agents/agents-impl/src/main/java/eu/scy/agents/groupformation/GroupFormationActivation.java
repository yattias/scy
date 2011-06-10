package eu.scy.agents.groupformation;

import eu.scy.common.mission.StrategyType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fschulz
 * Date: 09.06.11
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class GroupFormationActivation {

    private static class GroupFormationInfo {

        private StrategyType strategy;
        private int maxUsers;
        private int minUsers;
        private URI referenceElo;

        public void setStrategy(StrategyType strategy) {
            this.strategy = strategy;
        }

        public void setMaximumUsers(int maxUsers) {
            this.maxUsers = maxUsers;
        }

        public void setMinimumUsers(int minUsers) {
            this.minUsers = minUsers;
        }

        public void setReferenceElo(URI referenceElo) {
            this.referenceElo = referenceElo;
        }

        @Override
        public String toString() {
            return "GroupFormationInfo{" +
                    "strategy=" + strategy +
                    ", maxUsers=" + maxUsers +
                    ", minUsers=" + minUsers +
                    ", referenceElo=" + referenceElo +
                    '}';
        }
    }

    private Map<String, GroupFormationInfo> groupFormationInfoMap;

    public GroupFormationActivation() {
        groupFormationInfoMap = new HashMap<String, GroupFormationInfo>();
    }

    public void addStrategy(String las, StrategyType strategy) {
        GroupFormationInfo groupFormationInfo = getGroupFormationInfo(las);
        groupFormationInfo.setStrategy(strategy);
    }

    private GroupFormationInfo getGroupFormationInfo(String las) {
        GroupFormationInfo groupFormationInfo = groupFormationInfoMap.get(las);
        if (groupFormationInfo == null) {
            groupFormationInfo = new GroupFormationInfo();
            groupFormationInfoMap.put(las, groupFormationInfo);
        }
        return groupFormationInfo;
    }

    public void addMaximumUsers(String las, int maxUsers) {
        GroupFormationInfo groupFormationInfo = getGroupFormationInfo(las);
        groupFormationInfo.setMaximumUsers(maxUsers);
    }

    public void addMinimumUsers(String las, int minUsers) {
        GroupFormationInfo groupFormationInfo = getGroupFormationInfo(las);
        groupFormationInfo.setMinimumUsers(minUsers);
    }

    public void addReferenceElo(String las, URI referenceElo) {
        GroupFormationInfo groupFormationInfo = getGroupFormationInfo(las);
        groupFormationInfo.setReferenceElo(referenceElo);
    }

    @Override
    public String toString() {
        return "GroupFormationActivation{" +
                "groupFormationInfoMap=" + groupFormationInfoMap +
                '}';
    }
}
