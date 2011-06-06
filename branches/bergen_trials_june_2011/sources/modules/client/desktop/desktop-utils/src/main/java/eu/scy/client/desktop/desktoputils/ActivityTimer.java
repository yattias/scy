/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

/**
 *
 * @author SikkenJ
 */
import java.util.ArrayList;
import org.apache.log4j.Logger;

public final class ActivityTimer
{

   private static final Logger logger = Logger.getLogger(ActivityTimer.class);
   private static final String spaces = "                                                                                             ";
   private static final int valueLength = 7;
   private long startMillis = System.currentTimeMillis();
   private long startNanos = System.nanoTime();
   private ArrayList<TimeInfo> timeInfos = new ArrayList<TimeInfo>();

   private class TimeInfo
   {

      long started;
      long ended;
      String activity;

      public TimeInfo(String activity, long started)
      {
         super();
         this.activity = activity;
         this.started = started;
         ended = getMillis();
      }

      public long getStarted()
      {
         return started;
      }

      public long getEnded()
      {
         return ended;
      }

      public long getUsed()
      {
         return ended - started;
      }

      public String getLabel()
      {
         return activity;
      }
   }
   private String name;
   private boolean autoLog = true;
   private boolean activityRunning = false;

   private long getMillis()
   {
      @SuppressWarnings("unused")
      long millis = System.currentTimeMillis() - startMillis;
      long nanoMillis = (System.nanoTime() - startNanos) / 1000000;
//		System.err.println("millis: " + millis + ", nanoMillis: " + nanoMillis + ", div: " + (millis-nanoMillis));
      return nanoMillis;
   }

   public ActivityTimer(String name)
   {
      this.name = name;
   }

   public ActivityTimer(String name, String activity)
   {
      this.name = name;
      startActivity(activity);
   }

   public void reset()
   {
      startMillis = System.currentTimeMillis();
      startNanos = System.nanoTime();
      timeInfos.clear();
   }

   public boolean isAutoLog()
   {
      return autoLog;
   }

   public void setAutoLog(boolean autoLog)
   {
      this.autoLog = autoLog;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void startActivity(String activity)
   {
      long started = getMillis();
      if (activityRunning)
      {
         endActivity(false);
         started = timeInfos.get(timeInfos.size() - 1).ended;
      }
      activityRunning = true;
      TimeInfo timeInfo = new TimeInfo(activity, started);

      timeInfos.add(timeInfo);

   }

   public void endActivity()
   {
      endActivity(true);
   }

   public void endActivity(boolean logIt)
   {
      if (activityRunning)
      {
         timeInfos.get(timeInfos.size() - 1).ended = getMillis();
         activityRunning = false;
         if (autoLog && logIt)
         {
            logger.info(getTimeList());
         }
      }
   }

   public String getTimeList()
   {
      timeInfos.get(timeInfos.size() - 1).ended = getMillis();
      StringBuilder builder = new StringBuilder();
      builder.append("Time used for ");
      builder.append(name);
      builder.append("\n");
      long totalMillis = 0;
      boolean firstTime = true;
      for (TimeInfo timeInfo : timeInfos)
      {
         if (firstTime)
         {
            firstTime = false;
         }
         else
         {
            builder.append("\n");
         }
         addLong(builder, totalMillis);
         builder.append(" ");
         addLong(builder, timeInfo.getUsed());
         builder.append(" ");
         builder.append(timeInfo.getLabel());
         totalMillis += timeInfo.getUsed();
      }
      if (timeInfos.size() > 1)
      {
         builder.append("\n");
         addLong(builder, totalMillis);
      }
      return builder.toString();
   }

   private void addLong(StringBuilder builder, long value)
   {
      String stringValue = "" + value;
      int nrOfSpaces = valueLength - stringValue.length();
      if (nrOfSpaces<0){
         nrOfSpaces = 0;
      }
      builder.append(spaces.substring(0, nrOfSpaces));
      builder.append(stringValue);
   }
}
