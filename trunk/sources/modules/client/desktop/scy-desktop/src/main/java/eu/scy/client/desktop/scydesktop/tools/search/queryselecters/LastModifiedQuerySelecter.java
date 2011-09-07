/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQuery;

/**
 *
 * @author SikkenJ
 */
public class LastModifiedQuerySelecter extends AbstractSimpleQuerySelecter
{

   private static final Logger logger = Logger.getLogger(LastModifiedQuerySelecter.class);
   private final static long millisInDay = 24 * 60 * 60 * 1000;
   private final static long millisInWeek = 7 * millisInDay;
   private final IMetadataKey lastModifiedKey;

   private class DateMillis
   {

      final long nowMillis;
      final long startOfDayMillis;
      final long startOfWeekMillis;

      DateMillis(long millis)
      {
         nowMillis = millis;
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(millis);
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
         startOfDayMillis = calendar.getTimeInMillis();
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         startOfWeekMillis = startOfDayMillis - (dayOfWeek - Calendar.SUNDAY) * millisInDay;
      }
   }

   private enum LastModifiedOptions
   {

      TODAY,
      YESTERDAY,
      THIS_WEEK,
      LAST_WEEK,
      SAME_DAY,
      SAME_WEEK,
      OLDER_THEN,
      NEWER_THEN;
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
//            displayOptions.add(LastModifiedOptions.OLDER.toString());
            break;
         case ELO_BASED:
            if (getBasedOnElo().getDateLastModified() != null)
            {
               displayOptions.add(LastModifiedOptions.SAME_DAY.toString());
               displayOptions.add(LastModifiedOptions.SAME_WEEK.toString());
               displayOptions.add(LastModifiedOptions.OLDER_THEN.toString());
               displayOptions.add(LastModifiedOptions.NEWER_THEN.toString());
            }
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
   public void setFilterOptions(IQuery query)
   {
      LastModifiedOptions lastModifiedOptions = getSelectedEnum(LastModifiedOptions.class);
      if (lastModifiedOptions != null)
      {
         DateMillis dateMillis;
         switch (getQuerySelectorUsage())
         {
            case TEXT:
               dateMillis = new DateMillis(System.currentTimeMillis());
               break;
            case ELO_BASED:
               dateMillis = new DateMillis(getBasedOnElo().getDateLastModified());
               break;
            default:
               logger.warn("unexpected query selector: " + getQuerySelectorUsage());
               return;
         }
         Long startTime = null;
         Long endTime = null;
         switch (lastModifiedOptions)
         {
            case TODAY:
            case SAME_DAY:
               startTime = dateMillis.startOfDayMillis;
               endTime = dateMillis.startOfDayMillis + millisInDay;
               break;
            case YESTERDAY:
               startTime = dateMillis.startOfDayMillis - millisInDay;
               endTime = dateMillis.startOfDayMillis;
               break;
            case THIS_WEEK:
            case SAME_WEEK:
               startTime = dateMillis.startOfWeekMillis;
               endTime = dateMillis.startOfWeekMillis + millisInWeek;
               break;
            case LAST_WEEK:
               startTime = dateMillis.startOfWeekMillis - millisInWeek;
               endTime = dateMillis.startOfDayMillis;
               break;
            case OLDER_THEN:
               endTime = dateMillis.nowMillis;
               break;
            case NEWER_THEN:
               startTime = dateMillis.nowMillis;
               break;
         }
         query.setDateLastModifiedFilter(startTime, endTime);
      }
   }
}
