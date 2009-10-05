/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
   private IMetadataKey technicalFormatKey;
   private MissionModelCreator missionModelCreator;
   private RegisterContentCreators[] registerContentCreators;

   private Map<String,EloConfig> eloConfigs;
   private List<NewEloDescription> newEloDescriptions;

   private File loggingDirectory;
   private boolean redirectSystemStreams = false;

   private String backgroundImageFileName;
   private boolean backgroundImageFileNameRelative;

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
   public RegisterContentCreators[] getRegisterContentCreators()
   {
      return registerContentCreators;
   }

   public void setRegisterContentCreators(RegisterContentCreators[] registerContentCreators)
   {
      this.registerContentCreators = registerContentCreators;
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

   @Override
   public IMetadataKey getTechnicalFormatKey()
   {
      return technicalFormatKey;
   }

   public void setTechnicalFormatKey(IMetadataKey technicalFormatKey)
   {
      this.technicalFormatKey = technicalFormatKey;
   }

   public void setEloConfigs(List<BasicEloConfig> eloConfigList)
   {
      eloConfigs = new HashMap<String, EloConfig>();
      List<NewEloDescription> realNewDescriptions = new ArrayList<NewEloDescription>();
      for (BasicEloConfig basicEloConfig : eloConfigList){
         eloConfigs.put(basicEloConfig.getType(), basicEloConfig);
         if (basicEloConfig.isCreatable()){
            realNewDescriptions.add(new NewEloDescription(basicEloConfig.getType(), basicEloConfig.getDisplay()));
         }
      }
      newEloDescriptions = Collections.unmodifiableList(realNewDescriptions);
    }

   @Override
   public EloConfig getEloConfig(String eloType)
   {
      return eloConfigs.get(eloType);
   }

   @Override
   public List<NewEloDescription> getNewEloDescriptions()
   {
      return newEloDescriptions;
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
}
