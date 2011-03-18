package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.Set;

import eu.scy.agents.groupformation.GroupFormationScope;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.GroupCache;

public abstract class AbstractGroupFormationStrategy implements
		GroupFormationStrategy {

	protected GroupCache cache;
	protected TupleSpace commandSpace;
	protected GroupFormationScope scope;
	protected String mission;
	protected String las;

	protected int maximumGroupSize;
	protected int minimumGroupSize;
	private Set<String> availableUsers;

	@Override
	public void setGroupFormationCache(GroupCache groupFormationCache) {
		this.cache = groupFormationCache;
	}

	@Override
	public void setMaximumGroupSize(int maxGroupSize) {
		maximumGroupSize = maxGroupSize;
	}

	@Override
	public void setMinimumGroupSize(int minGroupSize) {
		minimumGroupSize = minGroupSize;
	}

	public GroupFormationScope getScope() {
		return scope;
	}

	public String getMission() {
		return mission;
	}

	public int getMaximumGroupSize() {
		return maximumGroupSize;
	}

	public int getMinimumGroupSize() {
		return minimumGroupSize;
	}

	@Override
	public void setMission(String mission) {
		this.mission = mission;
	}

	@Override
	public void setScope(GroupFormationScope scope) {
		this.scope = scope;
	}

	public TupleSpace getCommandSpace() {
		return commandSpace;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
		this.commandSpace = commandSpace;
	}

	public String getLas() {
		return las;
	}

	@Override
	public void setLas(String las) {
		this.las = las;
	}

	public Set<String> getAvailableUsers() {
		return availableUsers;
	}

	protected int getNumberOfGroups(int numberOfUsers) {
		int minimumNumberOfGroups = 0;
		if (numberOfUsers % getMaximumGroupSize() == 0) {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize();
		} else {
			minimumNumberOfGroups = numberOfUsers / getMaximumGroupSize() + 1;
		}

		int maximumNumberOfGroups = 0;
		if (numberOfUsers % getMinimumGroupSize() == 0) {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize();
		} else {
			maximumNumberOfGroups = numberOfUsers / getMinimumGroupSize() + 1;
		}

		return (int) Math
				.floor((maximumNumberOfGroups + minimumNumberOfGroups) / 2.0);
	}

	@Override
	public void setAvailableUsers(Set<String> availableUsers) {
		this.availableUsers = availableUsers;
	}

}