package eu.scy.agents.groupformation;

import eu.scy.agents.groupformation.strategies.ClusterStrategy;
import eu.scy.agents.groupformation.strategies.DummyStrategy;
import eu.scy.agents.groupformation.strategies.NoGroupStrategy;
import eu.scy.agents.groupformation.strategies.SameGroupsStrategy;
import eu.scy.common.mission.StrategyType;

import java.util.HashMap;
import java.util.Map;

public class GroupFormationStrategyFactory {

    private Map<StrategyType, GroupFormationStrategy> strategyMap;

    public GroupFormationStrategyFactory() {
        strategyMap = new HashMap<StrategyType, GroupFormationStrategy>();
        strategyMap.put(StrategyType.DUMMY, new DummyStrategy());
        strategyMap.put(StrategyType.CLUSTER, new ClusterStrategy());
        strategyMap.put(StrategyType.SAME, new SameGroupsStrategy());
    }

    public GroupFormationStrategy getStrategy(StrategyType strategyType) {
        GroupFormationStrategy strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            return new NoGroupStrategy();
        }
        return strategy.makeNewEmptyInstance();
    }

    public Map<StrategyType, GroupFormationStrategy> getStrategyMap() {
        return strategyMap;
    }

    public void setStrategyMap(Map<StrategyType, GroupFormationStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

}
