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
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;
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
      return "Find same technical format";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      final IQuery query =  new Query(new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, scyElo.getTechnicalFormat()));
      return tbi.getRepository().search(query);
   }

}
