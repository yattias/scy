/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import eu.scy.client.desktop.scydesktop.config.BasicMissionMap;
import eu.scy.client.desktop.scydesktop.config.RuntimeSettingConfig;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.mission.ColorSchemesElo;
import eu.scy.common.mission.GroupformationStrategyType;
import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.impl.BasicLas;
import eu.scy.common.mission.impl.BasicMissionAnchor;
import eu.scy.common.mission.impl.BasicMissionModelEloContent;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadata;
import roolo.elo.api.exceptions.ELODoesNotExistException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class BasicMissionConfigInput implements MissionConfigInput
{

   private static final Logger logger = Logger.getLogger(BasicConfig.class);
   private ToolBrokerAPI tbi;
   private List<EloToolConfig> eloToolConfigList = new ArrayList<EloToolConfig>();
   private BasicMissionMap basicMissionMap;
   private List<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor> basicMissionAnchors = new ArrayList<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor>();
   private List<URI> templateEloUris = new ArrayList<URI>();
   private List<String> errors = new ArrayList<String>();
   private URI missionDescriptionUri;
   private URI colorSchemesEloUri;
   private URI agentModelsEloUri;
   private String missionId;
   private String missionTitle;
   private String xhtmlVersionId;
   private Locale language;
   private IMetadata templateTrueMetadata;
   private List<RuntimeSetting> runtimeSettings = new ArrayList<RuntimeSetting>();

   public void parseEloConfigs(ToolBrokerAPI tbi)
   {
      this.tbi = tbi;
      templateTrueMetadata = tbi.getELOFactory().createMetadata();
      templateTrueMetadata.getMetadataValueContainer(tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)).setValue(Boolean.TRUE.toString());
      Set<URI> templateEloUriSet = new HashSet<URI>();
      for (URI uri : templateEloUris)
      {
         if (templateEloUriSet.contains(uri))
         {
            logger.error(addError("Duplicate template ELO uri: " + uri));
         }
         else
         {
            setTemplateFlag(uri);
            templateEloUriSet.add(uri);
         }
      }
      templateEloUris.clear();
      templateEloUris.addAll(templateEloUriSet);
      for (eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor anchor : basicMissionAnchors)
      {
         setTemplateFlag(anchor.getUri());
      }
      findGroupFormationRuntimeSettings();
      checkRuntimeSettings();
   }

   private void setTemplateFlag(URI eloUri)
   {
      if (eloUri != null)
      {
         try
         {
            tbi.getRepository().addMetadata(eloUri, templateTrueMetadata);
         }
         catch (ELODoesNotExistException e)
         {
            // elo does not exists, ignore it, this problem is handled elsewhere
         }
      }
   }

   @Override
   public URI getMissionDescriptionUri()
   {
      return missionDescriptionUri;
   }

   public void setMissionDescriptionUri(URI missionDescriptionUri)
   {
      this.missionDescriptionUri = missionDescriptionUri;
   }

   @Override
   public URI getColorSchemesEloUri()
   {
      if (colorSchemesEloUri != null)
      {
         ColorSchemesElo colorSchemesElo = ColorSchemesElo.loadElo(colorSchemesEloUri, tbi);
         if (colorSchemesElo == null)
         {
            logger.warn(addError("could not find ColorSchemeElo with uri: " + colorSchemesEloUri));
         }
      }
      return colorSchemesEloUri;
   }

   public void setColorSchemesEloUri(URI colorSchemesEloUri)
   {
      this.colorSchemesEloUri = colorSchemesEloUri;
   }

   public void setAgentModelsEloUri(URI agentModelsEloUri)
   {
      this.agentModelsEloUri = agentModelsEloUri;
   }

   @Override
   public URI getAgentModelsEloUri()
   {
      if (agentModelsEloUri != null)
      {
         ScyElo agentModelsElo = ScyElo.loadElo(agentModelsEloUri, tbi);
         if (agentModelsElo == null)
         {
            logger.warn(addError("could not find agentModelsElo with uri: " + agentModelsElo));
         }
      }
      return agentModelsEloUri;
   }

   public void setEloToolConfigs(List<EloToolConfig> eloConfigList)
   {
      this.eloToolConfigList = eloConfigList;
   }

   @Override
   public List<EloToolConfig> getEloToolConfigs()
   {
      return eloToolConfigList;
   }

   public void setBasicMissionAnchors(List<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor> basicMissionAnchors)
   {
      if (basicMissionAnchors != null)
      {
         this.basicMissionAnchors = basicMissionAnchors;
      }
   }

   public List<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor> getBasicMissionAnchors()
   {
      List<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor> basicMissionAnchorList = new ArrayList<eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor>();
      if (basicMissionAnchors != null)
      {
         for (eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor missionAnchor : basicMissionAnchors)
         {
            if (missionAnchor.getUri() != null)
            {
               ScyElo scyElo = ScyElo.loadMetadata(missionAnchor.getUri(), tbi);
               missionAnchor.setScyElo(scyElo);
               if (scyElo == null)
               {
                  logger.error(addError("Couldn't find anchor elo: " + missionAnchor.getUri()));
               }
               basicMissionAnchorList.add(missionAnchor);
            }
            else
            {
               logger.error(addError("The basicMissionAnchor with id '" + missionAnchor.getId() + "' has no uri defined"));
            }
         }
      }
      return basicMissionAnchorList;
   }

   public void setTemplateEloUris(List<URI> templateEloUris)
   {
      if (templateEloUris != null)
      {
         this.templateEloUris = templateEloUris;
      }
   }

   @Override
   public List<URI> getTemplateEloUris()
   {
      return templateEloUris;
   }

   public BasicMissionMap getBasicMissionMap()
   {
      return basicMissionMap;
   }

   public void setBasicMissionMap(BasicMissionMap basicMissionMap)
   {
      this.basicMissionMap = basicMissionMap;
   }

   @Override
   public MissionModelEloContent getMissionModelEloContent()
   {
      final BasicMissionModelEloContent missionModelEloContent = new BasicMissionModelEloContent();
      missionModelEloContent.setMissionMapBackgroundImageUri(getBasicMissionMap().getMissionMapBackgroundImageUri());
      missionModelEloContent.setMissionMapInstructionUri(getBasicMissionMap().getMissionMapInstructionUri());
      missionModelEloContent.setMissionMapButtonIconType(getBasicMissionMap().getMissionMapButtonIconType());
      if (missionModelEloContent.getMissionMapBackgroundImageUri() == null || missionModelEloContent.getMissionMapBackgroundImageUri().toString().length() == 0)
      {
         logger.info(addError("MissionMapBackgroundImageUri is not defined"));
      }
      missionModelEloContent.setLoEloUris(getBasicMissionMap().getLoEloUris());
      missionModelEloContent.setLasses(getLasses());

      final String initialActiveLasId = getBasicMissionMap().getInitialLasId();
      if (initialActiveLasId != null)
      {
         Las initialLas = null;
         for (Las las : missionModelEloContent.getLasses())
         {
            if (initialActiveLasId.equals(las.getId()))
            {
               initialLas = las;
            }
         }
         if (initialLas != null)
         {
            missionModelEloContent.setSelectedLas(initialLas);
         }
         else
         {
            logger.error(addError("Cannot find initial active las with id: '" + initialActiveLasId + "'"));
         }
      }
//      missionModel.findPreviousLasLinks();
      return missionModelEloContent;
   }

   private List<Las> getLasses()
   {
      Map<String, BasicLas> lasIdMap = new HashMap<String, BasicLas>();
      List<Las> lasses = new ArrayList<Las>();
      for (eu.scy.client.desktop.scydesktop.config.BasicLas basicLas : getBasicMissionMap().getLasses())
      {
         BasicLas las = new BasicLas();
         las.setId(basicLas.getId());
         las.setXPos(basicLas.getxPosition());
         las.setYPos(basicLas.getyPosition());
         las.setLoEloUris(basicLas.getLoEloUris());
         las.setToolTip(basicLas.getTooltip());
         las.setInstructionUri(basicLas.getInstructionUri());
         las.setLasType(basicLas.getLasType());
         if (!lasIdMap.containsKey(las.getId()))
         {
            lasIdMap.put(las.getId(), las);
            lasses.add(las);
         }
         else
         {
            logger.error(addError("Duplicate las id: '" + las.getId() + "'"));
         }
      }
      // fix the next las links
      for (eu.scy.client.desktop.scydesktop.config.BasicLas basicLas : getBasicMissionMap().getLasses())
      {
         BasicLas las = lasIdMap.get(basicLas.getId());
         List<Las> nextLasses = new ArrayList<Las>();
         for (String lasId : basicLas.getNextLasses())
         {
            BasicLas nextLas = lasIdMap.get(lasId);
            if (nextLas != null)
            {
               nextLasses.add(nextLas);
            }
            else
            {
               logger.error(addError("Cannot find nextLas with id '" + lasId + "', in las with id '" + las.getId() + "'"));
            }
         }
         las.setNextLasses(nextLasses);
      }
      // fix the mission anchor links
      // create all missionanchors
      Map<String, BasicMissionAnchor> missionAnchorMap = new HashMap<String, BasicMissionAnchor>();
      List<BasicMissionAnchor> missionAnchors = new ArrayList<BasicMissionAnchor>();
      for (eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor basicMissionAnchor : getBasicMissionAnchors())
      {
         BasicMissionAnchor missionAnchor = new BasicMissionAnchor();
         missionAnchor.setEloUri(basicMissionAnchor.getUri());
         missionAnchor.setId(basicMissionAnchor.getId());
         missionAnchor.setIconType(basicMissionAnchor.getIconType());
         missionAnchor.setScyElo(basicMissionAnchor.getScyElo());
         missionAnchor.setExisting(basicMissionAnchor.getScyElo() != null);
         missionAnchor.setLoEloUris(basicMissionAnchor.getLoEloUris());
         missionAnchor.setTargetDescriptionUri(basicMissionAnchor.getTargetDescriptionUri());
         missionAnchor.setAssignmentUri(basicMissionAnchor.getAssignmentUri());
         missionAnchor.setResourcesUri(basicMissionAnchor.getResourcesUri());
         missionAnchor.setHelpUri(basicMissionAnchor.getHelpUri());
         missionAnchor.setColorSchemeId(basicMissionAnchor.getColorScheme());
         missionAnchor.setRelationNames(basicMissionAnchor.getRelationNames());
         missionAnchor.setDependingOnMissionAnchorIds(basicMissionAnchor.getDependingOnMissionAnchorIds());
         if (!missionAnchorMap.containsKey(basicMissionAnchor.getId()))
         {
            missionAnchorMap.put(basicMissionAnchor.getId(), missionAnchor);
         }
         else
         {
            logger.error(addError("Duplicate mission anchor id: '" + basicMissionAnchor.getId() + "'"));
         }
      }
      // fill in the input mission anchors
      for (eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor basicMissionAnchor : getBasicMissionAnchors())
      {
         BasicMissionAnchor missionAnchor = missionAnchorMap.get(basicMissionAnchor.getId());
         List<MissionAnchor> inputMissionAnchors = new ArrayList<MissionAnchor>();
         for (String missionAnchorId : basicMissionAnchor.getInputMissionAnchorIds())
         {
            BasicMissionAnchor inputMissionAnchor = missionAnchorMap.get(missionAnchorId);
            if (inputMissionAnchor != null)
            {
               inputMissionAnchors.add(inputMissionAnchor);
            }
            else
            {
               logger.error(addError("Cannot find next mission anchor with id '" + missionAnchorId + "' for mission anchor with id '" + basicMissionAnchor.getId() + "'"));
            }
         }
         missionAnchor.setInputMissionAnchors(inputMissionAnchors);
      }
      // set the mission anchors of the las
      List<Las> errorLasses = new ArrayList<Las>();
      for (eu.scy.client.desktop.scydesktop.config.BasicLas basicLas : getBasicMissionMap().getLasses())
      {
         BasicLas las = lasIdMap.get(basicLas.getId());
         MissionAnchor mainAnchor = missionAnchorMap.get(basicLas.getAnchorEloId());
         if (mainAnchor != null)
         {
            las.setMissionAnchor(mainAnchor);
         }
         else
         {
            logger.error(addError("Cannot find mission anchor with id '" + basicLas.getAnchorEloId() + "' for las with id '" + basicLas.getId() + "'"));
            errorLasses.add(las);
         }
         List<MissionAnchor> intermediateAnchors = new ArrayList<MissionAnchor>();
         for (String intermediateAnhorId : basicLas.getIntermediateEloIds())
         {
            MissionAnchor intermediateAnchor = missionAnchorMap.get(intermediateAnhorId);
            if (intermediateAnchor != null)
            {
               intermediateAnchors.add(intermediateAnchor);
            }
            else
            {
               logger.error(addError("Cannot find intermediate mission anchor with id '" + intermediateAnhorId + "' for las with id '" + basicLas.getId() + "'"));
            }
         }
         las.setIntermediateAnchors(intermediateAnchors);
      }
      // check the dependingOnMissionAnchorIds
      for (BasicMissionAnchor missionAnchor : missionAnchorMap.values())
      {
         List<String> dependingOnMissionAnchorIds = new ArrayList<String>();
         if (missionAnchor.getDependingOnMissionAnchorIds() != null)
         {
            for (String dependingOnMissionAnchorId : missionAnchor.getDependingOnMissionAnchorIds())
            {
               if (missionAnchorMap.containsKey(dependingOnMissionAnchorId))
               {
                  dependingOnMissionAnchorIds.add(dependingOnMissionAnchorId);
               }
               else
               {
                  logger.error(addError("Cannot find depending on mission anchor with id '" + dependingOnMissionAnchorId + "' for mission anchor with id '" + missionAnchor.getId() + "'"));

               }
            }
         }
         missionAnchor.setDependingOnMissionAnchorIds(dependingOnMissionAnchorIds);
      }

      for (Las errorLas : errorLasses)
      {
         lasses.remove(errorLas);
      }

      return lasses;
   }

   private String addError(String errorMessage)
   {
      errors.add(errorMessage);
      return errorMessage;
   }

   @Override
   public List<String> getErrors()
   {
      return errors;
   }

   @Override
   public String getMissionId()
   {
      return missionId;
   }

   public void setMissionId(String missionId)
   {
      this.missionId = missionId;
   }

   @Override
   public String getXhtmlVersionId()
   {
      return xhtmlVersionId;
   }

   public void setXhtmlVersionId(String xhtmlVersionId)
   {
      this.xhtmlVersionId = xhtmlVersionId;
   }

   @Override
   public Locale getLanguage()
   {
      return language;
   }

   public void setLanguageId(String languageId)
   {
      this.language = new Locale(languageId);
   }

   @Override
   public String getMissionTitle()
   {
      return missionTitle;
   }

   public void setMissionTitle(String missionTitle)
   {
      this.missionTitle = missionTitle;
   }

   @Override
   public List<RuntimeSetting> getRuntimeSettings()
   {
      return runtimeSettings;
   }

   public void setRuntimeSettings(List<RuntimeSettingConfig> runtimeSettingConfigs)
   {
      for (RuntimeSettingConfig runtimeSettingConfig : runtimeSettingConfigs)
      {
         int nrOfErrors = errors.size();
         if (StringUtils.isEmpty(runtimeSettingConfig.getName()))
         {
            addError("the name of a runtime setting config may not be empty");
         }
         if (runtimeSettingConfig.getValue() == null)
         {
            addError("the value of a runtime setting config must be defined, name is " + runtimeSettingConfig.getName());
         }
         if (nrOfErrors == errors.size())
         {
            runtimeSettings.add(new RuntimeSetting(new RuntimeSettingKey(runtimeSettingConfig.getName(), runtimeSettingConfig.getLasId(), runtimeSettingConfig.getEloUri()), runtimeSettingConfig.getValue()));
         }
      }
   }

   private void findGroupFormationRuntimeSettings()
   {
      for (eu.scy.client.desktop.scydesktop.config.BasicLas basicLas : getBasicMissionMap().getLasses())
      {
         if (basicLas.getGroupFormationConfig() != null)
         {
            int nrOfErrors = errors.size();
            GroupformationStrategyType strategy = null;
            if (StringUtils.isEmpty(basicLas.getGroupFormationConfig().getStrategy()))
            {
               addError("group formation strategy may not be empty, in las " + basicLas.getId());
            }
            else
            {
               try
               {
                  strategy = GroupformationStrategyType.valueOf(basicLas.getGroupFormationConfig().getStrategy());
               }
               catch (IllegalArgumentException e)
               {
                  addError("group formation strategy (" + basicLas.getGroupFormationConfig().getStrategy() + ") is a unknown type, in las " + basicLas.getId());
               }
            }
            if (basicLas.getGroupFormationConfig().getMinimumNrOfUsers() < 2)
            {
               addError("group formation minimum number of users (" + basicLas.getGroupFormationConfig().getMinimumNrOfUsers() + ") must be at least 2, in las " + basicLas.getId());
            }
            if (basicLas.getGroupFormationConfig().getMinimumNrOfUsers() > basicLas.getGroupFormationConfig().getMaximumNrOfUsers())
            {
               addError("group formation minimum number of users (" + basicLas.getGroupFormationConfig().getMinimumNrOfUsers() + ") may not be larger then maximum number of users (" + basicLas.getGroupFormationConfig().getMaximumNrOfUsers() + "), in las " + basicLas.getId());
            }
            if (basicLas.getGroupFormationConfig().getReferenceEloUri() != null)
            {
               ScyElo elo = ScyElo.loadElo(basicLas.getGroupFormationConfig().getReferenceEloUri(), tbi);
               if (elo == null)
               {
                  addError("group formation reference ELO (" + basicLas.getGroupFormationConfig().getReferenceEloUri() + ") cannot be found, in las " + basicLas.getId());
               }

            }
            if (nrOfErrors == errors.size())
            {
               runtimeSettings.add(new RuntimeSetting(new RuntimeSettingKey("groupformation.strategy", basicLas.getId(), null), strategy.toString()));
               runtimeSettings.add(new RuntimeSetting(new RuntimeSettingKey("groupformation.minUsers", basicLas.getId(), null), "" + basicLas.getGroupFormationConfig().getMinimumNrOfUsers()));
               runtimeSettings.add(new RuntimeSetting(new RuntimeSettingKey("groupformation.maxUsers", basicLas.getId(), null), "" + basicLas.getGroupFormationConfig().getMaximumNrOfUsers()));
               if (basicLas.getGroupFormationConfig().getReferenceEloUri() != null)
               {
                  runtimeSettings.add(new RuntimeSetting(new RuntimeSettingKey("groupformation.referenceElo", basicLas.getId(), null), basicLas.getGroupFormationConfig().getReferenceEloUri().toString()));
               }
            }
         }
      }
   }

   private void checkRuntimeSettings()
   {
      Set<String> lasIds = new HashSet<String>();
      for (eu.scy.client.desktop.scydesktop.config.BasicLas basicLas : getBasicMissionMap().getLasses())
      {
         lasIds.add(basicLas.getId());
      }
      List<RuntimeSetting> checkedRuntimeSettings = new ArrayList<RuntimeSetting>();
      for (RuntimeSetting runtimeSetting : runtimeSettings)
      {
         int nrOfErrors = errors.size();
         if (runtimeSetting.getKey().getLasId() != null && !lasIds.contains(runtimeSetting.getKey().getLasId()))
         {
            addError("unknown lasId " + runtimeSetting.getKey().getLasId() + " in runtime setting: " + runtimeSetting);
         }
         if (nrOfErrors == errors.size())
         {
            checkedRuntimeSettings.add(runtimeSetting);
         }
      }
      runtimeSettings = checkedRuntimeSettings;
   }
}
