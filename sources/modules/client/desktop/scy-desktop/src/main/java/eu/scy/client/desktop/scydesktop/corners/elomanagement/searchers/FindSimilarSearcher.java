/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.AbstractQueryComponent;
import roolo.search.AndQuery;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;

/**
 *
 * @author weinbrenner
 */
public class FindSimilarSearcher implements EloBasedSearcher {

    final private ToolBrokerAPI tbi;

   final private IMetadataKey keywordsKey;

    public FindSimilarSearcher(ToolBrokerAPI tbi) {
        this.tbi = tbi;
        keywordsKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS);
    }

    @Override
    public String getDisplayId() {
        return "Find similar";
    }

    @Override
    public List<ISearchResult> findElos(ScyElo scyElo) {
        StringBuilder sb = new StringBuilder();
        for (String keyword : scyElo.getKeywords()) {
            sb.append(keyword);
            sb.append(" ");
        }
        for (String tag : scyElo.getTagNames()) {
            sb.append(tag);
            sb.append(" ");
        }
        for (String author : scyElo.getAuthors()) {
            sb.append(author);
            sb.append(" ");
        }
        sb.append(scyElo.getTitle());
        return tbi.getRepository().search(new Query(new MetadataQueryComponent("contents", sb)));
    }
}