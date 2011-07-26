/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterCreator;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class MissionQuerySelecterCreator  implements QuerySelecterCreator
{

   public final static String id = "mission";
   private final ToolBrokerAPI tbi;

   public MissionQuerySelecterCreator(ToolBrokerAPI tbi)
   {
      this.tbi = tbi;
   }

   @Override
   public String getId()
   {
      return id;
   }

   @Override
   public QuerySelecter createQuerySelecter(QuerySelecterUsage querySelectorUsage)
   {
      return new MissionQuerySelecter(tbi, id, querySelectorUsage);
   }
}
