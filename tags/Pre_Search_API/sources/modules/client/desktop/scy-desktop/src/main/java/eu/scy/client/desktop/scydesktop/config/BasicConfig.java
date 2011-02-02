/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.common.mission.impl.BasicEloToolConfig;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
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
   private ToolBrokerAPI toolBrokerAPI;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;
   private RegisterContentCreators[] registerContentCreators;
   private List<BasicEloToolConfig> eloToolConfigList;
   private Map<String, BasicEloToolConfig> eloConfigs;
   private List<String> newEloDescriptions;
   private BasicMissionMap basicMissionMap;
//   private String missionId;
//   private String missionName;
   private List<BasicMissionAnchor> basicMissionAnchors;
//   private URI activeMissionAnchorUri;
   private List<URI> templateEloUris;
   private DisplayNames logicalTypeDisplayNames;
   private DisplayNames functionalTypeDisplayNames;
   private File loggingDirectory;
   private boolean redirectSystemStreams = false;
   private String backgroundImageFileName;
   private boolean backgroundImageFileNameRelative;

   public void initialize()
   {
      parseEloConfigs();
   }

   public void parseEloConfigs()
   {
      eloConfigs = new HashMap<String, BasicEloToolConfig>();
      for (BasicEloToolConfig basicEloToolConfig : eloToolConfigList)
      {
         eloConfigs.put(basicEloToolConfig.getEloType(), basicEloToolConfig);
      }
      if (templateEloUris == null)
      {
         templateEloUris = new ArrayList<URI>();
      }
      Set<URI> templateEloUriSet = new HashSet<URI>();
      for (URI uri : templateEloUris)
      {
         if (templateEloUriSet.contains(uri))
         {
            logger.error("duplicate template ELO uri: " + uri);
         }
         else
         {
            templateEloUriSet.add(uri);
         }
      }
      templateEloUris.clear();
      templateEloUris.addAll(templateEloUriSet);
   }

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
      List<String> realNewTypes = new ArrayList<String>();
      eloToolConfigList = new ArrayList<BasicEloToolConfig>();
      for (BasicEloConfig basicEloConfig : eloConfigList){
         eloToolConfigList.add(basicEloConfig);
         if (basicEloConfig.isCreatable()){
            realNewTypes.add(basicEloConfig.getEloType());
         }
      }
      newEloDescriptions = Collections.unmodifiableList(realNewTypes);
   }

   public void setEloToolConfigs(List<BasicEloToolConfig> eloToolConfigList)
   {
      this.eloToolConfigList = eloToolConfigList;
   }

   @Override
   public BasicEloToolConfig getEloToolConfig(String eloType)
   {
      return eloConfigs.get(eloType);
   }

   @Override
   public List<String> getNewEloTypes()
   {
      return newEloDescriptions;
   }

