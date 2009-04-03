package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.impl.AbstractProcessingAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotification;

public class SearchForSimilarConceptsAgent<Key extends IMetadataKey> extends
		AbstractProcessingAgent<Key> {

	private static final String USER = "similarEloAgent";
	// private IMetadataTypeManager<Key> metadataTypeManager;
	private HashMap<URI, Double> score;

	// private IRepository<IELO<Key>, Key> repository;

	// public SearchForSimilarConceptsAgent() {
	// super("SearchForSimilarConceptsAgent");
	// // metadataTypeManager = getMetadataTypeManager();
	// score = new HashMap<URI, Double>();
	// }

	public SearchForSimilarConceptsAgent() {
		super("SearchForSimilarConceptsAgent");
		score = new HashMap<URI, Double>();
	}

	// SearchForSimilarConceptsAgent(ToolBrokerAPI<Key> toolBroker) {
	// super("SearchForSimilarConceptsAgent", toolBroker);
	// metadataTypeManager = getMetadataTypeManager();
	// score = new HashMap<URI, Double>();
	// }

	@Override
	protected void doRun() {
		try {
			Tuple tuple = getTupleSpace().waitToTake(
					new Tuple("scymapper", Long.class, String.class));
			URI eloUri = new URI((String) tuple.getField(2).getValue());

			IELO<Key> elo = repository.retrieveELO(eloUri);
			IMetadata<Key> metadata = elo.getMetadata();

			Set<URI> hits = searchForRelatedElos(eloUri, repository, metadata);

			Set<String> relatedUsers = new HashSet<String>();
			for (URI hitUri : hits) {
				if (score.get(hitUri) < 0.6) {
					continue;
				}
				IMetadata<Key> hitMetadata = repository
						.retrieveMetadata(hitUri);
				IMetadataValueContainer value = hitMetadata
						.getMetadataValueContainer(metadataTypeManager
								.getMetadataKey(RooloMetadataKeys.AUTHOR
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

			INotification notification = new Notification();
			notification
					.addProperty("users", relatedUserList.toString().trim());
			notification.addProperty("target", "awareness");
			notification.addProperty("eloUri", elo.getUri().toString());
			NotificationSender sender = new NotificationSender();
			sender.send(USER, "searchSimilarElosAgent", notification);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Set<URI> searchForRelatedElos(URI originalEloUri,
			IRepository<IELO<Key>, Key> repository, IMetadata<Key> metadata) {

		Set<URI> hits = new HashSet<URI>();

		double maxScore = -1;

		Key nodeLabelKey = metadataTypeManager.getMetadataKey("nodeLabel");
		IMetadataValueContainer nodeValues = metadata
				.getMetadataValueContainer(nodeLabelKey);
		List<String> nodeLabels = (List<String>) nodeValues.getValueList();
		for (String nodeLabel : nodeLabels) {
			IQuery query = new BasicMetadataQuery<Key>(nodeLabelKey,
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

		Key linkLabelKey = metadataTypeManager.getMetadataKey("linkLabel");
		IMetadataValueContainer linkValues = metadata
				.getMetadataValueContainer(linkLabelKey);
		List<String> linkLabels = (List<String>) linkValues.getValueList();
		for (String linkLabel : linkLabels) {
			IQuery query = new BasicMetadataQuery<Key>(linkLabelKey,
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
}
