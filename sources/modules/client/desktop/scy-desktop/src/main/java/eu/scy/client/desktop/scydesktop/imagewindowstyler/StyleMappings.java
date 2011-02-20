/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.scyelo.EloFunctionalRole;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SikkenJ
 */
public class StyleMappings
{

   private static StyleMappings styleMappings;
   private final Map<String, String> typeNamesMap = new HashMap<String, String>();
   private final Map<String, ColorSchemeId> colorSchemeIdMap = new HashMap<String, ColorSchemeId>();


   static {
      styleMappings = new StyleMappings();
      // type based mappings
      styleMappings.addStyleMapping("scy/drawing", "drawing2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/model", "model", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/dataset", "dataset", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/simconfig", "model", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/pds", "dataset_processed", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/copex", "exp_design", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/text", "report2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/image", "information", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/mapping", "concept_map", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/url", "information", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/interview", "interview", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/video", "video", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/xproc", "exp_design", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/ppt", "presentation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/rtf", "report2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/doc", "report2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/skp", "drawing", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/mathtool", "report2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/formauthor", "report2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/youtuber", "video", ColorSchemeId.SIX);
      // functional role based mappings
      styleMappings.addStyleMapping(EloFunctionalRole.ARGUMENT, "debate_argument", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.ARTEFACT_DESIGNED, "designed_artifact", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DATASET, "dataset", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DATASET_PROCESSED, "dataset_processed", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DATA_ANALYSIS, "analyse", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DOCUMENT_DESIGN, "design_of_artefact", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DOCUMENT_REFLECTION, "reflection", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DRAWING_DESIGN, "design_of_artefact", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.EXPERIMENTAL_CONCLUSION, "conclusion2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.EXPERIMENTAL_DESIGN, "exp_design", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.HYPOTHESIS, "hypothese", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_ASSIGNMENT, "assignment", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_HELP, "info2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_RESOURCE, "infoformation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.INTERVIEW, "interview", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_CONCEPT, "concept_map", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_INITIAL_IDEAS, "concept_map3", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_KEY_IDEAS, "concept_hypothese", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.PRESENTATION_DESIGN, "presentation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.PRESENTATION_FINAL, "presentation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.REPORT_EVALUATION, "evaluation report", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.REPORT_FINAL, "conclusion2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.RESEARCH_QUESTION, "research_question", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.SOURCE_DATA, "data", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.STATEMENT_PROBLEM, "research_question", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.WEB_SUMMARY, "conclusion2", ColorSchemeId.SIX);
      // special type mappings
      styleMappings.addStyleMapping("special/assignment", "assignment", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("general/new", "new", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("general/search", "search", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("general/navigation", "navigation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("general/logo_gray", "????", ColorSchemeId.SIX);
      // ard types mappings
      styleMappings.addStyleMapping("ard/concept_map", "concept_map2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/assignment", "assingment", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/choices", "choices2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/conclusion", "conclusion2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/drawing", "drawing2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/house", "house2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/hypothese", "hypothese2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/info", "info2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/idea", "idea2", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/presentation", "presentation", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/report", "report2", ColorSchemeId.SIX);
   }

   private StyleMappings()
   {
   }

   private void addStyleMapping(String type, String eloIconName, ColorSchemeId colorSchemeId)
   {
      typeNamesMap.put(type, eloIconName);
      colorSchemeIdMap.put(type, colorSchemeId);
   }

   private void addStyleMapping(EloFunctionalRole functionalRole, String eloIconName, ColorSchemeId colorSchemeId)
   {
      addStyleMapping(functionalRole.toString(), eloIconName, colorSchemeId);
   }

   public static String getEloIconName(String type)
   {
      if (type == null)
      {
         return null;
      }
      final String name = styleMappings.typeNamesMap.get(type);
      if (name != null)
      {
         return name;
      }
      return type;
   }

   public static ColorSchemeId getColorSchemeId(String type)
   {
      if (type == null)
      {
         return null;
      }
      final ColorSchemeId scyColors = styleMappings.colorSchemeIdMap.get(type);
      if (scyColors != null)
      {
         return scyColors;
      }
      return ColorSchemeId.NINE;
   }
}
