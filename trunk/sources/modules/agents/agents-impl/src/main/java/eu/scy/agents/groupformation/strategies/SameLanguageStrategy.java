package eu.scy.agents.groupformation.strategies;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roolo.elo.api.IELO;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.impl.AgentProtocol;

public class SameLanguageStrategy implements GroupFormationStrategy {

	private TupleSpace commandSpace;

	@Override
	public List<String> formGroup(IELO elo, String mission, String user) {
		String language = getLanguage(mission, user);
		List<String> availableUsers = getAvailableUsers(mission);

		List<String> result = new ArrayList<String>();
		for (String availableUser : availableUsers) {
			String languageOfOtherUsers = getLanguage(mission, availableUser);
			if (language.equals(languageOfOtherUsers)) {
				result.add(availableUser);
			}
		}
		return result;
	}

	// TODO implement
	private List<String> getAvailableUsers(String mission) {
		return Collections.emptyList();
	}

	private String getLanguage(String mission, String user) {
		String queryId = new VMID().toString();
		try {
			getCommandSpace().write(
					new Tuple("language", AgentProtocol.QUERY, queryId,
							mission, user));
			Tuple response = getCommandSpace().waitToTake(
					new Tuple("language", AgentProtocol.RESPONSE, queryId,
							String.class), AgentProtocol.ALIVE_INTERVAL * 5);
			String language = (String) response.getField(3).getValue();
			return language;
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return "en";
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
