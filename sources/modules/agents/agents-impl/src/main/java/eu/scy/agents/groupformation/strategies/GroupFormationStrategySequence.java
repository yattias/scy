package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.util.CountingSet;

/**
 * A group formation strategy that combines several group formation strategies
 * in a sequence. Only possible group member that are found in predefined number
 * of strategies are included into the final group.
 * 
 * @author Florian Schulz
 * 
 */
public class GroupFormationStrategySequence implements GroupFormationStrategy {

	private List<GroupFormationStrategy> strategies;
	private Integer threshold = 2;
	private TupleSpace commandSpace;

	public GroupFormationStrategySequence() {
		strategies = new ArrayList<GroupFormationStrategy>();
	}

	@Override
	public List<String> formGroup(IELO elo, String mission, String user) {
		CountingSet<String> userMatchRuleCount = new CountingSet<String>();
		List<String> finalGroup = new ArrayList<String>();
		finalGroup.add(user);

		for (int i = 0; i < strategies.size(); i++) {
			GroupFormationStrategy strategy = strategies.get(i);
			strategy.setCommandSpace(commandSpace);
			List<String> intermediateGroup = strategy.formGroup(elo, mission,
					user);
			for (String proposedUser : intermediateGroup) {
				userMatchRuleCount.add(proposedUser);
			}
		}
		ArrayList<Entry<String, Integer>> countedRuleMatches = new ArrayList<Entry<String, Integer>>(
				userMatchRuleCount.getEntries());
		Collections.sort(countedRuleMatches,
				new Comparator<Entry<String, Integer>>() {
					@Override
					public int compare(Entry<String, Integer> o1,
							Entry<String, Integer> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});
		for (Entry<String, Integer> entry : countedRuleMatches) {
			if (entry.getValue() >= threshold) {
				finalGroup.add(entry.getKey());
			}
		}
		return finalGroup;
	}

	public void addStrategy(GroupFormationStrategy strategy) {
		strategies.add(strategy);
	}

	public void removeStrategy(int index) {
		strategies.remove(index);
	}

	@Override
	public TupleSpace getCommandSpace() {
		return commandSpace;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
		this.commandSpace = commandSpace;
	}

}
