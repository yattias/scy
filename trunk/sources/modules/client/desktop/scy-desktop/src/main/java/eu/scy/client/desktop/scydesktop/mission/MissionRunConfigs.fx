/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.scyelo.ScyElo;
import java.net.URI;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSettingsManager;

/**
 * @author SikkenJ
 */
public class MissionRunConfigs {

   public-init var tbi: ToolBrokerAPI;
   public-init var missionRuntimeModel: MissionRuntimeModel;
   public-init var missionMapModel: MissionModelFX;
   public-init var scyEloToLoad: ScyElo;
   public-read var nomberOfTimesStarted = 1;
   public-read var missionRuntimeURI: URI;
   public-init var missionSpecificationElo: MissionSpecificationElo;
   public-init var runtimeSettingsManager: RuntimeSettingsManager;

   init {
   //      findNumberOfTimesStarted();
   }

   public override function toString(): String {
      "MissionRunConfigs\{tbi={tbi}, missionRuntimeModel={missionRuntimeModel}, missionMapModel={missionMapModel}, scyEloToLoad={scyEloToLoad}\}"
   }

   public function findAdditionalInformation(): Void {
      findNumberOfTimesStarted();
      missionRuntimeURI = missionRuntimeModel.getMissionRuntimeElo().getUriFirstVersion();
      if (missionSpecificationElo == null) {
         def missionSpecificationURI = missionRuntimeModel.getMissionRuntimeElo().getTypedContent().getMissionSpecificationEloUri();
         if (missionSpecificationURI != null) {
            missionSpecificationElo = MissionSpecificationElo.loadElo(missionSpecificationURI, tbi)
         }
      }
      runtimeSettingsManager = missionRuntimeModel.getRuntimeSettingsManager();
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
