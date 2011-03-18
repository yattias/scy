package eu.scy.agents.groupformation;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.Collection;
import java.util.Set;

import eu.scy.agents.groupformation.cache.GroupCache;

import roolo.elo.api.IELO;

public interface GroupFormationStrategy {

	public Collection<Set<String>> formGroup(IELO elo);

	public void setCommandSpace(TupleSpace commandSpace);

	public void setGroupFormationCache(GroupCache groupFormationCache);

	public void setScope(GroupFormationScope scope);

	public void setMission(String mission);

	public void setMinimumGroupSize(int minGroupSize);

	public void setMaximumGroupSize(int maxGroupSize);

	public void setLas(String las);

	public void setAvailableUsers(Set<String> availableUsers);

	public GroupFormationStrategy makeNewEmptyInstance();

}
