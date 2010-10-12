/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.common.mission.TemplateElosElo;
import eu.scy.common.mission.RuntimeSettingsElo;

/**
 * @author SikkenJ
 */
public class MissionRunConfigs {

   public-init var tbi: ToolBrokerAPI;
   public-init var missionRuntimeElo: MissionRuntimeElo;
   public-init var missionMapModel: MissionModelFX;
   public-init var eloToolConfigsElo: EloToolConfigsElo;
   public-init var templateElosElo: TemplateElosElo;
   public-init var runtimeSettingsElo: RuntimeSettingsElo;
}