//   public void setMissionId(String missionId)
//   {
//      this.missionId = missionId;
//   }
//   @Override
//   public String getMissionId()
//   {
//      return missionId;
//   }
//
//   public void setMissionName(String missionName)
//   {
//      this.missionName = missionName;
//   }
//
//   @Override
//   public String getMissionName()
//   {
//      return missionName;
//   }
//   public void setBasicMissionAnchorConfigs(List<BasicMissionAnchorConfig> basicMissionAnchorConfigs)
//   {
//      this.basicMissionAnchorConfigs = basicMissionAnchorConfigs;
//   }
//
//   @Override
//   public List<BasicMissionAnchorConfig> getBasicMissionAnchorConfigs()
//   {
//      return basicMissionAnchorConfigs;
//   }
//   public void setActiveMissionAnchorUri(URI activeMissionAnchorUri)
//   {
//      this.activeMissionAnchorUri = activeMissionAnchorUri;
//   }
//
//   @Override
//   public URI getActiveMissionAnchorUri()
//   {
//      return activeMissionAnchorUri;
//   }
   public void setBasicMissionAnchors(List<BasicMissionAnchor> basicMissionAnchors)
   {
      this.basicMissionAnchors = basicMissionAnchors;
   }

   @Override
   public List<BasicMissionAnchor> getBasicMissionAnchors()
   {
      List<BasicMissionAnchor> basicMissionAnchorList = new ArrayList<BasicMissionAnchor>();
      if (basicMissionAnchors != null)
      {
         for (BasicMissionAnchor missionAnchor : basicMissionAnchors)
         {
            if (missionAnchor.getUri() != null)
            {
               ScyElo scyElo = ScyElo.loadMetadata(missionAnchor.getUri(), toolBrokerAPI);
               missionAnchor.setScyElo(scyElo);
               if (scyElo == null)
               {
                  logger.error("Couldn't find anchor elo: " + missionAnchor.getUri());
               }
               basicMissionAnchorList.add(missionAnchor);
            }
            else
            {
               logger.error("The basicMissionAnchor with id " + missionAnchor.getId() + " has no uri defined");
            }
         }
      }
//      List<BasicMissionAnchor> basicMissionAnchors = new ArrayList<BasicMissionAnchor>();
//      Map<String, BasicMissionAnchor> basicMissionAnchorsMap = new HashMap<String, BasicMissionAnchor>();
//      // create the list of BasicMissionAnchorConfig
//      for (BasicMissionAnchorConfig basicMissionAnchorConfig : basicMissionAnchorConfigs)
//      {
//         BasicMissionAnchor missionAnchor = new BasicMissionAnchor();
//         missionAnchor.setEloUri(basicMissionAnchorConfig.getUri());
//         missionAnchor.setId(basicMissionAnchorConfig.getId());
//         IMetadata metadata = repository.retrieveMetadata(missionAnchor.getEloUri());
//         missionAnchor.setMetadata(metadata);
//         if (metadata == null)
//         {
//            logger.error("Couldn't find anchor elo: " + missionAnchor.getEloUri());
//         }
//          missionAnchor.setRelationNames(basicMissionAnchorConfig.getRelationNames());
//         missionAnchor.setLoEloUris(createExistingUriList(basicMissionAnchorConfig.getLoEloUris(), "learning object", basicMissionAnchorConfig.getId()));
//         basicMissionAnchors.add(missionAnchor);
//         if (basicMissionAnchorsMap.containsKey(basicMissionAnchorConfig.getId()))
//         {
//            logger.error("duplicate anchor name: " + basicMissionAnchorConfig.getId());
//         }
//         else
//         {
//            basicMissionAnchorsMap.put(basicMissionAnchorConfig.getId(), missionAnchor);
//         }
//      }
////      // fill in the links
////      for (BasicMissionAnchorConfig basicMissionAnchorConfig : basicMissionAnchorConfigs)
////      {
////         BasicMissionAnchor missionAnchor = basicMissionAnchorsMap.get(basicMissionAnchorConfig.getName());
////         if (missionAnchor != null)
////         {
////            missionAnchor.setNextMissionAnchors(createMissionAnchorList(basicMissionAnchorConfig.getNextMissionAnchorNames(), basicMissionAnchorsMap, basicMissionAnchorConfig.getUri(), "next"));
////            missionAnchor.setInputMissionAnchors(createMissionAnchorList(basicMissionAnchorConfig.getInputMissionAnchorNames(), basicMissionAnchorsMap, basicMissionAnchorConfig.getUri(), "input"));
////         }
////      }
//      // "convert" the list
//      for (BasicMissionAnchor basicMissionAnchor : basicMissionAnchors)
//      {
//         missionAnchors.add(basicMissionAnchor);
//      }
      return basicMissionAnchorList;
   }

   @Override
   public List<URI> getAllMissionEloUris()
   {
      List<URI> allMissionEloUris = new ArrayList<URI>();
      if (basicMissionMap != null)
      {
         Set<URI> allMissionEloUriSet = new HashSet<URI>();
         addListToSet(basicMissionMap.getLoEloUris(), allMissionEloUriSet);
         Set<String> anchorIdSet = new HashSet<String>();
         for (BasicLas basicLas : basicMissionMap.getLasses())
         {
            addListToSet(basicLas.getLoEloUris(), allMissionEloUriSet);
            anchorIdSet.add(basicLas.getAnchorEloId());
            if (basicLas.getIntermediateEloIds() != null)
            {
               anchorIdSet.addAll(basicLas.getIntermediateEloIds());
            }
         }
         for (BasicMissionAnchor missionAnchor : basicMissionAnchors)
         {
            if (missionAnchor.getUri() != null)
            {
               allMissionEloUriSet.add(missionAnchor.getUri());
            }
            addListToSet(missionAnchor.getLoEloUris(), allMissionEloUriSet);
         }
         allMissionEloUris.addAll(allMissionEloUriSet);
         Collections.sort(allMissionEloUris);
      }
      return allMissionEloUris;
   }

   private void addListToSet(List<URI> uris, Set<URI> uriSet)
   {

      if (uris != null)
      {
         uriSet.addAll(uris);
      }
   }

