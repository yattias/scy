package eu.scy.core.roolo;

import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.core.BaseELOService;
import eu.scy.core.model.transfer.SearchResultTransfer;
import roolo.search.AndQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:26:23
 * To change this template use File | Settings | File Templates.
 */
public class BaseELOServiceImpl extends RooloAccessorImpl implements BaseELOService {

    private static Logger log = Logger.getLogger("BaseELOServiceImpl.class");

    private IMetadataKey authorKey;

    @Override
    public List<ISearchResult> getRuntimeElos(MissionSpecificationElo missionSpecificationElo) {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        IQuery missionRuntimeQuery = new Query(missionRuntimeQueryComponent);
        missionRuntimeQuery.setMaxResults(500);
        return getRepository().search(missionRuntimeQuery);
    }
    
    

    @Override
    public List getUsersFromRuntimeElos(MissionSpecificationElo missionSpecificationElo) {
        IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(), SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        IQueryComponent missionRunningQueryComponent = new MetadataQueryComponent(ScyRooloMetadataKeyIds.MISSION_RUNNING.getId(), SearchOperation.EQUALS, missionSpecificationElo.getUri());
        IQueryComponent andQuery = new AndQuery(missionRuntimeQueryComponent, missionRunningQueryComponent);
        IQuery missionRuntimeQuery = new Query(andQuery);
        missionRuntimeQuery.setMaxResults(500);
        
        List<ISearchResult> search = getRepository().search(missionRuntimeQuery);
        List<String> userNames = new LinkedList<String>();
        
        for (ISearchResult searchResult : search) {
            if (searchResult.getAuthors() != null && searchResult.getAuthors().size() > 0) {
                userNames.add(searchResult.getAuthors().get(0));
            }
        }
        return userNames;
    }



    public List getAllRuntimes() {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        IQuery missionRuntimeQuery = new Query(missionRuntimeQueryComponent);
        return getELOs(missionRuntimeQuery);

    }

    public IMetadataKey getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(IMetadataKey authorKey) {
        this.authorKey = authorKey;
    }

    public List<ISearchResult> findElosFor(String username) {
        log.info("Searching for elos for: " + username);
        IQueryComponent bmq2 = new MetadataQueryComponent(getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR), SearchOperation.EQUALS, username);
        IQuery q = new Query(bmq2);
        List <ISearchResult> result = getRepository().search(q);
        log.info("Found: " + result.size() + " elos...");
        return result;
    }

    @Override
    public List<SearchResultTransfer> getSearchResultTransfers(List<ISearchResult> searchResults, Locale locale) {
        List <SearchResultTransfer> returnLIst = new LinkedList<SearchResultTransfer>();
        for (int i = 0; i < searchResults.size(); i++) {
            ISearchResult iSearchResult = searchResults.get(i);
            returnLIst.add(new SearchResultTransfer(locale, iSearchResult));
        }

        return returnLIst;
    }

}
