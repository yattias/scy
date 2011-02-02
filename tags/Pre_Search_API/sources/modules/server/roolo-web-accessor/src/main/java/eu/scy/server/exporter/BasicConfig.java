/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.server.exporter;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.configuration.Configuration;

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
   private IMetadataKey technicalFormatKey;
   private Configuration serverConfig;

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

   private IMetadataKey getMetadataKey(CoreRooloMetadataKeyIds keyId){
      IMetadataKey key = metadataTypeManager.getMetadataKey(keyId.getId());
      if (key==null){
         throw new IllegalStateException("cannot find key " + keyId.getId());
      }
      return key;
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
      if (titleKey == null) {
          titleKey = getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      }
      return titleKey;
   }

   public void setTitleKey(IMetadataKey titleKey)
   {
      this.titleKey = titleKey;
   }

   @Override
   public IMetadataKey getTechnicalFormatKey()
   {
      if (technicalFormatKey == null) {
          technicalFormatKey = getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      }
      return technicalFormatKey;
   }

   public void setTechnicalFormatKey(IMetadataKey technicalFormatKey)
   {
      this.technicalFormatKey = technicalFormatKey;
   }
 
    @Override
    public Configuration getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(Configuration serverConfig) {
        this.serverConfig = serverConfig;
    }

}
