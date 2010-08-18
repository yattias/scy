/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.config.DisplayNames;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public class BasicEloToolConfig implements EloToolConfig {

   private String eloType;
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
   private List<String> logicalTypeNames;
   private List<String> functionalTypeNames;

   @Override
   public String toString()
   {
      return "eloType=" + eloType + ", contentCreatorId=" + contentCreatorId + ", topDrawerCreatorId=" + topDrawerCreatorId
         + ", rightDrawerCreatorId=" + rightDrawerCreatorId + ", bottomDrawerCreatorId=" + bottomDrawerCreatorId
         + ", leftDrawerCreatorId=" + leftDrawerCreatorId;
   }

   public BasicEloToolConfig()
   {
   }

   public BasicEloToolConfig(BasicEloToolConfig eloToolConfig)
   {
      eloType = eloToolConfig.eloType;
      contentCreatorId = eloToolConfig.contentCreatorId;
      topDrawerCreatorId = eloToolConfig.topDrawerCreatorId;
      rightDrawerCreatorId = eloToolConfig.rightDrawerCreatorId;
      bottomDrawerCreatorId = eloToolConfig.bottomDrawerCreatorId;
      leftDrawerCreatorId = eloToolConfig.leftDrawerCreatorId;
      contentCollaboration = eloToolConfig.contentCollaboration;
      topDrawerCollaboration = eloToolConfig.topDrawerCollaboration;
      rightDrawerCollaboration = eloToolConfig.rightDrawerCollaboration;
      bottomDrawerCollaboration = eloToolConfig.bottomDrawerCollaboration;
      leftDrawerCollaboration = eloToolConfig.leftDrawerCollaboration;
      if (eloToolConfig.logicalTypeNames != null)
      {
         logicalTypeNames = new ArrayList<String>(eloToolConfig.logicalTypeNames);
      }
      if (eloToolConfig.functionalTypeNames != null)
      {
         functionalTypeNames = new ArrayList<String>(eloToolConfig.functionalTypeNames);
      }
   }

   @Override
   public BasicEloToolConfig clone() throws CloneNotSupportedException
   {
      BasicEloToolConfig clone = (BasicEloToolConfig) super.clone();
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
               throw new IllegalArgumentException("Type name defined in " + label + " in eloConfig " + eloType + " is not defined: " + typeName);
            }
         }
      }
   }

   public String getEloType()
   {
      return eloType;
   }

   public void setEloType(String eloType)
   {
      this.eloType = eloType;
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
