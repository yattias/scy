package eu.scy.agents.groupformation;

import eu.scy.common.mission.GroupformationStrategyType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: fschulz Date: 09.06.11 Time: 15:51 To change this template use File | Settings |
 * File
 * Templates.
 */
public class GroupFormationActivation {


    public static class GroupFormationInfo {

        private GroupformationStrategyType strategy;
        private int maxUsers;
        private int minUsers;
        private URI referenceElo;

        public void setStrategy(GroupformationStrategyType strategy) {
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

        public int getMaximumUsers() {
            return maxUsers;
        }

        public int getMinimumUsers() {
            return minUsers;
        }

        public URI getReferenceElo() {
            return referenceElo;
        }

        public GroupformationStrategyType getStrategy() {
            return strategy;
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

    public void addStrategy(String las, GroupformationStrategyType strategy) {
        GroupFormationInfo groupFormationInfo = getOrAddGroupFormationInfo(las);
        groupFormationInfo.setStrategy(strategy);
    }

    private GroupFormationInfo getOrAddGroupFormationInfo(String las) {
        GroupFormationInfo groupFormationInfo = groupFormationInfoMap.get(las);
        if (groupFormationInfo == null) {
            groupFormationInfo = new GroupFormationInfo();
            groupFormationInfoMap.put(las, groupFormationInfo);
        }
        return groupFormationInfo;
    }

    public GroupFormationInfo getGroupFormationInfo(String las) {
        GroupFormationInfo groupFormationInfo = groupFormationInfoMap.get(las);
        return groupFormationInfo;
    }

    public void addMaximumUsers(String las, int maxUsers) {
        GroupFormationInfo groupFormationInfo = getOrAddGroupFormationInfo(las);
        groupFormationInfo.setMaximumUsers(maxUsers);
    }

    public void addMinimumUsers(String las, int minUsers) {
        GroupFormationInfo groupFormationInfo = getOrAddGroupFormationInfo(las);
        groupFormationInfo.setMinimumUsers(minUsers);
    }

    public void addReferenceElo(String las, URI referenceElo) {
        GroupFormationInfo groupFormationInfo = getOrAddGroupFormationInfo(las);
        groupFormationInfo.setReferenceElo(referenceElo);
    }

    @Override
    public String toString() {
        return "GroupFormationActivation{" +
                "groupFormationInfoMap=" + groupFormationInfoMap +
                '}';
    }

    public boolean shouldActivate(String las) {
        return groupFormationInfoMap.containsKey(las);
    }
}
