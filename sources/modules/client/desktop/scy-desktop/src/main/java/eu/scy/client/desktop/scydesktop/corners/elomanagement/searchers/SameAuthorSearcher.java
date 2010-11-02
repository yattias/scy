/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.api.search.OrQuery;
import org.roolo.rooloimpljpa.repository.search.BasicMetadataQuery;
import org.roolo.rooloimpljpa.repository.search.BasicSearchOperations;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author SikkenJ
 */
public class SameAuthorSearcher implements EloBasedSearcher {

   final private ToolBrokerAPI tbi;
   final private IMetadataKey authorKey;

   public SameAuthorSearcher(ToolBrokerAPI tbi)
   {
      this.tbi = tbi;
      authorKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
   }

   @Override
   public String toString()
   {
      return getDisplayId();
   }

   @Override
   public String getDisplayId()
   {
      return "findSameAuthor";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      final List<String> authors = scyElo.getAuthors();
      if (authors == null || authors.isEmpty()){
         return null;
      }
      IQuery query = new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, new Contribute(authors.get(0),System.currentTimeMillis()));
      if (authors.size()>1){
         IQuery[] queries = new IQuery[authors.size()-1];
         for (int i = 1; i<authors.size();i++){
            queries[i-1] = new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, new Contribute(authors.get(i),System.currentTimeMillis()));
         }
         query = new OrQuery(query,queries);
      }
      return tbi.getRepository().search(query);
   }

}
