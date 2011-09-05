/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQuery;
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
      return "format";
   }

   @Override
   public void setFilterOptions(IQuery query)
   {
      TechnicalFormatOptions technicalFormatOption = getSelectedEnum(TechnicalFormatOptions.class);
      if (technicalFormatOption != null)
      {
         Set<String> allowedTypes = new HashSet<String>();
         Set<String> notAllowedTypes = new HashSet<String>();
         switch (technicalFormatOption)
         {
            case SAME:
               allowedTypes.add(getBasedOnElo().getTechnicalFormat());
               break;
            case NOT_SAME:
               notAllowedTypes.add(getBasedOnElo().getTechnicalFormat());
               break;
         }
         query.setIncludedEloTypes(allowedTypes);
         query.setExcludedEloTypes(notAllowedTypes);
      }
   }
}
