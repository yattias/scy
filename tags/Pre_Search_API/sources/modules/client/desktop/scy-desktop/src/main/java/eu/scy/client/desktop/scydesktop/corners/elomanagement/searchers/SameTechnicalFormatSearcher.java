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
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class SameTechnicalFormatSearcher implements EloBasedSearcher {

   final private ToolBrokerAPI tbi;
   final private IMetadataKey technicalFormatKey;

   public SameTechnicalFormatSearcher(ToolBrokerAPI tbi)
   {
      this.tbi = tbi;
      technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   @Override
   public String toString()
   {
      return getDisplayId();
   }

   @Override
   public String getDisplayId()
   {
      return "findSameTechnicalFormat";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      final IQuery query =  new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, scyElo.getTechnicalFormat());
      return tbi.getRepository().search(query);
   }

}
