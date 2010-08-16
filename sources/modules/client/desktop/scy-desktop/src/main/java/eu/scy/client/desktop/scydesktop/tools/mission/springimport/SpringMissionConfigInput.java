/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import eu.scy.client.desktop.scydesktop.config.BasicEloConfig;
import eu.scy.client.desktop.scydesktop.config.BasicLas;
import eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor;
import eu.scy.client.desktop.scydesktop.config.BasicMissionMap;
import eu.scy.client.desktop.scydesktop.config.DisplayNames;
import eu.scy.client.desktop.scydesktop.config.NewEloDescription;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public class SpringMissionConfigInput implements MissionConfigInput
{

   private static final Logger logger = Logger.getLogger(BasicConfig.class);
   private List<BasicEloConfig> eloConfigList;
   private Map<String, BasicEloConfig> eloConfigs;
   private List<NewEloDescription> newEloDescriptions;
   private BasicMissionMap basicMissionMap;
   private List<BasicMissionAnchor> basicMissionAnchors;
   private List<URI> templateEloUris;
   private DisplayNames logicalTypeDisplayNames;
   private DisplayNames functionalTypeDisplayNames;
   private IRepository repository;

   public void initialize(IRepository repository)
   {
      this.repository = repository;
      parseEloConfigs();
   }

   public void parseEloConfigs()
   {
      eloConfigs = new HashMap<String, BasicEloConfig>();
      List<NewEloDescription> realNewDescriptions = new ArrayList<NewEloDescription>();
      for (BasicEloConfig basicEloConfig : eloConfigList)
      {
         basicEloConfig.checkTypeNames(logicalTypeDisplayNames, functionalTypeDisplayNames);
         eloConfigs.put(basicEloConfig.getType(), basicEloConfig);
         if (basicEloConfig.isCreatable())
         {
            realNewDescriptions.add(new NewEloDescription(basicEloConfig.getType(), basicEloConfig.getDisplay()));
         }
      }
      newEloDescriptions = Collections.unmodifiableList(realNewDescriptions);
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

   public void setEloConfigs(List<BasicEloConfig> eloConfigList)
   {
      this.eloConfigList = eloConfigList;
   }

   @Override
   public List<NewEloDescription> getNewEloDescriptions()
   {
      return newEloDescriptions;
   }

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
               IMetadata metadata = repository.retrieveMetadata(missionAnchor.getUri());
               missionAnchor.setMetadata(metadata);
               if (metadata == null)
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
   public BasicMissionMap getBasicMissionMap()
   {
      return basicMissionMap;
   }

   public void setBasicMissionMap(BasicMissionMap basicMissionMap)
   {
      this.basicMissionMap = basicMissionMap;
   }
}
