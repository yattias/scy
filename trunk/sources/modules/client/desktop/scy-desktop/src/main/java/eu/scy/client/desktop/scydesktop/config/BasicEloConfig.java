/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import java.util.List;

/**
 *
 * @author sikkenj
 */
public class BasicEloConfig implements EloConfig
{

   private String type;
   private String display;
   private boolean creatable = true;
   private String contentCreatorId;
   private String topDrawerCreatorId;
   private String rightDrawerCreatorId;
   private String bottomDrawerCreatorId;
   private String leftDrawerCreatorId;
   private List<String> logicalTypeNames;
   private List<String> functionalTypeNames;

   @Override
   public String toString()
   {
      return "type=" + type + "display=" + display + "creatable=" + creatable + ", contentCreatorId=" + contentCreatorId + ", topDrawerCreatorId=" + topDrawerCreatorId
         + ", rightDrawerCreatorId=" + rightDrawerCreatorId + ", bottomDrawerCreatorId=" + bottomDrawerCreatorId
         + ", leftDrawerCreatorId=" + leftDrawerCreatorId;
   }

   public void checkTypeNames(DisplayNames logicalTypeDisplayNames, DisplayNames functionalTypeDisplayNames)
   {
      checkTyleTypeNames(logicalTypeNames, logicalTypeDisplayNames, "logicalTypeNames");
      checkTyleTypeNames(functionalTypeNames, functionalTypeDisplayNames, "functionalTypeNames");
   }

   private void checkTyleTypeNames(List<String> typeNames, DisplayNames displayNames, String label)
   {
      if (typeNames != null)
      {
         for (String typeName : typeNames)
         {
            if (!displayNames.typeExists(typeName))
            {
               throw new IllegalArgumentException("Type name defined in " + label + " in eloConfig " + type + " is not defined: " + typeName);
            }
         }
      }
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public boolean isCreatable()
   {
      return creatable;
   }

   public void setCreatable(boolean creatable)
   {
      this.creatable = creatable;
   }

   public String getDisplay()
   {
      return display;
   }

   public void setDisplay(String display)
   {
      this.display = display;
   }

   @Override
   public String getBottomDrawerCreatorId()
   {
      return bottomDrawerCreatorId;
   }

   public void setBottomDrawerCreatorId(String bottomDrawerCreatorId)
   {
      this.bottomDrawerCreatorId = bottomDrawerCreatorId;
   }

   @Override
   public String getContentCreatorId()
   {
      return contentCreatorId;
   }

   public void setContentCreatorId(String contentCreatorId)
   {
      this.contentCreatorId = contentCreatorId;
   }

   @Override
   public String getLeftDrawerCreatorId()
   {
      return leftDrawerCreatorId;
   }

   public void setLeftDrawerCreatorId(String leftDrawerCreatorId)
   {
      this.leftDrawerCreatorId = leftDrawerCreatorId;
   }

   @Override
   public String getRightDrawerCreatorId()
   {
      return rightDrawerCreatorId;
   }

   public void setRightDrawerCreatorId(String rightDrawerCreatorId)
   {
      this.rightDrawerCreatorId = rightDrawerCreatorId;
   }

   @Override
   public String getTopDrawerCreatorId()
   {
      return topDrawerCreatorId;
   }

   public void setTopDrawerCreatorId(String topDrawerCreatorId)
   {
      this.topDrawerCreatorId = topDrawerCreatorId;
   }

   @Override
   public List<String> getFunctionalTypeNames()
   {
      return functionalTypeNames;
   }

   public void setFunctionalTypeNames(List<String> functionalTypeNames)
   {
      this.functionalTypeNames = functionalTypeNames;
   }

   @Override
   public List<String> getLogicalTypeNames()
   {
      return logicalTypeNames;
   }

   public void setLogicalTypeNames(List<String> logicalTypeNames)
   {
      this.logicalTypeNames = logicalTypeNames;
   }
}
