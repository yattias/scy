package eu.scy.agents.keywords;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractorFactory;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class AddKeywordsToMetadataAgent extends AbstractELOSavedAgent implements
		IRepositoryAgent {

	private final static String NAME = AddKeywordsToMetadataAgent.class
			.getName();

	private IMetadataTypeManager metadataTypeManager;
	private IRepository repository;

	protected AddKeywordsToMetadataAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	@Override
	protected void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType) {

		IELO elo = getELO(eloUri);
		if (elo == null) {
			return;
		}

		KeywordExtractor extractor = KeywordExtractorFactory
				.getKeywordExtractor(eloType);
		extractor.setTupleSpace(getCommandSpace());
		List<String> keywords = extractor.getKeywords(elo);

		addKeywordsToMetadata(elo, keywords);
	}

	private void addKeywordsToMetadata(IELO elo, List<String> keywords) {
		if (keywords.isEmpty()) {
			return;
		}
		IMetadataKey keywordKey = metadataTypeManager
				.getMetadataKey(eu.scy.agents.keywords.KeywordConstants.AGENT_KEYWORDS);
		IMetadataValueContainer agentKeywordsContainer = elo.getMetadata()
				.getMetadataValueContainer(keywordKey);
		agentKeywordsContainer.setValueList(keywords);

		repository.updateELO(elo);
	}

	private IELO getELO(String eloUri) {
		try {
			return repository.retrieveELO(new URI(eloUri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		metadataTypeManager = manager;
	}

	@Override
	public void setRepository(IRepository rep) {
		repository = rep;
	}

}