//   private List<URI> createExistingUriList(List<URI> uris, String label, String anchorName)
//   {
//      List<URI> existingUris = new ArrayList<URI>();
//      if (uris != null)
//      {
//         for (URI uri : uris)
//         {
//            IMetadata eloMetadata = repository.retrieveMetadata(uri);
//            if (eloMetadata != null)
//            {
//               if (!existingUris.contains(uri))
//               {
//                  existingUris.add(uri);
//               }
//               else
//               {
//                  logger.error("Duplicate " + label + " elo uri: " + uri + " for mission anchor " + anchorName);
//
//               }
//            }
//            else
//            {
//               logger.error("Could not find " + label + " elo uri: " + uri + " for mission anchor " + anchorName);
//            }
//         }
//      }
//      return existingUris;
//   }
//
//   private List<MissionAnchor> createMissionAnchorList(List<String> names, Map<String, BasicMissionAnchor> basicMissionAnchorsMap, URI missionAnchorUri, String label)
//   {
//      List<MissionAnchor> missionAnchors = new ArrayList<MissionAnchor>();
//      if (names != null)
//      {
//         for (String name : names)
//         {
//            BasicMissionAnchor missionAnchor = basicMissionAnchorsMap.get(name);
//            if (missionAnchor != null)
//            {
//               if (!missionAnchors.contains(missionAnchor))
//               {
//                  missionAnchors.add(missionAnchor);
//               }
//               else
//               {
//                  logger.error("Duplicate " + label + " mission anchor with name: " + name + " for mission anchor " + name);
//               }
//            }
//            else
//            {
//               logger.error("can't find " + label + " mission anchor with name: " + name + " for mission anchor " + name);
//            }
//         }
//      }
//      else
//      {
//         logger.info("no " + label + " anchor names for " + missionAnchorUri);
//      }
//      return missionAnchors;
//   }
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
   public DisplayNames getFunctionalTypeDisplayNames()
   {
      return functionalTypeDisplayNames;
   }

   public void setFunctionalTypeDisplayNames(DisplayNames functionalTypeDisplayNames)
   {
      this.functionalTypeDisplayNames = functionalTypeDisplayNames;
   }

   @Override
   public DisplayNames getLogicalTypeDisplayNames()
   {
      return logicalTypeDisplayNames;
   }

   public void setLogicalTypeDisplayNames(DisplayNames logicalTypeDisplayNames)
   {
      this.logicalTypeDisplayNames = logicalTypeDisplayNames;
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
   public BasicMissionMap getBasicMissionMap()
   {
      return basicMissionMap;
   }

   public void setBasicMissionMap(BasicMissionMap basicMissionMap)
   {
      this.basicMissionMap = basicMissionMap;
   }
}
