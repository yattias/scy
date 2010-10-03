/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.impl.jdom.RuntimeSettingsEloContentXmlUtils;

public class RuntimeSettingsEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      MissionEloType.MISSION_RUNTIME.getType()
   }

   override protected function doImport(): Void {
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def eloConfigs = RuntimeSettingsEloContentXmlUtils.runtimeSettingsFromXml(xml);
         if (eloConfigs == null) {
            errors = "The xml is not valid for runtime settings";
         }
      }
      errors
   }

}
