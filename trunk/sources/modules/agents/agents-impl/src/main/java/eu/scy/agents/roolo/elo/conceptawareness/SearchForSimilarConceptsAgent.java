package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractProcessingAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Searches for similar concept maps of a saved ELO, retrieves the creators of
 * the found ELOs and returns this list of users as potential collaborators.
 * 
 * ("scymapper":String, <TS>:Long, <EloURI>:String) ->
 * ("searchSimilarElosAgent":String, <RelatedUserList>:String, <User>:String,
 * <EloUri>:String) <br />
 * +<TS> Timestamp <br />
 * +<EloURI>: The uri of the elo for that similar elos should be searched.<br />
 * -<RelatedUserList> Creators of similar elos. -<User> creator of original elo
 * 
 * @author Florian Schulz
 * 
 */
public class SearchForSimilarConceptsAgent extends AbstractProcessingAgent
		implements IRepositoryAgent {

	/**
	 * Name of the agent.
	 */
	public static final String SEARCH_FOR_SIMILAR_CONCEPTS_AGENT_NAME = "SearchForSimilarConceptsAgent";

	private IMetadataTypeManager metadataTypeManager;
	private HashMap<URI, Double> score;
	private IRepository repository;

	/**
	 * Create a new SearchForSimilarConceptsAgent filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public SearchForSimilarConceptsAgent(Map<String, Object> map) {
		super(SEARCH_FOR_SIMILAR_CONCEPTS_AGENT_NAME, (String) map.get("id"));
		score = new HashMap<URI, Double>();
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getTupleSpace().waitToTake(getTemplateTuple(),
						AgentProtocol.ALIVE_INTERVAL);
				if (trigger == null) {
					continue;
				}
				URI eloUri = new URI((String) trigger.getField(2).getValue());

				IELO elo = repository.retrieveELO(eloUri);

				if (elo == null) {
					continue;
				}
				IMetadata metadata = elo.getMetadata();

				Set<URI> hits = searchForRelatedElos(eloUri, repository,
						metadata);

				Set<String> relatedUsers = new HashSet<String>();
				for (URI hitUri : hits) {
					if (score.get(hitUri) < 0.3) {
						continue;
					}
					IMetadata hitMetadata = repository.retrieveMetadata(hitUri);
					IMetadataValueContainer value = hitMetadata
							.getMetadataValueContainer(metadataTypeManager
									.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR
											.getId()));
					Contribute contribution = (Contribute) value.getValue();
					String userGUID = contribution.getVCard();
					relatedUsers.add(userGUID);
				}

				StringBuffer relatedUserList = new StringBuffer();
				for (String relatedUser : relatedUsers) {
					relatedUserList.append(relatedUser);
					relatedUserList.append(";");
				}

				IMetadataKey authorKey = metadataTypeManager
						.getMetadataKey("author");
				IMetadataValueContainer authorContainer = metadata
						.getMetadataValueContainer(authorKey);
				Contribute author = (Contribute) authorContainer.getValue();
				String user = "";
				if (author != null) {
					user = author.getVCard();
				}

				Tuple notificationTuple = new Tuple("searchSimilarElosAgent",
						relatedUserList.toString().trim(), user, eloUri
								.toString());
				getTupleSpace().write(notificationTuple);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (TupleSpaceException e) {
				if (status != Status.Stopping) {
					stop();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Set<URI> searchForRelatedElos(URI originalEloUri,
			IRepository repository, IMetadata metadata) {

		Set<URI> hits = new HashSet<URI>();

		double maxScore = -1;

		IMetadataKey nodeLabelKey = metadataTypeManager
				.getMetadataKey("nodeLabel");
		IMetadataValueContainer nodeValues = metadata
				.getMetadataValueContainer(nodeLabelKey);
		List<String> nodeLabels = (List<String>) nodeValues.getValueList();
		for (String nodeLabel : nodeLabels) {
			IQuery query = new BasicMetadataQuery(nodeLabelKey,
					BasicSearchOperations.EQUALS, nodeLabel, null);
			List<ISearchResult> results = repository.search(query);
			for (ISearchResult result : results) {
				hits.add(result.getUri());

				Double oldScore = score.get(result.getUri());
				if (oldScore == null) {
					oldScore = 0.0;
				}
				score.put(result.getUri(), oldScore + 1);
				if (maxScore < oldScore + 1.0) {
					maxScore = oldScore + 1.0;
				}
			}
		}

		IMetadataKey linkLabelKey = metadataTypeManager
				.getMetadataKey("linkLabel");
		IMetadataValueContainer linkValues = metadata
				.getMetadataValueContainer(linkLabelKey);
		List<String> linkLabels = (List<String>) linkValues.getValueList();
		for (String linkLabel : linkLabels) {
			IQuery query = new BasicMetadataQuery(linkLabelKey,
					BasicSearchOperations.EQUALS, linkLabel, null);
			List<ISearchResult> results = repository.search(query);
			for (ISearchResult result : results) {
				hits.add(result.getUri());

				Double oldScore = score.get(result.getUri());
				if (oldScore == null) {
					oldScore = 0.0;
				}
				score.put(result.getUri(), oldScore + 1);
				if (maxScore < oldScore + 1.0) {
					maxScore = oldScore + 1.0;
				}
			}
		}

		for (URI hit : score.keySet()) {
			Double hitScore = score.get(hit);
			hitScore = hitScore / maxScore;
			score.put(hit, hitScore);
		}

		hits.remove(originalEloUri);
		score.remove(originalEloUri);
		return hits;
	}

	private Tuple getTemplateTuple() {
		return new Tuple("scymapper", Long.class, String.class);
	}

	public IRepository getRepository() {
		return repository;
	}

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
		this.metadataTypeManager = metadataTypeManager;
	}

	public void setRepository(IRepository repo) {
		repository = repo;
	}

	public IMetadataTypeManager getMetadataTypeManager() {
		return metadataTypeManager;
	}

	@Override
	protected void doStop() {
		status = Status.Stopping;
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

}
