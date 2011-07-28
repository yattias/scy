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
public class TechnicalFormatQuerySelector extends AbstractSimpleQuerySelecter
{

   private final IMetadataKey technicalFormatKey;

   private enum TechnicalFormatOptions
   {

      SAME,
      NOT_SAME;
   }

   public TechnicalFormatQuerySelector(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.technicalFormatKey = getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   @Override
   protected List<String> createDisplayOptions()
   {
      List<String> displayOptions = new ArrayList<String>();
      for (TechnicalFormatOptions technicalFormatOption : TechnicalFormatOptions.values())
      {
         displayOptions.add(technicalFormatOption.toString());
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "report";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "technical format";
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      TechnicalFormatOptions technicalFormatOption = TechnicalFormatOptions.valueOf(getSelectedOption());
      switch (technicalFormatOption)
      {
         case SAME:
            return new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, getBasedOnElo().getTechnicalFormat());
         case NOT_SAME:
            return new MetadataQueryComponent(technicalFormatKey, SearchOperation.NOT_EQUALS, getBasedOnElo().getTechnicalFormat());
      }
      return null;
   }
}
