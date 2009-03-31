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
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.impl.AbstractProcessingAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotification;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class SearchForSimilarConceptsAgent<Key extends IMetadataKey> extends
		AbstractProcessingAgent<Key> {

	private static final String USER = "similarEloAgent";
	private IMetadataTypeManager<Key> metadataTypeManager;
	private static HashMap<String, String> userMap = new HashMap<String, String>();

	static {
		userMap.put("e3457b7e-73c4-418b-8cc2-b8534f01eee1", "adam");
		userMap.put("6903ce4b-6643-4bb2-9ba7-123a19e05e5d", "henrik");
		userMap.put("4270398a-90e5-4588-8182-85c62832d377", "florian");
		userMap.put("5ee4d993-c938-4bc8-91db-cc5e6433c5dc", "lars");
	}

	public SearchForSimilarConceptsAgent() {
		super("SearchForSimilarConceptsAgent");
		metadataTypeManager = getMetadataTypeManager();
	}

	public SearchForSimilarConceptsAgent(ToolBrokerAPI<Key> toolBroker) {
		super("SearchForSimilarConceptsAgent", toolBroker);
		metadataTypeManager = getMetadataTypeManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doRun() {
		try {
			Tuple tuple = getTupleSpace().waitToTake(
					new Tuple("scymapper", Long.class, String.class));
			String eloUri = (String) tuple.getField(2).getValue();

			IRepository<IELO<Key>, Key> repository = getRepository();
			IELO<Key> elo = repository.retrieveELO(new URI(eloUri));
			IMetadata<Key> metadata = elo.getMetadata();

			Key nodeLabelKey = metadataTypeManager.getMetadataKey("nodeLabel");
			IMetadataValueContainer values = metadata
					.getMetadataValueContainer(nodeLabelKey);

			List<String> nodeLabels = (List<String>) values.getValueList();
			Set<URI> hits = new HashSet<URI>();

			for (String nodeLabel : nodeLabels) {
				IQuery query = new BasicMetadataQuery<Key>(nodeLabelKey,
						BasicSearchOperations.EQUALS, nodeLabel, null);
				List<ISearchResult> results = repository.search(query);
				for (ISearchResult result : results) {
					hits.add(result.getUri());
				}
			}

			Set<String> relatedUsers = new HashSet<String>();
			for (URI hitUri : hits) {
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
				relatedUserList.append(userMap.get(relatedUser));
				relatedUserList.append(" ");
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
}
