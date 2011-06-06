/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission;
import eu.scy.common.mission.impl.jdom.MissionRuntimeEloContentXmlUtils;
import eu.scy.common.mission.MissionEloType;

/**
 * @author SikkenJ
 */

public class MissionRuntimeEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      MissionEloType.MISSION_RUNTIME.getType()
   }

   override protected function doImport(): Void {
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def missionSpecification = MissionRuntimeEloContentXmlUtils.missionRuntimeFromXml(xml);
         if (missionSpecification == null) {
            errors = "The xml is not valid for mission specification";
         }
      }
      errors
   }

}
