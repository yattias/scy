/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.impl.BasicEloToolConfig;

/**
 *
 * @author SikkenJ
 */
public class DefaultSettingEloConfigManager implements EloConfigManager
{

   private final EloConfigManager eloConfigManager;
   private final EloToolConfig defaultEloToolConfig;
   private final String defaultEloToolConfigType = "defaultEloToolConfig";

   public DefaultSettingEloConfigManager(EloConfigManager eloConfigManager)
   {
      this.eloConfigManager = eloConfigManager;
      defaultEloToolConfig = findDefaultEloToolConfig();
   }

   private EloToolConfig findDefaultEloToolConfig()
   {
      BasicEloToolConfig basicEloConfig;
      EloToolConfig eloToolConfig = eloConfigManager.getEloToolConfig(defaultEloToolConfigType);
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
      EloToolConfig eloToolConfig = eloConfigManager.getEloToolConfig(eloType);
      if (eloToolConfig == null)
      {
         return null;
      }
      BasicEloToolConfig basicEloConfig = new BasicEloToolConfig(eloToolConfig);
      addDefaults(basicEloConfig);
      return basicEloConfig;
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
         basicEloConfig.setRightDrawerCollaboration(defaultEloToolConfig.isRightDrawerCollaboration());
      }
      if (basicEloConfig.isBottomDrawerCollaboration() == null)
      {
         basicEloConfig.setBottomDrawerCollaboration(defaultEloToolConfig.isBottomDrawerCollaboration());
      }
      if (basicEloConfig.isLeftDrawerCollaboration() == null)
      {
         basicEloConfig.setLeftDrawerCollaboration(defaultEloToolConfig.isLeftDrawerCollaboration());
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

   @Override
   public void addDebugCreatorId(String creatorId)
   {
      eloConfigManager.addDebugCreatorId(creatorId);
   }

   @Override
   public void setDebug(boolean debug)
   {
      eloConfigManager.setDebug(debug);
   }
}
