/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.scyelo.EloFunctionalRole;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class StyleMappings
{
   private final static Logger logger = Logger.getLogger(StyleMappings.class);
   private static StyleMappings styleMappings;
   private final Map<String, String> typeNamesMap = new HashMap<String, String>();
   private final Map<String, ColorSchemeId> colorSchemeIdMap = new HashMap<String, ColorSchemeId>();


   static {
      styleMappings = new StyleMappings();
      // type based mappings
      styleMappings.addStyleMapping("scy/drawing", "drawing2", ColorSchemeId.ONE);
      styleMappings.addStyleMapping("scy/model", "model", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("scy/dataset", "dataset", ColorSchemeId.THREE);
      styleMappings.addStyleMapping("scy/simconfig", "model", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("scy/pds", "dataset_processed", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("scy/copex", "exp_design", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("scy/text", "report", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("scy/image", "information", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping("scy/mapping", "concept_map", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping("scy/url", "information", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping("scy/interview", "interview", ColorSchemeId.SEVEN);
      styleMappings.addStyleMapping("scy/video", "video", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping("scy/xproc", "exp_design", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("scy/ppt", "presentation", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("scy/rtf", "report", ColorSchemeId.ONE);
      styleMappings.addStyleMapping("scy/doc", "report", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("scy/skp", "drawing", ColorSchemeId.THREE);
      styleMappings.addStyleMapping("scy/mathtool", "report", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("scy/formauthor", "report", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping("scy/youtuber", "video", ColorSchemeId.SIX);
      // functional role based mappings
      styleMappings.addStyleMapping(EloFunctionalRole.ARGUMENT, "debate_argument", ColorSchemeId.ONE);
      styleMappings.addStyleMapping(EloFunctionalRole.ARTEFACT_DESIGNED, "designed_artifact", ColorSchemeId.TWO);
      styleMappings.addStyleMapping(EloFunctionalRole.DATASET, "dataset", ColorSchemeId.THREE);
      styleMappings.addStyleMapping(EloFunctionalRole.DATASET_PROCESSED, "dataset_processed", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping(EloFunctionalRole.DATA_ANALYSIS, "analyse", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping(EloFunctionalRole.DOCUMENT_DESIGN, "design_of_artifact", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.DOCUMENT_REFLECTION, "reflection", ColorSchemeId.SEVEN);
      styleMappings.addStyleMapping(EloFunctionalRole.DRAWING_DESIGN, "design_of_artifact", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping(EloFunctionalRole.EXPERIMENTAL_CONCLUSION, "conclusion", ColorSchemeId.ONE);
      styleMappings.addStyleMapping(EloFunctionalRole.EXPERIMENTAL_DESIGN, "exp_design", ColorSchemeId.TWO);
      styleMappings.addStyleMapping(EloFunctionalRole.HYPOTHESIS, "hypothese", ColorSchemeId.THREE);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_ASSIGNMENT, "assignment", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_HELP, "information2", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping(EloFunctionalRole.INFORMATION_RESOURCE, "information", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.INTERVIEW, "interview", ColorSchemeId.SEVEN);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_CONCEPT, "concept_map", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_INITIAL_IDEAS, "concept_map3", ColorSchemeId.ONE);
      styleMappings.addStyleMapping(EloFunctionalRole.MAP_KEY_IDEAS, "concept_hypothese", ColorSchemeId.TWO);
      styleMappings.addStyleMapping(EloFunctionalRole.PRESENTATION_DESIGN, "presentation", ColorSchemeId.THREE);
      styleMappings.addStyleMapping(EloFunctionalRole.PRESENTATION_FINAL, "presentation", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping(EloFunctionalRole.REPORT_EVALUATION, "evaluation_report", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping(EloFunctionalRole.REPORT_FINAL, "conclusion", ColorSchemeId.SIX);
      styleMappings.addStyleMapping(EloFunctionalRole.RESEARCH_QUESTION, "research_question", ColorSchemeId.SEVEN);
      styleMappings.addStyleMapping(EloFunctionalRole.SOURCE_DATA, "data", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping(EloFunctionalRole.STATEMENT_PROBLEM, "research_question", ColorSchemeId.ONE);
      styleMappings.addStyleMapping(EloFunctionalRole.WEB_SUMMARY, "conclusion", ColorSchemeId.TWO);
      // special type mappings
      styleMappings.addStyleMapping("special/assignment", "assignment", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("general/new", "new", ColorSchemeId.NINE);
      styleMappings.addStyleMapping("general/search", "search", ColorSchemeId.NINE);
      styleMappings.addStyleMapping("general/navigation", "navigation", ColorSchemeId.NINE);
      styleMappings.addStyleMapping("general/logo_gray", "????", ColorSchemeId.SIX);
      // ard types mappings
      styleMappings.addStyleMapping("ard/concept_map", "concept_map2", ColorSchemeId.TWO);
      styleMappings.addStyleMapping("ard/assignment", "assignment", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("ard/choices", "choices", ColorSchemeId.THREE);
      styleMappings.addStyleMapping("ard/conclusion", "conclusion", ColorSchemeId.SIX);
      styleMappings.addStyleMapping("ard/drawing", "drawing2", ColorSchemeId.ONE);
      styleMappings.addStyleMapping("ard/house", "house", ColorSchemeId.SEVEN);
      styleMappings.addStyleMapping("ard/hypothese", "hypothese2", ColorSchemeId.THREE);
      styleMappings.addStyleMapping("ard/info", "information2", ColorSchemeId.FIVE);
      styleMappings.addStyleMapping("ard/idea", "idea", ColorSchemeId.EIGHT);
      styleMappings.addStyleMapping("ard/presentation", "presentation", ColorSchemeId.FOUR);
      styleMappings.addStyleMapping("ard/report", "report", ColorSchemeId.TWO);
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

   public static void validateEloIconNames(String[] exisitingEloIconNames){
      Set<String> exisitingEloIconNamesSet = new TreeSet<String>();
      for (String name : exisitingEloIconNames){
         exisitingEloIconNamesSet.add(name.toLowerCase());
      }
      for (String eloIconName : styleMappings.typeNamesMap.values()){
         if (!exisitingEloIconNamesSet.contains(eloIconName.toLowerCase())){
            logger.warn("Unknown elo icon name defined: " + eloIconName);
         }
      }
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
