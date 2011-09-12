package eu.scy.common.mission.impl;

import org.apache.log4j.Logger;

import eu.scy.common.mission.EloToolConfig;

public class ApplyEloToolConfigDefaults
{

   private final static Logger logger = Logger.getLogger(ApplyEloToolConfigDefaults.class);
   public final static String defaultEloToolConfigType = "defaultEloToolConfig";
   private EloToolConfig defaultEloToolConfig;

   public void setDefaultEloToolConfig(final EloToolConfig defaultEloToolConfig)
   {
      BasicEloToolConfig basicEloConfig;
      if (defaultEloToolConfig != null)
      {
         basicEloConfig = new BasicEloToolConfig(defaultEloToolConfig);
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
      this.defaultEloToolConfig = basicEloConfig;
   }

   public EloToolConfig applyDefaults(final EloToolConfig eloToolConfig)
   {
      if (eloToolConfig == null)
      {
         return null;
      }
      if (defaultEloToolConfig == null)
      {
         logger.warn("cannot apply defaults, because there is no default");
         return eloToolConfig;
      }
      final BasicEloToolConfig basicEloConfig = new BasicEloToolConfig(eloToolConfig);

      if (isEmpty(basicEloConfig.getTopDrawerCreatorId()))
      {
         basicEloConfig.setTopDrawerCreatorId(defaultEloToolConfig.getTopDrawerCreatorId());
      }
      if (isEmpty(basicEloConfig.getRightDrawerCreatorId()))
      {
         basicEloConfig.setRightDrawerCreatorId(defaultEloToolConfig.getRightDrawerCreatorId());
      }
      if (isEmpty(basicEloConfig.getBottomDrawerCreatorId()))
      {
         basicEloConfig.setBottomDrawerCreatorId(defaultEloToolConfig.getBottomDrawerCreatorId());
      }
      if (isEmpty(basicEloConfig.getLeftDrawerCreatorId()))
      {
         basicEloConfig.setLeftDrawerCreatorId(defaultEloToolConfig.getLeftDrawerCreatorId());
      }
      if (isEmpty(basicEloConfig.getTopDrawerCreatorId()))
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
      return basicEloConfig;
   }

   private boolean isEmpty(String string)
   {
      return string == null || string.isEmpty();
   }
}
