package eu.scy.agents.groupformation;

import java.util.HashMap;
import java.util.Map;

import eu.scy.agents.groupformation.strategies.NoGroupStrategy;

public class GroupFormationStrategyFactory {

	private Map<String, GroupFormationStrategy> strategyMap;

	public GroupFormationStrategyFactory() {
		strategyMap = new HashMap<String, GroupFormationStrategy>();
	}

	public GroupFormationStrategy getStrategy(String strategyName) {
		GroupFormationStrategy strategy = strategyMap.get(strategyName);
		if (strategy == null) {
			return new NoGroupStrategy();
		}
		return strategy;
	}

	public Map<String, GroupFormationStrategy> getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(Map<String, GroupFormationStrategy> strategyMap) {
		this.strategyMap = strategyMap;
	}

}
