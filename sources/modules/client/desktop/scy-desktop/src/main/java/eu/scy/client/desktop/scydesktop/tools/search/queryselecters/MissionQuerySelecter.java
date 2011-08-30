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
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

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
      SAME,
      NOT_SAME,
      ALL_VERSIONS_OF_MISSION;
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
            if (myMissionSpecificationUri != null)
            {
               displayOptions.add(MissionOptions.THIS.toString());
               displayOptions.add(MissionOptions.NOT_THIS.toString());
            }
            if (!StringUtils.isEmpty(myMissionId))
            {
               displayOptions.add(MissionOptions.ALL_VERSIONS_OF_THIS.toString());
            }
            break;
         case ELO_BASED:
            if (getBasedOnElo().getMissionSpecificationEloUri() != null)
            {
               displayOptions.add(MissionOptions.SAME.toString());
               displayOptions.add(MissionOptions.NOT_SAME.toString());
            }
            if (getBasedOnElo().getMissionId() != null)
            {
               displayOptions.add(MissionOptions.ALL_VERSIONS_OF_MISSION.toString());
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
   public String getEloIconTooltip()
   {
      return "mission";
   }

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      MissionOptions missionOption = MissionOptions.valueOf(getSelectedOption());
      switch (missionOption)
      {
         case THIS:
            return new MetadataQueryComponent(missionRunningKey, SearchOperation.EQUALS, myMissionSpecificationUri);
         case NOT_THIS:
            return new MetadataQueryComponent(missionRunningKey, SearchOperation.NOT_EQUALS, myMissionSpecificationUri);
         case ALL_VERSIONS_OF_THIS:
            return new MetadataQueryComponent(missionRunningKey, SearchOperation.EQUALS, myMissionId);
         case SAME:
            return new MetadataQueryComponent(missionRunningKey, SearchOperation.EQUALS, getBasedOnElo().getMissionSpecificationEloUri());
         case NOT_SAME:
            return new MetadataQueryComponent(missionRunningKey, SearchOperation.NOT_EQUALS, getBasedOnElo().getMissionSpecificationEloUri());
         case ALL_VERSIONS_OF_MISSION:
            return new MetadataQueryComponent(missionIdKey, SearchOperation.NOT_EQUALS, getBasedOnElo().getMissionId());
      }
      return null;
   }
}
