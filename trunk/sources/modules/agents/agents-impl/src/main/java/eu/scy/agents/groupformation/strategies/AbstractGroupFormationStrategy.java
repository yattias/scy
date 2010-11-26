package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;
import eu.scy.agents.groupformation.GroupFormationCache;
import eu.scy.agents.groupformation.GroupFormationStrategy;

public abstract class AbstractGroupFormationStrategy implements
		GroupFormationStrategy {

	protected GroupFormationCache cache;
	protected TupleSpace commandSpace;
	private String scope;
	private String mission;
	private int maximumGroupSize;
	private int minimumGroupSize;

	@Override
	public void setGroupFormationCache(GroupFormationCache groupFormationCache) {
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

	public String getScope() {
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
	public void setScope(String scope) {
		this.scope = scope;
	}

	public TupleSpace getCommandSpace() {
		return commandSpace;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
		this.commandSpace = commandSpace;
	}

}