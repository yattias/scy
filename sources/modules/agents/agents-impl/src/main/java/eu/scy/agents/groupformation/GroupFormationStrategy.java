package eu.scy.agents.groupformation;

import eu.scy.agents.groupformation.cache.GroupCache;
import info.collide.sqlspaces.client.TupleSpace;
import roolo.elo.api.IELO;

import java.util.Collection;
import java.util.Set;

public interface GroupFormationStrategy {

    public Collection<Set<String>> formGroup(IELO elo);

    public void setCommandSpace(TupleSpace commandSpace);

    public void setGroupFormationCache(GroupCache groupFormationCache);

    public void setMission(String mission);

    public void setMinimumGroupSize(int minGroupSize);

    public void setMaximumGroupSize(int maxGroupSize);

    public void setLas(String las);

    public void setAvailableUsers(Set<String> availableUsers);

    public GroupFormationStrategy makeNewEmptyInstance();

}
