/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.common.mission.impl.BasicEloToolConfig;
import java.util.List;

/**
 * Just here, so that I don't have to modify all old config files
 *
 * @author sikkenj
 */
public class BasicEloConfig extends BasicEloToolConfig
{
   private String display;
   private boolean creatable;
   private List<String> logicalTypeNames;
   private List<String> functionalTypeNames;

   public void setType(String type)
   {
      setEloType(type);
   }

   public void setCreatable(boolean creatable)
   {
      this.creatable = creatable;
   }

   public boolean isCreatable()
   {
      return creatable;
   }

   public void setDisplay(String display)
   {
      this.display = display;
   }

   public String getDisplay()
   {
      return display;
   }

   public void setFunctionalTypeNames(List<String> functionalTypeNames)
   {
      this.functionalTypeNames = functionalTypeNames;
   }

   public void setLogicalTypeNames(List<String> logicalTypeNames)
   {
      this.logicalTypeNames = logicalTypeNames;
   }


}
