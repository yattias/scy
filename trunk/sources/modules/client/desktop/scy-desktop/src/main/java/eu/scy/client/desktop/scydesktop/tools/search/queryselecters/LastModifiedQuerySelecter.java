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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.AndQuery;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class LastModifiedQuerySelecter extends AbstractSimpleQuerySelecter
{
   private final static long millisInDay = 24*60*60*1000;
   private final static long millisInWeek = 7*millisInDay;

   private final IMetadataKey lastModifiedKey;

   private enum LastModifiedOptions
   {

      TODAY,
      YESTERDAY,
      THIS_WEEK,
      LAST_WEEK,
      SAME_DAY,
      SAME_WEEK,
      OLDER,
      NEWER;
   }

   public LastModifiedQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.lastModifiedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
   }

   @Override
   protected List<String> createDisplayOptions()
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
            displayOptions.add(LastModifiedOptions.OLDER.toString());
            displayOptions.add(LastModifiedOptions.NEWER.toString());
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "orientation";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "date modified";
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
         case OLDER:
         case NEWER:
      }
      return null;
   }

   @Override
   public void setFilterOptions(IQuery query)
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         query.enableDateLastModifiedFilter(false);
      }
      else
      {
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
         long today0_00Milis = calendar.getTimeInMillis();
         calendar.set(Calendar.DAY_OF_WEEK, 0);
         long beginOfWeekMillis = calendar.getTimeInMillis();
         query.enableDateLastModifiedFilter(false);
         Long startTime = null;
         Long endTime = null;
         Set<String> allowedUsers = new HashSet<String>();
         Set<String> notAllowedUsers = new HashSet<String>();
         LastModifiedOptions lastModifiedOptions = LastModifiedOptions.valueOf(getSelectedOption());
         switch (lastModifiedOptions)
         {
            case TODAY:
               startTime = today0_00Milis;
               break;
            case YESTERDAY:
               startTime = today0_00Milis-millisInDay;
               endTime = today0_00Milis;
               break;
            case THIS_WEEK:
               startTime = beginOfWeekMillis;
               break;
            case LAST_WEEK:
               startTime = beginOfWeekMillis-millisInWeek;
               endTime = beginOfWeekMillis;
               break;
            case SAME_DAY:
               break;
            case SAME_WEEK:
               break;
            case OLDER:
               break;
            case NEWER:
               break;
         }
         query.setDateLastModifiedFilter(startTime,endTime);
      }
   }
}
