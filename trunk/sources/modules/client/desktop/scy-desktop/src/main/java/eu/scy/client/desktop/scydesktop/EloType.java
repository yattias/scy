/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop;

/**
 *
 * @author SikkenJ
 */
public enum EloType {
   MISSION_SPECIFICATIOM("scy/missionspecification"),
   MISSION_RUNTIME("scy/missionruntime"),
   MISSION_MAP_MODEL("scy/missionmapmodel"),
   ELO_TOOL_CONFIGURATION("scy/elotoolconfiguration");

   private final String type;

   private EloType(String type)
   {
      this.type = type;
   }

   public String getType()
   {
      return type;
   }
}
