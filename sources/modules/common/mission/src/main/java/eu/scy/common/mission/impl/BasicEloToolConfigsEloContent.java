/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl;

import java.util.HashMap;
import java.util.List;

import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.EloToolConfigsEloContent;

/**
 * 
 * @author SikkenJ
 */
public class BasicEloToolConfigsEloContent implements EloToolConfigsEloContent
{
   private final String defaultEloToolConfigType = "defaultEloToolConfig";

   private List<EloToolConfig> eloToolConfigs;
   private HashMap<String, EloToolConfig> eloToolConfigsMap = new HashMap<String, EloToolConfig>();
   private EloToolConfig defaultEloToolConfig;

   @Override
   public String toString()
   {
      return "BasicEloToolConfigsEloContent{" + "eloToolConfigs=" + eloToolConfigs + '}';
   }

   @Override
   public List<EloToolConfig> getEloToolConfigs()
   {
      return eloToolConfigs;
   }

   @Override
   public void setEloToolConfigs(List<EloToolConfig> eloToolConfigs)
   {
      this.eloToolConfigs = eloToolConfigs;
      eloToolConfigsMap.clear();
      for (EloToolConfig eloToolConfig : eloToolConfigs)
      {
         eloToolConfigsMap.put(eloToolConfig.getEloType(), eloToolConfig);
      }
      defaultEloToolConfig = findDefaultEloToolConfig();
   }

   private EloToolConfig findDefaultEloToolConfig()
   {
      BasicEloToolConfig basicEloConfig;
      EloToolConfig eloToolConfig = eloToolConfigsMap.get(defaultEloToolConfigType);
      if (eloToolConfig != null)
      {
         basicEloConfig = new BasicEloToolConfig(eloToolConfig);
      }
      else
      {
         basicEloConfig = new BasicEloToolConfig();
      }
      // set default boolean values, if they are not set
      if (basicEloConfig.isTopDrawerCollaboration() == null)
      {
         basicEloConfig.setTopDrawerCollaboration(false);
      }
      if (basicEloConfig.isRightDrawerCollaboration() == null)
      {
         basicEloConfig.setRightDrawerCollaboration(false);
      }
      if (basicEloConfig.isBottomDrawerCollaboration() == null)
      {
         basicEloConfig.setBottomDrawerCollaboration(false);
      }
      if (basicEloConfig.isLeftDrawerCollaboration() == null)
      {
         basicEloConfig.setLeftDrawerCollaboration(false);
      }
      if (basicEloConfig.isContentCollaboration() == null)
      {
         basicEloConfig.setContentCollaboration(false);
      }
      if (basicEloConfig.isContentStatic() == null)
      {
         basicEloConfig.setContentStatic(false);
      }
      return basicEloConfig;
   }

   @Override
   public EloToolConfig getEloToolConfig(String eloType)
   {
      EloToolConfig eloToolConfig = eloToolConfigsMap.get(eloType);
      if (eloToolConfig != null)
      {
         BasicEloToolConfig basicEloConfig = new BasicEloToolConfig(eloToolConfig);
         addDefaults(basicEloConfig);
         eloToolConfig = basicEloConfig;
      }
      return eloToolConfig;
   }

   private void addDefaults(BasicEloToolConfig basicEloConfig)
   {
      if (basicEloConfig.getTopDrawerCreatorId() == null)
      {
         basicEloConfig.setTopDrawerCreatorId(defaultEloToolConfig.getTopDrawerCreatorId());
      }
      if (basicEloConfig.getRightDrawerCreatorId() == null)
      {
         basicEloConfig.setRightDrawerCreatorId(defaultEloToolConfig.getRightDrawerCreatorId());
      }
      if (basicEloConfig.getBottomDrawerCreatorId() == null)
      {
         basicEloConfig.setBottomDrawerCreatorId(defaultEloToolConfig.getBottomDrawerCreatorId());
      }
      if (basicEloConfig.getLeftDrawerCreatorId() == null)
      {
         basicEloConfig.setLeftDrawerCreatorId(defaultEloToolConfig.getLeftDrawerCreatorId());
      }
      if (basicEloConfig.getTopDrawerCreatorId() == null)
      {
         basicEloConfig.setTopDrawerCreatorId(defaultEloToolConfig.getTopDrawerCreatorId());
      }
      if (basicEloConfig.isTopDrawerCollaboration() == null)
      {
         basicEloConfig.setTopDrawerCollaboration(defaultEloToolConfig.isTopDrawerCollaboration());
      }
      if (basicEloConfig.isRightDrawerCollaboration() == null)
      {
         basicEloConfig.setRightDrawerCollaboration(defaultEloToolConfig
                  .isRightDrawerCollaboration());
      }
      if (basicEloConfig.isBottomDrawerCollaboration() == null)
      {
         basicEloConfig.setBottomDrawerCollaboration(defaultEloToolConfig
                  .isBottomDrawerCollaboration());
      }
      if (basicEloConfig.isLeftDrawerCollaboration() == null)
      {
         basicEloConfig
                  .setLeftDrawerCollaboration(defaultEloToolConfig.isLeftDrawerCollaboration());
      }
      if (basicEloConfig.isContentCollaboration() == null)
      {
         basicEloConfig.setContentCollaboration(defaultEloToolConfig.isContentCollaboration());
      }
      if (basicEloConfig.isContentStatic() == null)
      {
         basicEloConfig.setContentStatic(defaultEloToolConfig.isContentStatic());
      }
   }

}
