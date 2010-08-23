/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.client.desktop.scydesktop.config.Config;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;
import roolo.api.search.AndQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IELO;

/**
 *
 * @author SikkenJ
 */
public class MissionLocatorUtils
{

   private static final Logger logger = Logger.getLogger(MissionLocatorUtils.class);

   public static class Missions
   {

      List<MissionRuntimeElo> missionRuntimeElos;
      List<MissionSpecificationElo> missionSpecificationElos;

      public Missions()
      {
         missionRuntimeElos = new ArrayList<MissionRuntimeElo>();
         missionSpecificationElos = new ArrayList<MissionSpecificationElo>();
      }

      public boolean isEmpty(){
         return missionRuntimeElos.isEmpty() && missionSpecificationElos.isEmpty();
      }

      public int size(){
         return missionRuntimeElos.size() + missionSpecificationElos.size();
      }
   }

   public static Missions findMissions(Config config, String userName)
   {
      Missions missions = new Missions();
      IQuery missionSpecificationQuery = new BasicMetadataQuery(config.getTechnicalFormatKey(), BasicSearchOperations.EQUALS, EloType.MISSION_SPECIFICATIOM, null);
      List<ISearchResult> missionSpecificationResults = config.getRepository().search(missionSpecificationQuery);
      IQuery missionRuntimeQuery = new BasicMetadataQuery(config.getTechnicalFormatKey(), BasicSearchOperations.EQUALS, EloType.MISSION_RUNTIME, null);
      IQuery titleQuery = new BasicMetadataQuery(config.getTitleKey(), BasicSearchOperations.EQUALS, userName, null);
      IQuery myMissionRuntimeQuery = new AndQuery(missionRuntimeQuery, titleQuery);
      List<ISearchResult> missionRuntimeResults = config.getRepository().search(myMissionRuntimeQuery);
      if (missionSpecificationResults.isEmpty())
      {
         logger.warn("could not find any mission specification elos, falling back to spring configuration");
         return missions;
      }
      HashSet<URI> startedMissionSpecificationUris = new HashSet<URI>();
      for (ISearchResult missionRuntimeResult : missionRuntimeResults)
      {
         IELO elo = config.getRepository().retrieveELO(missionRuntimeResult.getUri());
         MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(elo, config.getMetadataTypeManager());
         missions.missionRuntimeElos.add(missionRuntimeElo);
         startedMissionSpecificationUris.add(missionRuntimeElo.getUri());
      }
      for (ISearchResult missionSpecificationResult : missionSpecificationResults)
      {
         IELO elo = config.getRepository().retrieveELO(missionSpecificationResult.getUri());
         if (!startedMissionSpecificationUris.contains(elo.getUri()))
         {
            MissionSpecificationElo missionSpecificationElo = new MissionSpecificationElo(elo, config.getMetadataTypeManager());
            missions.missionSpecificationElos.add(missionSpecificationElo);
         }
      }

      return missions;
   }
}
