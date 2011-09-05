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
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 *
 * @author SikkenJ
 */
public abstract class AbstractSimpleQuerySelecter implements QuerySelecter
{

   private final static Logger logger = Logger.getLogger(AbstractSimpleQuerySelecter.class);
   private final static String selectedOptionTagName = "selectedOption";
   private final String id;
   private List<String> displayOptions;
   private String selectedOption = "";
   private ScyElo basedOnElo = null;
   private final QuerySelecterUsage querySelectorUsage;
   private final ToolBrokerAPI tbi;
   private boolean authorMode = false;
   private boolean debugMode = false;

   public AbstractSimpleQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
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

   public IMetadataKey getMetadataKey(IMetadataKeyIdDefinition keyId)
   {
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
         switch (getQuerySelectorUsage())
         {
            case ELO_BASED:
               if (getBasedOnElo() == null)
               {
                  logger.error("can't call createDisplayOptions, bacause the basedOnElo is null, my id: " + id);
                  break;
               }
            default:
               displayOptions = createDisplayOptions();
         }
      }
      return displayOptions;
   }

   protected abstract List<String> createDisplayOptions();

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

   public <T extends Enum<T>> T getOptionEnum(Class<T> enumType, String option)
   {
      try
      {
         return Enum.valueOf(enumType, option.toUpperCase());
      }
      catch (IllegalArgumentException e)
      {
         return null;
      }
   }

   public <T extends Enum<T>> T getSelectedEnum(Class<T> enumType)
   {
      return getOptionEnum(enumType,getSelectedOption());
   }

   public boolean isAuthorMode()
   {
      return authorMode;
   }

   @Override
   public void setAuthorMode(boolean authorMode)
   {
      this.authorMode = authorMode;
   }

   public boolean isDebugMode()
   {
      return debugMode;
   }

   @Override
   public void setDebugMode(boolean debugMode)
   {
      this.debugMode = debugMode;
   }

}
