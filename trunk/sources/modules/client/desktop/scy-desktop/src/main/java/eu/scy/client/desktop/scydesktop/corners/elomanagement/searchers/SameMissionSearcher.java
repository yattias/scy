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
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
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
         return "findSameMission";
      }
      else
      {
         return "findOtherMission";
      }
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      URI missionSpecificationEloUri = scyElo.getMissionSpecificationEloUri();
      if (missionSpecificationEloUri==null){
         return null;
      }
      BasicSearchOperations searchOperation;
      if (findSame)
      {
         searchOperation = BasicSearchOperations.EQUALS;
      }
      else
      {
         searchOperation = BasicSearchOperations.NOT_EQUALS;
      }
      final IQuery query = new BasicMetadataQuery(missionRunningKey, searchOperation, missionSpecificationEloUri);
      return tbi.getRepository().search(query);
   }
}
