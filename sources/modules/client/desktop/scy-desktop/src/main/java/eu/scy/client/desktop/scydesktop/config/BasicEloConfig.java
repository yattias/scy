/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import java.util.ArrayList;
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
   private boolean contentCollaboration = false;
   private boolean topDrawerCollaboration = false;
   private boolean rightDrawerCollaboration = false;
   private boolean bottomDrawerCollaboration = false;
   private boolean leftDrawerCollaboration = false;
   private boolean contentStatic = false;
   private List<String> logicalTypeNames;
   private List<String> functionalTypeNames;

   @Override
   public String toString()
   {
      return "type=" + type + "display=" + display + "creatable=" + creatable + ", contentCreatorId=" + contentCreatorId + ", topDrawerCreatorId=" + topDrawerCreatorId
         + ", rightDrawerCreatorId=" + rightDrawerCreatorId + ", bottomDrawerCreatorId=" + bottomDrawerCreatorId
         + ", leftDrawerCreatorId=" + leftDrawerCreatorId;
   }

   public BasicEloConfig()
   {
   }

   public BasicEloConfig(BasicEloConfig eloConfig)
   {
      type = eloConfig.type;
      display = eloConfig.display;
      creatable = eloConfig.creatable;
      contentCreatorId = eloConfig.contentCreatorId;
      topDrawerCreatorId = eloConfig.topDrawerCreatorId;
      rightDrawerCreatorId = eloConfig.rightDrawerCreatorId;
      bottomDrawerCreatorId = eloConfig.bottomDrawerCreatorId;
      leftDrawerCreatorId = eloConfig.leftDrawerCreatorId;
      contentCollaboration = eloConfig.contentCollaboration;
      topDrawerCollaboration = eloConfig.topDrawerCollaboration;
      rightDrawerCollaboration = eloConfig.rightDrawerCollaboration;
      bottomDrawerCollaboration = eloConfig.bottomDrawerCollaboration;
      leftDrawerCollaboration = eloConfig.leftDrawerCollaboration;
      contentStatic = eloConfig.contentStatic;
      if (eloConfig.logicalTypeNames != null)
      {
         logicalTypeNames = new ArrayList<String>(eloConfig.logicalTypeNames);
      }
      if (eloConfig.functionalTypeNames != null)
      {
         functionalTypeNames = new ArrayList<String>(eloConfig.functionalTypeNames);
      }
   }

   @Override
   public BasicEloConfig clone() throws CloneNotSupportedException
   {
      BasicEloConfig clone = (BasicEloConfig) super.clone();
      if (logicalTypeNames != null)
      {
         clone.logicalTypeNames = new ArrayList<String>(logicalTypeNames);
      }
      if (functionalTypeNames != null)
      {
         clone.functionalTypeNames = new ArrayList<String>(functionalTypeNames);
      }
      return clone;
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
   public boolean isBottomDrawerCollaboration()
   {
      return bottomDrawerCollaboration;
   }

   public void setBottomDrawerCollaboration(boolean bottomDrawerCollaboration)
   {
      this.bottomDrawerCollaboration = bottomDrawerCollaboration;
   }

   @Override
   public boolean isContentCollaboration()
   {
      return contentCollaboration;
   }

   public void setContentCollaboration(boolean contentCollaboration)
   {
      this.contentCollaboration = contentCollaboration;
   }

   @Override
   public boolean isLeftDrawerCollaboration()
   {
      return leftDrawerCollaboration;
   }

   public void setLeftDrawerCollaboration(boolean leftDrawerCollaboration)
   {
      this.leftDrawerCollaboration = leftDrawerCollaboration;
   }

   @Override
   public boolean isRightDrawerCollaboration()
   {
      return rightDrawerCollaboration;
   }

   public void setRightDrawerCollaboration(boolean rightDrawerCollaboration)
   {
      this.rightDrawerCollaboration = rightDrawerCollaboration;
   }

   @Override
   public boolean isTopDrawerCollaboration()
   {
      return topDrawerCollaboration;
   }

   public void setTopDrawerCollaboration(boolean topDrawerCollaboration)
   {
      this.topDrawerCollaboration = topDrawerCollaboration;
   }

   @Override
   public boolean isContentStatic()
   {
      return contentStatic;
   }

   public void setContentStatic(boolean contentStatic)
   {
      this.contentStatic = contentStatic;
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
