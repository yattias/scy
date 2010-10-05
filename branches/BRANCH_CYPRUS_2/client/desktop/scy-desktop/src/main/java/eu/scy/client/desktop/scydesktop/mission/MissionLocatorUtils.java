/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;
import roolo.api.search.AndQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class MissionLocatorUtils
{

   private static final Logger logger = Logger.getLogger(MissionLocatorUtils.class);

   public static Missions findMissions(final ToolBrokerAPI tbi, final String userName)
   {
      Missions missions = new Missions();
      final IMetadataKey missionRunningKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
      final IMetadataKey technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      IQuery missionSpecificationQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType(), null);
      List<ISearchResult> missionSpecificationResults = tbi.getRepository().search(missionSpecificationQuery);
      IQuery missionRuntimeQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, MissionEloType.MISSION_RUNTIME.getType(), null);
      IQuery titleQuery = new BasicMetadataQuery(missionRunningKey, BasicSearchOperations.EQUALS, userName, null);
      IQuery myMissionRuntimeQuery = new AndQuery(missionRuntimeQuery, titleQuery);
      List<ISearchResult> missionRuntimeResults = tbi.getRepository().search(myMissionRuntimeQuery);
      if (missionSpecificationResults == null || missionSpecificationResults.isEmpty())
      {
         logger.warn("could not find any mission specification elos, falling back to spring configuration");
         return missions;
      }
      HashSet<URI> startedMissionSpecificationUris = new HashSet<URI>();
      for (ISearchResult missionRuntimeResult : missionRuntimeResults)
      {
         IELO elo = tbi.getRepository().retrieveELO(missionRuntimeResult.getUri());
         MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(elo, tbi);
         missions.missionRuntimeElos.add(missionRuntimeElo);
         if (missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri() != null)
         {
            startedMissionSpecificationUris.add(missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri());
         }
      }
      for (ISearchResult missionSpecificationResult : missionSpecificationResults)
      {
         IELO elo = tbi.getRepository().retrieveELO(missionSpecificationResult.getUri());
         if (!startedMissionSpecificationUris.contains(elo.getUri()))
         {
            MissionSpecificationElo missionSpecificationElo = new MissionSpecificationElo(elo, tbi);
            missions.missionSpecificationElos.add(missionSpecificationElo);
         }
      }

      return missions;
   }
}
