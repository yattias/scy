/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.List;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;

/**
 *
 * @author SikkenJ
 */
public abstract class SimilarQuerySelector extends AbstractSimpleQuerySelecter
{

   private enum SimilarOptions
   {

      SIMILAR;
   }

   public SimilarQuerySelector(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
   }

   @Override
   protected List<String> createDisplayOptions()
   {
      List<String> displayOptions = new ArrayList<String>();
      for (SimilarOptions similarOptions : SimilarOptions.values())
      {
         displayOptions.add(similarOptions.toString());
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "drawing";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "similar";
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      SimilarOptions similarOption = SimilarOptions.valueOf(getSelectedOption());
      switch (similarOption)
      {
         case SIMILAR:
            StringBuilder sb = new StringBuilder();
            for (String keyword : getBasedOnElo().getKeywords())
            {
               sb.append(keyword);
               sb.append(" ");
            }
            for (String tag : getBasedOnElo().getTagNames())
            {
               sb.append(tag);
               sb.append(" ");
            }
            for (String author : getBasedOnElo().getAuthors())
            {
               sb.append(author);
               sb.append(" ");
            }
            sb.append(getBasedOnElo().getTitle());
            return new MetadataQueryComponent("contents", sb);
      }
      return null;
   }
}
