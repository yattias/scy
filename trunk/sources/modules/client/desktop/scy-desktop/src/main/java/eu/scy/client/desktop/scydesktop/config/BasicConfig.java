/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterWindowContentCreators;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikkenj
 */
public class BasicConfig implements Config
{
   private IRepository repository;
   private IExtensionManager extensionManager;
   private IMetadataTypeManager metadataTypeManager;
   private IELOFactory eloFactory;
   private IMetadataKey titleKey;
   private MissionModelCreator missionModelCreator;
   private RegisterWindowContentCreators[] registerWindowContentCreators;

   @Override
   public IELOFactory getEloFactory()
   {
      return eloFactory;
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
      this.eloFactory = eloFactory;
   }

   @Override
   public IExtensionManager getExtensionManager()
   {
      return extensionManager;
   }

   public void setExtensionManager(IExtensionManager extensionManager)
   {
      this.extensionManager = extensionManager;
   }

   @Override
   public IMetadataTypeManager getMetadataTypeManager()
   {
      return metadataTypeManager;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
   }

   @Override
   public MissionModelCreator getMissionModelCreator()
   {
      return missionModelCreator;
   }

   public void setMissionModelCreator(MissionModelCreator missionModelCreator)
   {
      this.missionModelCreator = missionModelCreator;
   }

   @Override
   public RegisterWindowContentCreators[] getRegisterWindowContentCreators()
   {
      return registerWindowContentCreators;
   }

   public void setRegisterWindowContentCreators(RegisterWindowContentCreators[] registerWindowContentCreators)
   {
      this.registerWindowContentCreators = registerWindowContentCreators;
   }

   @Override
   public IRepository getRepository()
   {
      return repository;
   }

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   @Override
   public IMetadataKey getTitleKey()
   {
      return titleKey;
   }

   public void setTitleKey(IMetadataKey titleKey)
   {
      this.titleKey = titleKey;
   }
}
