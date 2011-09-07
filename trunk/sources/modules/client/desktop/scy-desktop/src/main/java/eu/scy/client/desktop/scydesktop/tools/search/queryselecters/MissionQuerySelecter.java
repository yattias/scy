/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQuery;

/**
 *
 * @author SikkenJ
 */
public class MissionQuerySelecter extends AbstractSimpleQuerySelecter
{

   private final IMetadataKey missionRunningKey;
   private final IMetadataKey missionIdKey;
   private final URI myMissionSpecificationUri;
   private final String myMissionId;

   private enum MissionOptions
   {

      THIS,
      NOT_THIS,
      ALL_VERSIONS_OF_THIS,
      NOT_VERSIONS_OF_THIS,
      SAME,
      NOT_SAME,
      ALL_VERSIONS_OF_MISSION,
      NOT_VERSIONS_OF_MISSION;
   }

   MissionQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.missionRunningKey = getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
      this.missionIdKey = getMetadataKey(CoreRooloMetadataKeyIds.MISSION_ID);
      myMissionSpecificationUri = tbi.getMissionSpecificationURI();
      myMissionId = "";
   }

   @Override
   protected List<String> createDisplayOptions()
   {
      List<String> displayOptions = new ArrayList<String>();
      switch (getQuerySelectorUsage())
      {
         case TEXT:
            if (isDebugMode() &&  myMissionSpecificationUri != null)
            {
               displayOptions.add(MissionOptions.THIS.toString());
               displayOptions.add(MissionOptions.NOT_THIS.toString());
            }
            if (!StringUtils.isEmpty(myMissionId))
            {
               displayOptions.add(MissionOptions.ALL_VERSIONS_OF_THIS.toString());
               displayOptions.add(MissionOptions.NOT_VERSIONS_OF_THIS.toString());
            }
            break;
         case ELO_BASED:
            if (isDebugMode() && getBasedOnElo().getMissionSpecificationEloUri() != null)
            {
               displayOptions.add(MissionOptions.SAME.toString());
               displayOptions.add(MissionOptions.NOT_SAME.toString());
            }
            if (getBasedOnElo().getMissionId() != null)
            {
               displayOptions.add(MissionOptions.ALL_VERSIONS_OF_MISSION.toString());
               displayOptions.add(MissionOptions.NOT_VERSIONS_OF_MISSION.toString());
            }
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "mission_map";
   }

   @Override
   public void setFilterOptions(IQuery query)
   {
      MissionOptions missionOption = getSelectedEnum(MissionOptions.class);
      if (missionOption != null)
      {
         URI allowedMissionSpecificationUri = null;
         URI notAllowedMissionSpecificationUri = null;
         String allowedMissionId = null;
         String notAllowedMissionId = null;
         switch (missionOption)
         {
            case THIS:
               allowedMissionSpecificationUri = myMissionSpecificationUri;
               break;
            case NOT_THIS:
               notAllowedMissionSpecificationUri = myMissionSpecificationUri;
               break;
            case ALL_VERSIONS_OF_THIS:
               allowedMissionId = myMissionId;
               break;
            case NOT_VERSIONS_OF_THIS:
               notAllowedMissionId = myMissionId;
               break;
            case SAME:
               allowedMissionSpecificationUri = getBasedOnElo().getMissionRuntimeEloUri();
               break;
            case NOT_SAME:
               notAllowedMissionSpecificationUri = getBasedOnElo().getMissionRuntimeEloUri();
               break;
            case ALL_VERSIONS_OF_MISSION:
               allowedMissionId = getBasedOnElo().getMissionId();
               break;
            case NOT_VERSIONS_OF_MISSION:
               notAllowedMissionId = getBasedOnElo().getMissionId();
               break;
         }
         query.setIncludedMissionSpecification(allowedMissionSpecificationUri);
         query.setExcludedMissionSpecification(notAllowedMissionSpecificationUri);
         query.setIncludedMissionId(allowedMissionId);
         query.setExcludedMissionId(notAllowedMissionId);
      }
   }
}
