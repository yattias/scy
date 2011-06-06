/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.roolows.config;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import java.io.File;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author sikkenj
 */
public class BasicConfig implements Config
{

   private static final Logger logger = Logger.getLogger(BasicConfig.class);
   private IRepository repository;
   private IExtensionManager extensionManager;
   private IMetadataTypeManager metadataTypeManager;
   private IELOFactory eloFactory;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;
   private File loggingDirectory;
   private boolean redirectSystemStreams = false;
   private String backgroundImageFileName;
   private boolean backgroundImageFileNameRelative;
   private Configuration serverConfig;
   private IActionLogger actionLogger;
   private String passwordServiceURL;

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
   public File getLoggingDirectory()
   {
      return loggingDirectory;
   }

   public void setLoggingDirectory(File loggingDirectory)
   {
      this.loggingDirectory = loggingDirectory;
   }

   @Override
   public boolean isRedirectSystemStreams()
   {
      return redirectSystemStreams;
   }

   public void setRedirectSystemStreams(boolean redirectSystemStreams)
   {
      this.redirectSystemStreams = redirectSystemStreams;
   }

   @Override
   public String getBackgroundImageFileName()
   {
      return backgroundImageFileName;
   }

   public void setBackgroundImageFileName(String backgroundImageFileName)
   {
      this.backgroundImageFileName = backgroundImageFileName;
   }

   @Override
   public boolean isBackgroundImageFileNameRelative()
   {
      return backgroundImageFileNameRelative;
   }

   public void setBackgroundImageFileNameRelative(boolean backgroundImageFileNameRelative)
   {
      this.backgroundImageFileNameRelative = backgroundImageFileNameRelative;
   }

    @Override
    public Configuration getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(Configuration serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public IActionLogger getActionLogger() {
        return actionLogger;
    }

    public void setActionLogger (IActionLogger actionLogger){
        this.actionLogger = actionLogger;
    }

    @Override
    public String getPasswordServiceURL() {
        return passwordServiceURL;
    }

    public void setPasswordServiceURL(String passwordServiceURL) {
        this.passwordServiceURL = passwordServiceURL;
    }
}
