/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.scyelo.ScyElo;

/**
 * @author SikkenJ
 */
public class MissionRunConfigs {

   public-init var tbi: ToolBrokerAPI;
   public-init var missionRuntimeModel: MissionRuntimeModel;
   public-init var missionMapModel: MissionModelFX;
   public-init var scyEloToLoad: ScyElo;
   public-init var nomberOfTimesStarted = 1;

   init {
      findNumberOfTimesStarted();
   }

   public override function toString(): String {
      "MissionRunConfigs\{tbi={tbi}, missionRuntimeModel={missionRuntimeModel}, missionMapModel={missionMapModel}, scyEloToLoad={scyEloToLoad}\}"
   }

   function findNumberOfTimesStarted(): Void {
      def missionMapModelEloUri = missionRuntimeModel.getMissionModelElo().getUri();
      if (missionMapModelEloUri != null) {
         def allMissionModelMetadatas = tbi.getRepository().retrieveMetadataAllVersions(missionMapModelEloUri);
         if (allMissionModelMetadatas != null) {
            nomberOfTimesStarted = allMissionModelMetadatas.size();
         }
      }
   }

}
