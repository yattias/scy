/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class TechnicalFormatQuerySelector implements QuerySelecter
{

   private final static String selectedOptionTagName = "selectedOption";
   private ScyElo basedOnElo;
   private final static String sameFormat = "same";
   private final static String otherFormat = "other";
   private List<String> displayOptions;
   private String selectedOption = "";
   private final IMetadataKey technicalFormatKey;

   public TechnicalFormatQuerySelector(IMetadataKey technicalFormatKey)
   {
      this.technicalFormatKey = technicalFormatKey;
   }

   @Override
   public String getId()
   {
      return TechnicalFormatQuerySelecterCreator.id;
   }

   @Override
   public void setBasedOnElo(ScyElo elo)
   {
      this.basedOnElo = elo;
   }

   @Override
   public String getEloIconName()
   {
      return "unknown";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "Technical format";
   }

   @Override
   public List<String> getDisplayOptions()
   {
      if (displayOptions == null)
      {
         displayOptions = new ArrayList<String>();
         displayOptions.add(sameFormat);
         displayOptions.add(otherFormat);
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
      if (sameFormat.equals(selectedOption))
      {
         return new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, basedOnElo.getTechnicalFormat());
      }
      else if (otherFormat.equals(selectedOption))
      {
         return new MetadataQueryComponent(technicalFormatKey, SearchOperation.NOT_EQUALS, basedOnElo.getTechnicalFormat());
      }
      return null;
   }
}
