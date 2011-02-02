/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl;

import java.util.ArrayList;
import java.util.List;

import eu.scy.common.mission.EloSystemRole;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.EloLogicalRole;

/**
 *
 * @author SikkenJ
 */
public class BasicEloToolConfig implements EloToolConfig {

   private String eloType;
   private EloSystemRole eloSystemRole = EloSystemRole.USER;
   private String contentCreatorId;
   private String topDrawerCreatorId;
   private String rightDrawerCreatorId;
   private String bottomDrawerCreatorId;
   private String leftDrawerCreatorId;
   private Boolean contentCollaboration;
   private Boolean topDrawerCollaboration;
   private Boolean rightDrawerCollaboration;
   private Boolean bottomDrawerCollaboration;
   private Boolean leftDrawerCollaboration;
   private Boolean contentStatic;
   private List<EloLogicalRole> eloLogicalRoles;
   private List<EloFunctionalRole> eloFunctionalRoles;

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

   public BasicEloToolConfig(EloToolConfig eloToolConfig)
   {
      eloType = eloToolConfig.getEloType();
      eloSystemRole = eloToolConfig.getEloSystemRole();
      contentCreatorId = eloToolConfig.getContentCreatorId();
      topDrawerCreatorId = eloToolConfig.getTopDrawerCreatorId();
      rightDrawerCreatorId = eloToolConfig.getRightDrawerCreatorId();
      bottomDrawerCreatorId = eloToolConfig.getBottomDrawerCreatorId();
      leftDrawerCreatorId = eloToolConfig.getLeftDrawerCreatorId();
      contentCollaboration = eloToolConfig.isContentCollaboration();
      topDrawerCollaboration = eloToolConfig.isTopDrawerCollaboration();
      rightDrawerCollaboration = eloToolConfig.isRightDrawerCollaboration();
      bottomDrawerCollaboration = eloToolConfig.isBottomDrawerCollaboration();
      leftDrawerCollaboration = eloToolConfig.isLeftDrawerCollaboration();
      contentStatic = eloToolConfig.isContentStatic();
      if (eloToolConfig.getEloLogicalRoles() != null)
      {
         eloLogicalRoles = new ArrayList<EloLogicalRole>(eloToolConfig.getEloLogicalRoles());
      }
      if (eloToolConfig.getEloFunctionalRoles() != null)
      {
         eloFunctionalRoles = new ArrayList<EloFunctionalRole>(eloToolConfig.getEloFunctionalRoles());
      }
   }

   @Override
   public BasicEloToolConfig clone() throws CloneNotSupportedException
   {
      BasicEloToolConfig clone = (BasicEloToolConfig) super.clone();
      if (eloLogicalRoles != null)
      {
         clone.eloLogicalRoles = new ArrayList<EloLogicalRole>(eloLogicalRoles);
      }
      if (eloFunctionalRoles != null)
      {
         clone.eloFunctionalRoles = new ArrayList<EloFunctionalRole>(eloFunctionalRoles);
      }
      return clone;
   }

   @Override
   public String getEloType()
   {
      return eloType;
   }

   public void setEloType(String eloSysteType)
   {
      this.eloType = eloSysteType;
   }

   @Override
   public EloSystemRole getEloSystemRole()
   {
      return eloSystemRole;
   }

   public void setEloSystemRole(EloSystemRole eloSystemRole)
   {
      this.eloSystemRole = eloSystemRole;
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
   public Boolean isBottomDrawerCollaboration()
   {
      return bottomDrawerCollaboration;
   }

   public void setBottomDrawerCollaboration(Boolean bottomDrawerCollaboration)
   {
      this.bottomDrawerCollaboration = bottomDrawerCollaboration;
   }

   @Override
   public Boolean isContentCollaboration()
   {
      return contentCollaboration;
   }

   public void setContentCollaboration(Boolean contentCollaboration)
   {
      this.contentCollaboration = contentCollaboration;
   }

   @Override
   public Boolean isLeftDrawerCollaboration()
   {
      return leftDrawerCollaboration;
   }

   public void setLeftDrawerCollaboration(Boolean leftDrawerCollaboration)
   {
      this.leftDrawerCollaboration = leftDrawerCollaboration;
   }

   @Override
   public Boolean isRightDrawerCollaboration()
   {
      return rightDrawerCollaboration;
   }

   public void setRightDrawerCollaboration(Boolean rightDrawerCollaboration)
   {
      this.rightDrawerCollaboration = rightDrawerCollaboration;
   }

   @Override
   public Boolean isTopDrawerCollaboration()
   {
      return topDrawerCollaboration;
   }

   public void setTopDrawerCollaboration(Boolean topDrawerCollaboration)
   {
      this.topDrawerCollaboration = topDrawerCollaboration;
   }

   @Override
   public List<EloLogicalRole> getEloLogicalRoles()
   {
      return eloLogicalRoles;
   }

   public void setEloLogicalRoles(List<EloLogicalRole> eloLogicalRoles)
   {
      this.eloLogicalRoles = eloLogicalRoles;
   }

   public void setEloLogicalRoleValues(List<String> eloLogicalRoles)
   {
      this.eloLogicalRoles = JDomConversionUtils.convertToEnums(EloLogicalRole.class, eloLogicalRoles);
   }

   @Override
   public List<EloFunctionalRole> getEloFunctionalRoles()
   {
      return eloFunctionalRoles;
   }

   public void setEloFunctionalRoles(List<EloFunctionalRole> eloFunctionalRoles)
   {
      this.eloFunctionalRoles = eloFunctionalRoles;
   }

   public void setEloFunctionalRoleValues(List<String> eloFunctionalRoles)
   {
      this.eloFunctionalRoles = JDomConversionUtils.convertToEnums(EloFunctionalRole.class, eloFunctionalRoles);
   }

   public void setContentStatic(Boolean contentStatic)
   {
      this.contentStatic = contentStatic;
   }

   @Override
   public Boolean isContentStatic()
   {
      return contentStatic;
   }

}
