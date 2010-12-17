package eu.scy.agents.groupformation;

import java.util.HashMap;
import java.util.Map;

import eu.scy.agents.groupformation.strategies.ClusterStrategy;
import eu.scy.agents.groupformation.strategies.DummyStrategy;
import eu.scy.agents.groupformation.strategies.NoGroupStrategy;
import eu.scy.agents.groupformation.strategies.SameGroupsStrategy;

public class GroupFormationStrategyFactory {

	private static final String SAME = "same";
	private static final String CLUSTER = "cluster";
	private static final String DUMMY = "dummy";
	private Map<String, GroupFormationStrategy> strategyMap;

	public GroupFormationStrategyFactory() {
		strategyMap = new HashMap<String, GroupFormationStrategy>();
		strategyMap.put(DUMMY, new DummyStrategy());
		strategyMap.put(CLUSTER, new ClusterStrategy());
		strategyMap.put(SAME, new SameGroupsStrategy());
	}

	public GroupFormationStrategy getStrategy(String strategyName) {
		GroupFormationStrategy strategy = strategyMap.get(strategyName);
		if (strategy == null) {
			return new NoGroupStrategy();
		}
		return strategy.makeNewEmptyInstance();
	}

	public Map<String, GroupFormationStrategy> getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(Map<String, GroupFormationStrategy> strategyMap) {
		this.strategyMap = strategyMap;
	}

}
