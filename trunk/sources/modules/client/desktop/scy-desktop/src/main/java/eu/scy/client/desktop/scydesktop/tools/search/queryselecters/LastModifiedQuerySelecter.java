/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.search.AndQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class LastModifiedQuerySelecter implements QuerySelecter
{

   private final static String selectedOptionTagName = "selectedOption";
   private final IMetadataKey lastModifiedKey;
   private final static String today = "today";
   private final static String thisWeek = "this week";
   private final static String lastWeek = "last week";
   private List<String> displayOptions;
   private String selectedOption = "";

   public LastModifiedQuerySelecter(IMetadataKey lastModifiedKey)
   {
      this.lastModifiedKey = lastModifiedKey;
   }

   @Override
   public String getId()
   {
      return LastModifiedQuerySelecterCreator.id;
   }

   @Override
   public void setBasedOnElo(ScyElo elo)
   {
      throw new UnsupportedOperationException("Not supported yet.");
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
   public List<String> getDisplayOptions()
   {
      if (displayOptions == null)
      {
         displayOptions = new ArrayList<String>();
         displayOptions.add(today);
         displayOptions.add(thisWeek);
         displayOptions.add(lastWeek);
      }
      return displayOptions;
   }

   @Override
   public String getSelectedOption()
   {
      return selectedOption;
   }

   @Override
   public void setSelectedOption(String option)
   {
      this.selectedOption = option;
   }

   @Override
   public void addState(Element root)
   {
      root.addContent(JDomConversionUtils.createElement(selectedOptionTagName, selectedOption));
   }

   @Override
   public void setState(Element root)
   {
      selectedOption = root.getChildTextTrim(selectedOptionTagName);
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      long today0_00Milis = calendar.getTimeInMillis();
      calendar.set(Calendar.DAY_OF_WEEK, 0);
      long beginOfWeekMillis = calendar.getTimeInMillis();
      if (today.equals(selectedOption))
      {
         return new MetadataQueryComponent(lastModifiedKey, SearchOperation.GREATER_OR_EQUAL, today0_00Milis);
      }
      else if (thisWeek.equals(selectedOption))
      {
         IQueryComponent olderThenTodayQuery = new MetadataQueryComponent(lastModifiedKey, SearchOperation.LESS, today0_00Milis);
         IQueryComponent fromThisWeekQuery = new MetadataQueryComponent(lastModifiedKey, SearchOperation.GREATER_OR_EQUAL, beginOfWeekMillis);
         return new AndQuery(olderThenTodayQuery,fromThisWeekQuery);
      }
      return null;
   }
}
