/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission;

/**
 *
 * @author SikkenJ
 */
public enum MissionEloType {
   MISSION_SPECIFICATIOM("scy/missionspecification"),
   MISSION_RUNTIME("scy/missionruntime"),
   MISSION_MAP_MODEL("scy/missionmapmodel"),
   ELO_TOOL_CONFIGURATION("scy/elotoolconfiguration"),
   TEMPLATES_ELOS("scy/templateelos"),
   RUNTIME_SETTINGS("scy/runtimesettings"),
   EPORTFOLIO("scy/eportfolio"),
   AGENT_MODELS("scy/agentModels"),
   PADAGOGICAL_PLAN_SETTINGS("scy/pedagogicalplansettings"),
   COLOR_SCHEMES("scy/colorschemes");

   private final String type;

   private MissionEloType(String type)
   {
      this.type = type;
   }

   public String getType()
   {
      return type;
   }
}
