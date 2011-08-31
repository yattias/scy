package eu.scy.core;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.SearchResultTransfer;
import eu.scy.core.roolo.RooloAccessor;
import roolo.search.ISearchResult;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 05:55:27
 * To change this template use File | Settings | File Templates.
 */
public interface BaseELOService extends RooloAccessor {

        List getRuntimeElos(MissionSpecificationElo missionSpecificationElo);
        
        List getUsersFromRuntimeElos(MissionSpecificationElo missionSpecificationElo);

        public List<SearchResultTransfer> getSearchResultTransfers(List<ISearchResult> searchResults, Locale locale);

}
