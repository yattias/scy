/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.EloToolConfigsEloContent;
import eu.scy.common.mission.impl.BasicEloToolConfig;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author SikkenJ
 */
public class BasicEloToolConfigManager implements EloConfigManager {

   private final EloToolConfigsEloContent eloToolConfigsEloContent;
   private Set<String> debugCreatorIds = new HashSet<String>();
   private boolean debug = false;

   public BasicEloToolConfigManager(EloToolConfigsEloContent eloToolConfigsEloContent)
   {
      this.eloToolConfigsEloContent = eloToolConfigsEloContent;
   }

   @Override
   public EloToolConfig getEloToolConfig(String eloType)
   {
      EloToolConfig eloToolConfig = eloToolConfigsEloContent.getEloToolConfig(eloType);
      if (eloToolConfig != null)
      {
         BasicEloToolConfig basicEloConfig = new BasicEloToolConfig(eloToolConfig);
         basicEloConfig.setContentCreatorId(getDebugCheckedCreatorId(eloToolConfig.getContentCreatorId()));
         basicEloConfig.setTopDrawerCreatorId(getDebugCheckedCreatorId(eloToolConfig.getTopDrawerCreatorId()));
         basicEloConfig.setRightDrawerCreatorId(getDebugCheckedCreatorId(eloToolConfig.getRightDrawerCreatorId()));
         basicEloConfig.setBottomDrawerCreatorId(getDebugCheckedCreatorId(eloToolConfig.getBottomDrawerCreatorId()));
         basicEloConfig.setLeftDrawerCreatorId(getDebugCheckedCreatorId(eloToolConfig.getLeftDrawerCreatorId()));
         return basicEloConfig;
      }
      return eloToolConfig;
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
