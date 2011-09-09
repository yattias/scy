package eu.scy.agents.groupformation;

import eu.scy.agents.groupformation.strategies.ClusterStrategy;
import eu.scy.agents.groupformation.strategies.DummyStrategy;
import eu.scy.agents.groupformation.strategies.NoGroupStrategy;
import eu.scy.agents.groupformation.strategies.SameGroupsStrategy;
import eu.scy.common.mission.GroupformationStrategyType;

import java.util.HashMap;
import java.util.Map;

public class GroupFormationStrategyFactory {

    private Map<GroupformationStrategyType, GroupFormationStrategy> strategyMap;

    public GroupFormationStrategyFactory() {
        strategyMap = new HashMap<GroupformationStrategyType, GroupFormationStrategy>();
        strategyMap.put(GroupformationStrategyType.DUMMY, new DummyStrategy());
        strategyMap.put(GroupformationStrategyType.CLUSTER, new ClusterStrategy());
        strategyMap.put(GroupformationStrategyType.SAME, new SameGroupsStrategy());
    }

    public GroupFormationStrategy getStrategy(GroupformationStrategyType strategyType) {
        GroupFormationStrategy strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            return new NoGroupStrategy();
        }
        return strategy.makeNewEmptyInstance();
    }

    public Map<GroupformationStrategyType, GroupFormationStrategy> getStrategyMap() {
        return strategyMap;
    }

    public void setStrategyMap(Map<GroupformationStrategyType, GroupFormationStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

}
