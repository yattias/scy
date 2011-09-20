package eu.scy.agents.groupformation.strategies.features;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.common.scyelo.RooloServices;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IELO;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LanguageFeatureExtractor implements FeatureExtractor {

	private TupleSpace commandSpace;

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
	public Map<String, double[]> getFeatures(Set<String> availableUsers,
			String mission, IELO elo) {
		Map<String, Integer> languageAlphabet = new HashMap<String, Integer>();
		Map<String, double[]> results = new LinkedHashMap<String, double[]>();
		for (String user : availableUsers) {
			String language = getLanguage(mission, user);
			if (!languageAlphabet.containsKey(language)) {
				languageAlphabet.put(language, languageAlphabet.size());
			}
			results.put(user, new double[] { languageAlphabet.get(language) });
		}
		return results;
	}

	@Override
	public TupleSpace getCommandSpace() {
		return commandSpace;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
		this.commandSpace = commandSpace;
	}

	@Override
	public boolean canRun(IELO elo) {
		return true;
	}

    @Override
    public void setRepository(RooloServices repository) {
        // TODO Auto-generated method stub
        
    }

}
