package eu.scy.common.scyelo;

import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.AbstractQueryComponent;
import roolo.search.AndQuery;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.SearchOperation;

/**
 *
 * @author sven
 */
public class QueryFactory {

    /**
     * This method creates a google-like query. This means that all metadata are queried to match the terms from the query phrase somehow
     * @param queryPhrase the terms to look for
     * @return a query object to use for RoOLO search (repository.search(IQuery query))
     */
    public static IQuery createSimpleQuery(String queryPhrase) {
        AbstractQueryComponent queryComponent = new MetadataQueryComponent(queryPhrase, SearchOperation.HAS);
        IQuery query = new Query(queryComponent);
        query.setFindDeleted(false);
        query.setFindHidden(false);
        return query;
    }

    /**
     * This method creates a query for all ELOs from an author for a specific mission
     * @param author the author of the ELOs (which is actually just the plain username from the Contribute Object), e.g. "sven"
     * @param mission the mission name (e.g. "ecomission")
     * @return a query object to use for RoOLO search (repository.search(IQuery query))
     */
    public static IQuery createElosForMissionQuery(String author, String mission) {
        MetadataQueryComponent missionQueryComponent = new MetadataQueryComponent(ScyRooloMetadataKeyIds.MISSION.getId(), SearchOperation.EQUALS, mission);
        MetadataQueryComponent authorQueryComponent = new MetadataQueryComponent(CoreRooloMetadataKeyIds.AUTHOR.getId(), SearchOperation.EQUALS, author);
        AndQuery andQuery = new AndQuery(missionQueryComponent, authorQueryComponent);
        IQuery q = new Query(andQuery);
        q.setMaxResults(300);
        return q;
    }

     /**
     * This method creates a query to retrieve a mission runtime for a specific user
     * @param username the user running the mission
     * @param missionRuntimeType the type of the missionruntime (e.g. "scy/missionruntime")
     * @return a query object to use for RoOLO search (repository.search(IQuery query))
     */
    public static IQuery createMissionRuntimeQuery(String missionRuntimeType, String username) {
        IQueryComponent missionRuntimeQueryComp = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
                SearchOperation.EQUALS, missionRuntimeType);
        IQueryComponent titleQueryComp = new MetadataQueryComponent(ScyRooloMetadataKeyIds.USER_RUNNING_MISSION.getId(), SearchOperation.EQUALS,
                username);
        IQueryComponent myMissionRuntimeQuery = new AndQuery(missionRuntimeQueryComp, titleQueryComp);
        IQuery q = new Query(myMissionRuntimeQuery);
        return q;
    }

     /**
     * This method creates a query to retrieve all mission runtime for a specific URI
     * @param missionRuntimeType the type of the missionruntime (e.g. "scy/missionruntime")
     * @param missionRuntimeURI the URI of the mission runtime
     * @return a query object to use for RoOLO search (repository.search(IQuery query))
     */
    public static IQuery createAllMissionRuntimesQuery(String missionRuntimeType, URI missionRuntimeURI) {
        IQueryComponent missionRuntimeQueryComp = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
                SearchOperation.EQUALS, missionRuntimeType);
        IQueryComponent missionSpecifiactionQuery = new MetadataQueryComponent(ScyRooloMetadataKeyIds.MISSION_SPECIFICATION_ELO.getId(),
                SearchOperation.EQUALS, missionRuntimeURI.toString());
        IQueryComponent allMissionRuntimesQuery = new AndQuery(missionRuntimeQueryComp, missionSpecifiactionQuery);
        IQuery q = new Query(allMissionRuntimesQuery);
         q.setMaxResults(300);
        return q;
    }
    
     public static IQuery createMissionLocatorQuery(String missionRuntimeType, URI missionRuntimeURI) {
        IQueryComponent missionRuntimeQueryComp = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
                SearchOperation.EQUALS, missionRuntimeType);
        IQueryComponent missionSpecifiactionQuery = new MetadataQueryComponent(ScyRooloMetadataKeyIds.MISSION_SPECIFICATION_ELO.getId(),
                SearchOperation.EQUALS, missionRuntimeURI.toString());
        IQueryComponent allMissionRuntimesQuery = new AndQuery(missionRuntimeQueryComp, missionSpecifiactionQuery);
        IQuery q = new Query(allMissionRuntimesQuery);
         q.setMaxResults(300);
        return q;
    }

     public static IQuery createFeedbackEloQuery(URI feedbackParentURI, String technicalFormat){
         IQueryComponent feedbackQC = new MetadataQueryComponent(ScyRooloMetadataKeyIds.FEEDBACK_ON.getId(),SearchOperation.EQUALS,feedbackParentURI);
         IQueryComponent typeQC = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),SearchOperation.EQUALS,technicalFormat);
         IQueryComponent andQuery = new AndQuery(typeQC, feedbackQC);
         Query q = new Query(andQuery);
         q.setFindDeleted(false);
         q.setFindHidden(true);
         q.setLatestOnly(true);
         return q;
     }
}
