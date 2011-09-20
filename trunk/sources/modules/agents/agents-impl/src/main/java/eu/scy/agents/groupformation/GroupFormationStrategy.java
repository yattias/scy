package eu.scy.agents.groupformation;

import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.GroupCache;
import eu.scy.common.scyelo.RooloServices;
import info.collide.sqlspaces.client.TupleSpace;
import roolo.elo.api.IELO;

import java.util.Collection;
import java.util.Set;

public interface GroupFormationStrategy {

    public Collection<Group> formGroup(IELO elo);

    public Collection<Group> assignToExistingGroups(String newUser, IELO referenceElo);

    public void setCommandSpace(TupleSpace commandSpace);

    public void setGroupFormationCache(GroupCache groupFormationCache);

    public void setMission(String mission);

    public void setMinimumGroupSize(int minGroupSize);

    public void setMaximumGroupSize(int maxGroupSize);

    public void setLas(String las);

    public void setAvailableUsers(Set<String> availableUsers);

    public GroupFormationStrategy makeNewEmptyInstance();

    public RooloServices getRooloServices();

    public  void setRooloServices(RooloServices repository);
}
