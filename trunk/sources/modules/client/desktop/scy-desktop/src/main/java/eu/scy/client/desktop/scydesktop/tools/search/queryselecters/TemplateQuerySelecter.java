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
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public abstract class TemplateQuerySelecter extends AbstractSimpleQuerySelecter
{

   private final IMetadataKey templateKey;

   private enum TemplateOptions
   {

      NONE,
      ONLY,
      SAME,
      NOT_SAME;
   }

   public TemplateQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.templateKey = getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE);
   }

   @Override
   protected List<String> createDisplayOptions()
   {
      List<String> displayOptions = new ArrayList<String>();
      switch (getQuerySelectorUsage())
      {
         case TEXT:
            displayOptions.add(TemplateOptions.NONE.toString());
            displayOptions.add(TemplateOptions.ONLY.toString());
            break;
         case ELO_BASED:
            displayOptions.add(TemplateOptions.SAME.toString());
            displayOptions.add(TemplateOptions.NOT_SAME.toString());
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "exp_design";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "templates";
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      TemplateOptions technicalFormatOption = TemplateOptions.valueOf(getSelectedOption());
      switch (technicalFormatOption)
      {
         case NONE:
            return new MetadataQueryComponent(templateKey, SearchOperation.EQUALS, "false");
         case ONLY:
            return new MetadataQueryComponent(templateKey, SearchOperation.EQUALS, "true");
         case SAME:
            return new MetadataQueryComponent(templateKey, SearchOperation.EQUALS, Boolean.toString(getBasedOnElo().getTemplate()));
         case NOT_SAME:
            return new MetadataQueryComponent(templateKey, SearchOperation.NOT_EQUALS, Boolean.toString(getBasedOnElo().getTemplate()));
      }
      return null;
   }
}
