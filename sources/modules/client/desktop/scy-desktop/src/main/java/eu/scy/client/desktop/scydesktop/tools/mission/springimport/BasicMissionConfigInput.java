/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor;
import eu.scy.client.desktop.scydesktop.config.BasicMissionMap;
import eu.scy.client.desktop.scydesktop.config.DisplayNames;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public class BasicMissionConfigInput implements MissionConfigInput {
   private static final Logger logger = Logger.getLogger(BasicConfig.class);
   private IRepository repository;
   private List<EloToolConfig> eloToolConfigList;
   private BasicMissionMap basicMissionMap;
   private List<BasicMissionAnchor> basicMissionAnchors;
   private List<URI> templateEloUris;

   public void parseEloConfigs(IRepository repository)
   {
      this.repository = repository;
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

   public void setEloToolConfigs(List<EloToolConfig> eloConfigList)
   {
      this.eloToolConfigList = eloConfigList;
   }

   @Override
   public List<EloToolConfig> getEloToolConfigs()
   {
      return eloToolConfigList;
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
   public BasicMissionMap getBasicMissionMap()
   {
      return basicMissionMap;
   }

   public void setBasicMissionMap(BasicMissionMap basicMissionMap)
   {
      this.basicMissionMap = basicMissionMap;
   }

}
