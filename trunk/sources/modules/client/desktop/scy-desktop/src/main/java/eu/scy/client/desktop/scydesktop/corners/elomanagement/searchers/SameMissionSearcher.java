/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.List;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author SikkenJ
 */
public class SameMissionSearcher implements EloBasedSearcher
{

   final private ToolBrokerAPI tbi;
   final private IMetadataKey missionRunningKey;
   final private boolean findSame;

   public SameMissionSearcher(ToolBrokerAPI tbi, boolean findSame)
   {
      this.tbi = tbi;
      this.findSame = findSame;
      missionRunningKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
   }

   @Override
   public String toString()
   {
      return getDisplayId();
   }

   @Override
   public String getDisplayId()
   {
      if (findSame)
      {
         return "Find same mission";
      }
      else
      {
         return "Find other mission";
      }
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      URI missionSpecificationEloUri = scyElo.getMissionSpecificationEloUri();
      if (missionSpecificationEloUri==null){
         return null;
      }
      SearchOperation searchOperation;
      if (findSame)
      {
         searchOperation = SearchOperation.EQUALS;
      }
      else
      {
         searchOperation = SearchOperation.NOT_EQUALS;
      }
      final IQueryComponent queryComponent = new MetadataQueryComponent(missionRunningKey, searchOperation, missionSpecificationEloUri);
      final IQuery query = new Query(queryComponent);
      return tbi.getRepository().search(query);
   }
}
