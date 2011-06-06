/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.OrQuery;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;
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
      return "Find same author";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      final List<String> authors = scyElo.getAuthors();
      if (authors == null || authors.isEmpty()){
         return null;
      }
      IQueryComponent queryComponent = new MetadataQueryComponent(authorKey, SearchOperation.EQUALS, new Contribute(authors.get(0),System.currentTimeMillis()));
      IQuery query = new Query(queryComponent);
      if (authors.size()>1){
         IQueryComponent[] queryComponents = new IQueryComponent[authors.size()-1];
         for (int i = 1; i<authors.size();i++){
             IQueryComponent newQueryComponent = new MetadataQueryComponent(authorKey, SearchOperation.EQUALS, new Contribute(authors.get(i),System.currentTimeMillis()));
            queryComponents[i-1] = newQueryComponent;
         }
         query = new Query(new OrQuery(queryComponent,queryComponents));
      } 
      return tbi.getRepository().search(query);
   }

}
