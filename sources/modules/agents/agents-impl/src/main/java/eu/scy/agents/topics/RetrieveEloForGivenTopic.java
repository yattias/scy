package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.metadata.keys.KeyValuePair;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.SearchOperation;

/**
 * Retrieves ELOs that contain a certain topic. ("getTopicElos",
 * <QueryId>:String, <TopicId>:Integer, <MinProb>:Double) -> ("getTopicElos",
 * <QueryId>:String, <NumELOs>:Integer, <ELOUri_1>:<String>,
 * <ELOUri_2>:<String>, ...)
 * 
 * @author Florian Schulz
 */
public class RetrieveEloForGivenTopic extends AbstractRequestAgent implements
		IRepositoryAgent {

	static final String NAME = RetrieveEloForGivenTopic.class.getName();

	private IMetadataTypeManager typeManager;
	private IRepository repository;

	public RetrieveEloForGivenTopic(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			Tuple t = getCommandSpace().waitToTake(getTemplateTuple(),
					AgentProtocol.COMMAND_EXPIRATION);
			if (t != null) {
				String queryId = (String) t.getField(2).getValue();
				Integer topicId = (Integer) t.getField(3).getValue();
				Double topicProbability = (Double) t.getField(4).getValue();
				KeyValuePair topicQuery = new KeyValuePair();
				topicQuery.setKey("" + topicId);
				topicQuery.setValue("" + topicProbability);
				IQueryComponent queryComponent = new MetadataQueryComponent(typeManager
						.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES),
						SearchOperation.HAS, topicQuery.toLuceneString());
                                IQuery query = new Query(queryComponent);
				List<ISearchResult> hits = repository.search(query);

				List<URI> resultURIs = collectResults(topicId,
						topicProbability, hits);
				sendAnswer(queryId, resultURIs);
			} else {
				sendAliveUpdate();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<URI> collectResults(Integer topicId, Double topicProbability,
			List<ISearchResult> hits) {
		IMetadataKey topicScoresKey = typeManager
				.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES);

		List<URI> resultURIs = new ArrayList<URI>();
		for (ISearchResult hit : hits) {
			IELO elo = repository.retrieveELO(hit.getUri());
			List<? extends KeyValuePair> topicScoreEntryList = (List<KeyValuePair>) elo
					.getMetadata().getMetadataValueContainer(topicScoresKey)
					.getValueList();
			for (KeyValuePair entry : topicScoreEntryList) {
				if (entry.getKey().equals("" + topicId)) {
					if (Double.parseDouble(entry.getValue()) >= topicProbability) {
						resultURIs.add(hit.getUri());
					}
				}
			}
		}
		return resultURIs;
	}

	private void sendAnswer(String queryId, List<URI> resultURIs)
			throws TupleSpaceException {
		Tuple answer = new Tuple();
		answer.add(TopicAgents.GET_TOPIC_ELOS);
		answer.add(AgentProtocol.RESPONSE);
		answer.add(queryId);
		answer.add(resultURIs.size());
		for (URI uri : resultURIs) {
			answer.add(uri.toString());
		}
		getCommandSpace().write(answer);
	}

	private Tuple getTemplateTuple() {
		return new Tuple(TopicAgents.GET_TOPIC_ELOS, AgentProtocol.QUERY,
				String.class, Integer.class, Double.class);
	}

	@Override
	protected void doStop() {
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return new Tuple();
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		typeManager = manager;
	}

	@Override
	public void setRepository(IRepository rep) {
		repository = rep;
	}

}
