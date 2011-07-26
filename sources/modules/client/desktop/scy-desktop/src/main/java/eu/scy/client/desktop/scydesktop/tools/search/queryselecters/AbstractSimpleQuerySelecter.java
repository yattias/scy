/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 *
 * @author SikkenJ
 */
public abstract class AbstractSimpleQuerySelecter implements QuerySelecter
{

   private final static String selectedOptionTagName = "selectedOption";
   private final String id;
   private List<String> displayOptions;
   private String selectedOption = "";
   private ScyElo basedOnElo = null;
   private final QuerySelecterUsage querySelectorUsage;
   private final ToolBrokerAPI tbi;

   public AbstractSimpleQuerySelecter(ToolBrokerAPI tbi,String id,QuerySelecterUsage querySelectorUsage)
   {
      this.tbi = tbi;
      this.id = id;
      this.querySelectorUsage = querySelectorUsage;
   }

   @Override
   public String getId()
   {
      return id;
   }

   public QuerySelecterUsage getQuerySelectorUsage()
   {
      return querySelectorUsage;
   }

   public ToolBrokerAPI getTbi()
   {
      return tbi;
   }

   public IMetadataKey getMetadataKey(IMetadataKeyIdDefinition keyId){
      return tbi.getMetaDataTypeManager().getMetadataKey(keyId);
   }

   @Override
   public void setBasedOnElo(ScyElo elo)
   {
      this.basedOnElo = elo;
   }

   public ScyElo getBasedOnElo()
   {
      return basedOnElo;
   }

   @Override
   public List<String> getDisplayOptions()
   {
      if (displayOptions == null)
      {
         displayOptions = createDisplayOption();
      }
      return displayOptions;
   }

   protected abstract List<String> createDisplayOption();

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
}
