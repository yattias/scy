/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.AndQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class LastModifiedQuerySelecter extends AbstractSimpleQuerySelecter
{

   private final IMetadataKey lastModifiedKey;

   private enum LastModifiedOptions
   {

      TODAY,
      YESTERDAY,
      THIS_WEEK,
      LAST_WEEK,
      SAME_DAY,
      SAME_WEEK,
      BEFORE,
      AFTER;
   }

   public LastModifiedQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.lastModifiedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
   }

   @Override
   protected List<String> createDisplayOption()
   {
      List<String> displayOptions = new ArrayList<String>();
      switch (getQuerySelectorUsage())
      {
         case TEXT:
            displayOptions.add(LastModifiedOptions.TODAY.toString());
            displayOptions.add(LastModifiedOptions.YESTERDAY.toString());
            displayOptions.add(LastModifiedOptions.THIS_WEEK.toString());
            displayOptions.add(LastModifiedOptions.LAST_WEEK.toString());
            break;
         case ELO_BASED:
            displayOptions.add(LastModifiedOptions.SAME_DAY.toString());
            displayOptions.add(LastModifiedOptions.SAME_WEEK.toString());
            displayOptions.add(LastModifiedOptions.BEFORE.toString());
            displayOptions.add(LastModifiedOptions.AFTER.toString());
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "unknown";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "Last modified";
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      long today0_00Milis = calendar.getTimeInMillis();
      calendar.set(Calendar.DAY_OF_WEEK, 0);
      long beginOfWeekMillis = calendar.getTimeInMillis();
      LastModifiedOptions lastModifiedOptions = LastModifiedOptions.valueOf(getSelectedOption());
      switch (lastModifiedOptions)
      {
         case TODAY:
            return new MetadataQueryComponent(lastModifiedKey, SearchOperation.GREATER_OR_EQUAL, today0_00Milis);
         case YESTERDAY:
            break;
         case THIS_WEEK:
            IQueryComponent olderThenTodayQuery = new MetadataQueryComponent(lastModifiedKey, SearchOperation.LESS, today0_00Milis);
            IQueryComponent fromThisWeekQuery = new MetadataQueryComponent(lastModifiedKey, SearchOperation.GREATER_OR_EQUAL, beginOfWeekMillis);
            return new AndQuery(olderThenTodayQuery, fromThisWeekQuery);
         case LAST_WEEK:
            break;
         case SAME_DAY:
         case SAME_WEEK:
         case BEFORE:
         case AFTER:
      }
      return null;
   }
}
