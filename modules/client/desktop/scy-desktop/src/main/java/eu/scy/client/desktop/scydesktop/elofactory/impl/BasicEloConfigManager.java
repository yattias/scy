/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.config.BasicEloConfig;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.config.EloConfig;
import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sikken
 */
public class BasicEloConfigManager implements EloConfigManager
{

   private Config config;
   private Set<String> debugCreatorIds = new HashSet<String>();
   private boolean debug = false;

   public BasicEloConfigManager(Config config)
   {
      this.config = config;
   }

   @Override
   public EloConfig getEloConfig(String eloType)
   {
      BasicEloConfig eloConfig = config.getEloConfig(eloType);
      if (eloConfig != null)
      {
         BasicEloConfig basicEloConfig = new BasicEloConfig(eloConfig);
         basicEloConfig.setContentCreatorId(getDebugCheckedCreatorId(eloConfig.getContentCreatorId()));
         basicEloConfig.setTopDrawerCreatorId(getDebugCheckedCreatorId(eloConfig.getTopDrawerCreatorId()));
         basicEloConfig.setRightDrawerCreatorId(getDebugCheckedCreatorId(eloConfig.getRightDrawerCreatorId()));
         basicEloConfig.setBottomDrawerCreatorId(getDebugCheckedCreatorId(eloConfig.getBottomDrawerCreatorId()));
         basicEloConfig.setLeftDrawerCreatorId(getDebugCheckedCreatorId(eloConfig.getLeftDrawerCreatorId()));
         return basicEloConfig;
      }
      return eloConfig;
   }

   private String getDebugCheckedCreatorId(String creatorId)
   {
      if (debugCreatorIds.contains(creatorId))
      {
         if (!debug)
         {
            return null;
         }
      }
      return creatorId;
   }

   @Override
   public void addDebugCreatorId(String creatorId)
   {
      debugCreatorIds.add(creatorId);
   }

   public boolean isDebug()
   {
      return debug;
   }

   @Override
   public void setDebug(boolean debug)
   {
      this.debug = debug;
   }
}
