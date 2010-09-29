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
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
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
   public String getDisplayId()
   {
      return "sameTechnicalFormat";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      final IQuery query =  new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, scyElo.getTechnicalFormat(), null);
      return tbi.getRepository().search(query);
   }

}
