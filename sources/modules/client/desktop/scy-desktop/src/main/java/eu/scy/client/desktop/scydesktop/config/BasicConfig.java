/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.IllegalStateException;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
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
   private ToolBrokerAPI toolBrokerAPI;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;
   private RegisterContentCreators[] registerContentCreators;
   private Map<String, EloConfig> eloConfigs;
   private List<NewEloDescription> newEloDescriptions;
   private List<BasicMissionAnchorConfig> basicMissionAnchorConfigs;
   private URI activeMissionAnchorUri;
   private List<URI> templateEloUris;
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
      titleKey = getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      technicalFormatKey = getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   private IMetadataKey getMetadataKey(CoreRooloMetadataKeyIds keyId)
   {
      IMetadataKey key = metadataTypeManager.getMetadataKey(keyId.getId());
      if (key == null)
      {
         throw new IllegalStateException("cannot find key " + keyId.getId());
      }
      return key;
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
   public ToolBrokerAPI getToolBrokerAPI()
   {
      return toolBrokerAPI;
   }

   public void setToolBrokerAPI(ToolBrokerAPI toolBrokerAPI)
   {
      if (toolBrokerAPI == null)
      {
         throw new IllegalArgumentException("toolBrokerAPI may not be null");
      }
      this.toolBrokerAPI = toolBrokerAPI;
      setRepository(toolBrokerAPI.getRepository());
      setExtensionManager(toolBrokerAPI.getExtensionManager());
      setMetadataTypeManager(toolBrokerAPI.getMetaDataTypeManager());
      setEloFactory(toolBrokerAPI.getELOFactory());
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
      for (BasicEloConfig basicEloConfig : eloConfigList)
      {
         eloConfigs.put(basicEloConfig.getType(), basicEloConfig);
         if (basicEloConfig.isCreatable())
         {
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

   public void setBasicMissionAnchorConfigs(List<BasicMissionAnchorConfig> basicMissionAnchorConfigs)
   {
      this.basicMissionAnchorConfigs = basicMissionAnchorConfigs;
   }

   @Override
   public List<BasicMissionAnchorConfig> getBasicMissionAnchorConfigs()
   {
      return basicMissionAnchorConfigs;
   }

   public void setActiveMissionAnchorUri(URI activeMissionAnchorUri)
   {
      this.activeMissionAnchorUri = activeMissionAnchorUri;
   }

   @Override
   public URI getActiveMissionAnchorUri()
   {
      return activeMissionAnchorUri;
   }

   @Override
   public List<MissionAnchor> getMissionAnchors()
   {
      List<MissionAnchor> missionAnchors = new ArrayList<MissionAnchor>();
      if (basicMissionAnchorConfigs == null)
      {
         return missionAnchors;
      }
      List<BasicMissionAnchor> basicMissionAnchors = new ArrayList<BasicMissionAnchor>();
      Map<String, BasicMissionAnchor> basicMissionAnchorsMap = new HashMap<String, BasicMissionAnchor>();
      // create the list of BasicMissionAnchorConfig
      for (BasicMissionAnchorConfig basicMissionAnchorConfig : basicMissionAnchorConfigs)
      {
         BasicMissionAnchor missionAnchor = new BasicMissionAnchor();
         missionAnchor.setEloUri(basicMissionAnchorConfig.getUri());
         missionAnchor.setName(basicMissionAnchorConfig.getName());
         IMetadata metadata = repository.retrieveMetadata(missionAnchor.getEloUri());
         missionAnchor.setMetadata(metadata);
         if (metadata != null)
         {
            missionAnchor.setExisting(true);
            missionAnchor.setTitle((String) metadata.getMetadataValueContainer(titleKey).getValue());
         }
         else
         {
            missionAnchor.setExisting(false);
            logger.error("Couldn't find anchor elo: " + missionAnchor.getEloUri());
         }
         missionAnchor.setXPosition(basicMissionAnchorConfig.getXPosition());
         missionAnchor.setYPosition(basicMissionAnchorConfig.getYPosition());
         missionAnchor.setRelationNames(basicMissionAnchorConfig.getRelationNames());
         missionAnchor.setToolTip(basicMissionAnchorConfig.getToolTip());
         List<URI> helpEloUris = new ArrayList<URI>();
         if (basicMissionAnchorConfig.getHelpEloUris() != null)
         {
            for (URI helpEloUri : basicMissionAnchorConfig.getHelpEloUris())
            {
               IMetadata helpEloMetadata = repository.retrieveMetadata(helpEloUri);
               if (helpEloMetadata != null)
               {
                  helpEloUris.add(helpEloUri);
               }
               else
               {
                  logger.error("Could not find help elo uri: " + helpEloUri + " for mission anchor " + basicMissionAnchorConfig.getName());
               }
            }
         }
         missionAnchor.setHelpEloUris(helpEloUris);
         basicMissionAnchors.add(missionAnchor);
         if (basicMissionAnchorsMap.containsKey(basicMissionAnchorConfig.getName()))
         {
            logger.error("duplicate anchor name: " + basicMissionAnchorConfig.getName());
         }
         else
         {
            basicMissionAnchorsMap.put(basicMissionAnchorConfig.getName(), missionAnchor);
         }
      }
      // fill in the links
      for (BasicMissionAnchorConfig basicMissionAnchorConfig : basicMissionAnchorConfigs)
      {
         BasicMissionAnchor missionAnchor = basicMissionAnchorsMap.get(basicMissionAnchorConfig.getName());
         if (missionAnchor != null)
         {
            missionAnchor.setNextMissionAnchors(createMissionAnchorList(basicMissionAnchorConfig.getNextMissionAnchorNames(), basicMissionAnchorsMap, basicMissionAnchorConfig.getUri()));
            missionAnchor.setInputMissionAnchors(createMissionAnchorList(basicMissionAnchorConfig.getInputMissionAnchorNames(), basicMissionAnchorsMap, basicMissionAnchorConfig.getUri()));
         }
      }
      // "convert" the list
      for (BasicMissionAnchor basicMissionAnchor : basicMissionAnchors)
      {
         missionAnchors.add(basicMissionAnchor);
      }
      return missionAnchors;
   }

   private List<MissionAnchor> createMissionAnchorList(List<String> names, Map<String, BasicMissionAnchor> basicMissionAnchorsMap, URI missionAnchorUri)
   {
      List<MissionAnchor> nextMissionAnchors = new ArrayList<MissionAnchor>();
      if (names != null)
      {
         for (String name : names)
         {
            BasicMissionAnchor nextMissionAnchor = basicMissionAnchorsMap.get(name);
            if (nextMissionAnchor != null)
            {
               nextMissionAnchors.add(nextMissionAnchor);
            }
            else
            {
               logger.error("can't find next mission anchor with name: " + name + " for mission anchor " + name);
            }
         }
      }
      else
      {
         logger.info("no next anchor names for " + missionAnchorUri);
      }
      return nextMissionAnchors;
   }

   public void setTemplateEloUris(List<URI> templateEloUris)
   {
      this.templateEloUris = templateEloUris;
   }

   @Override
   public List<URI> getTemplateEloUris()
   {
      return templateEloUris;
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
