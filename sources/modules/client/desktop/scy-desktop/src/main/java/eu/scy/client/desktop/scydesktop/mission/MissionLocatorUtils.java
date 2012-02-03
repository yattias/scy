/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.Access;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.AndQuery;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class MissionLocatorUtils
{

   private static final Logger logger = Logger.getLogger(MissionLocatorUtils.class);

   public static Missions findMissions(final ToolBrokerAPI tbi, String userName)
   {
      Missions missions = new Missions();
      final IMetadataKey userRunningMissionKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.USER_RUNNING_MISSION);
      final IMetadataKey technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);

      IQueryComponent missionSpecificationQueryComponent = new MetadataQueryComponent(technicalFormatKey,
         SearchOperation.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType());
      IQuery missionSpecificationQuery = new Query(missionSpecificationQueryComponent);
      List<ISearchResult> missionSpecificationResults = tbi.getRepository().search(missionSpecificationQuery);

      IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
      IQueryComponent titleQuery = new MetadataQueryComponent(userRunningMissionKey, SearchOperation.EQUALS, userName);
      IQueryComponent myMissionRuntimeQueryComponent = new AndQuery(missionRuntimeQueryComponent, titleQuery);
      IQuery missionRuntimeQuery = new Query(myMissionRuntimeQueryComponent);
      List<ISearchResult> missionRuntimeResults = tbi.getRepository().search(missionRuntimeQuery);
      if (missionRuntimeResults == null || missionSpecificationResults.isEmpty())
      {
         logger.warn("could not find any mission specification elos, falling back to spring configuration");
         return missions;
      }
      HashSet<URI> startedMissionSpecificationUris = new HashSet<URI>();
      HashSet<String> startedMissionSpecificationIds = new HashSet<String>();
      if (missionRuntimeResults != null)
      {
         for (ISearchResult missionRuntimeResult : missionRuntimeResults)
         {
            IELO elo = tbi.getRepository().retrieveELO(missionRuntimeResult.getUri());
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(elo, tbi);
            if (missionRuntimeElo.getAccess() != Access.DELETED)
            {
               missions.missionRuntimeElos.add(missionRuntimeElo);
               if (missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri() != null)
               {
                  startedMissionSpecificationUris.add(missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri());
               }
               String id = getMissionIdWithLanguages(missionRuntimeElo);
               if (id != null)
               {
                  startedMissionSpecificationIds.add(id);
               }
            }
         }
      }
      if (missionSpecificationResults != null)
      {
         List<MissionSpecificationElo> missionSpecificationElos = new ArrayList<MissionSpecificationElo>();
         for (ISearchResult missionSpecificationResult : missionSpecificationResults)
         {
            IELO elo = tbi.getRepository().retrieveELO(missionSpecificationResult.getUri());
            if (!startedMissionSpecificationUris.contains(elo.getUri()))
            {
               MissionSpecificationElo missionSpecificationElo = new MissionSpecificationElo(elo, tbi);
               String id = getMissionIdWithLanguages(missionSpecificationElo);
               if (!startedMissionSpecificationIds.contains(id))
               {
                  if (missionSpecificationElo.getAccess() != Access.DELETED)
                  {
//                     if (missionSpecificationElo.getElo().supportsLanguage(Locale.getDefault()) || missionSpecificationElo.getElo().supportsLanguage(Locale.ENGLISH))
                     {
                        missionSpecificationElos.add(missionSpecificationElo);
                     }
                  }
               }
            }
         }
//         missions.missionSpecificationElos.addAll(filterOutOlderVersions(missionSpecificationElos));
         missions.missionSpecificationElos.addAll(missionSpecificationElos);
      }
      return missions;
   }

   private static List<MissionSpecificationElo> filterOutOlderVersions(List<MissionSpecificationElo> missionSpecifications)
   {
      List<MissionSpecificationElo> filteredMissions = new ArrayList<MissionSpecificationElo>();
      List<String> missionIds = new ArrayList<String>();
      for (MissionSpecificationElo mission : missionSpecifications)
      {
         String missionId = getMissionIdWithLanguages(mission);
         if (missionId != null)
         {
            if (!missionIds.contains(missionId))
            {
               missionIds.add(missionId);
            }
         }
         else
         {
            // no mission id defined
            filteredMissions.add(mission);
         }
      }
      for (String missionId : missionIds)
      {
         MissionSpecificationElo lastVersion = null;
         for (MissionSpecificationElo mission : missionSpecifications)
         {
            if (missionId.equals(getMissionIdWithLanguages(mission)))
            {
               if (lastVersion == null)
               {
                  lastVersion = mission;
               }
               else if (mission.getDateLastModified() > lastVersion.getDateLastModified())
               {
                  lastVersion = mission;
               }
            }
         }
         // add the last version
         if (lastVersion != null)
         {
            filteredMissions.add(lastVersion);
         }
      }
      return filteredMissions;
   }

   private static String getMissionIdWithLanguages(MissionSpecificationElo mission)
   {
      return getMissionIdWithLanguages(mission.getTypedContent().getMissionId(), mission.getElo().getLanguages());
   }

   private static String getMissionIdWithLanguages(MissionRuntimeElo mission)
   {
      return getMissionIdWithLanguages(mission.getTypedContent().getMissionId(), mission.getElo().getLanguages());
   }

   private static String getMissionIdWithLanguages(String missionId, List<Locale> languages)
   {
      if (missionId != null)
      {
         StringBuilder id = new StringBuilder(missionId);
         if (languages != null)
         {
            for (Locale language : languages)
            {
               id.append('_');
               id.append(language.getLanguage());
            }
         }
         return id.toString();
      }
      return null;
   }
}
