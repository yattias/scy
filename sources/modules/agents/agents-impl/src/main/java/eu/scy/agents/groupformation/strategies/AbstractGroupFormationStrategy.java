package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;
import eu.scy.agents.groupformation.GroupFormationCache;
import eu.scy.agents.groupformation.GroupFormationScope;
import eu.scy.agents.groupformation.GroupFormationStrategy;

public abstract class AbstractGroupFormationStrategy implements
		GroupFormationStrategy {

	protected GroupFormationCache cache;
	protected TupleSpace commandSpace;
	protected GroupFormationScope scope;
	protected String mission;
	protected String las;

	protected int maximumGroupSize;
	protected int minimumGroupSize;

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

}