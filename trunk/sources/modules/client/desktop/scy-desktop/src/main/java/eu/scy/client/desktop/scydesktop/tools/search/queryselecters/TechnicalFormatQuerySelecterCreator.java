/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterCreator;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class TechnicalFormatQuerySelecterCreator implements QuerySelecterCreator
{

   public final static String id = "technicalFormat";
   final private IMetadataKey technicalFormatKey;

   public TechnicalFormatQuerySelecterCreator(ToolBrokerAPI tbi)
   {
      technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   @Override
   public String getId()
   {
      return id;
   }

   @Override
   public QuerySelecter createQuerySelecter(QuerySelecterUsage querySelectorUsage)
   {
      if (QuerySelecterUsage.ELO_BASED == querySelectorUsage)
      {
         return new TechnicalFormatQuerySelector(technicalFormatKey);
      }
      return null;
   }
}
