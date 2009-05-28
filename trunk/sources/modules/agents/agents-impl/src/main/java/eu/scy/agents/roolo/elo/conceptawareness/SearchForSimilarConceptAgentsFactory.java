package eu.scy.agents.roolo.elo.conceptawareness;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.api.IParameter;
import eu.scy.agents.api.IThreadedAgent;

public class SearchForSimilarConceptAgentsFactory implements IAgentFactory {

	@SuppressWarnings("unchecked")
	@Override
	public IThreadedAgent create(IParameter params) {
		SearchForSimilarConceptsAgent<IMetadataKey> agent = new SearchForSimilarConceptsAgent<IMetadataKey>();
		/*agent
				.setRepository((IRepository<IELO<IMetadataKey>, IMetadataKey>) params
						.get(IParameter.ELO_REPOSITORY));
		agent
				.setRepository((IRepository<IELO<IMetadataKey>, IMetadataKey>) params
						.get(IParameter.METADATA_TYPE_MANAGER));
        */
        throw new RuntimeException("DID NOT BUILD -> HENRIK COMMENTED IT OUT!");
	}

	@Override
	public String getAgentName() {
		return SearchForSimilarConceptsAgent.SEARCH_FOR_SIMILAR_CONCEPTS_AGENT_NAME;
	}

}
