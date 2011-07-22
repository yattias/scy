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
public class LastModifiedQuerySelecterCreator implements QuerySelecterCreator {

   public final static String id = "lastModified";
   final private IMetadataKey lastModifiedKey;

   public LastModifiedQuerySelecterCreator(ToolBrokerAPI tbi)
   {
      lastModifiedKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
   }

   @Override
   public String getId()
   {
      return id;
   }

   @Override
   public QuerySelecter createQuerySelecter(QuerySelecterUsage querySelectorUsage)
   {
       if (QuerySelecterUsage.TEXT == querySelectorUsage)
      {
         return new LastModifiedQuerySelecter(lastModifiedKey);
      }
      return null;
   }

}
