/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.common.mission.ColorSchemeId;
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


   {
      styleMappings = new StyleMappings();
      styleMappings.addStyleMapping("scy/text", "report2", ColorSchemeId.SIX);
   }

   private StyleMappings(){

   }

   private void addStyleMapping(String type, String eloIconName, ColorSchemeId colorSchemeId){
         typeNamesMap.put(type, eloIconName);
         colorSchemeIdMap.put(type, colorSchemeId);
   }

   public static String getEloIconName(String type)
   {
      final String name = styleMappings.typeNamesMap.get(type);
      if (name != null)
      {
         return name;
      }
      return type;
   }

   public static ColorSchemeId getColorSchemeId(String type)
   {
      final ColorSchemeId scyColors = styleMappings.colorSchemeIdMap.get(type);
      if (scyColors != null)
      {
         return scyColors;
      }
      return ColorSchemeId.NINE;
   }
}
