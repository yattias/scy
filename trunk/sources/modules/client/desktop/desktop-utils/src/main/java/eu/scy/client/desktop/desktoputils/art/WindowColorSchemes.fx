/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art;

import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.mission.ColorScheme;
import java.util.HashMap;
import javafx.util.Sequences;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SikkenJ
 */
public class WindowColorSchemes {

   def windowColorSchemeMap = new HashMap();

   init {
      addWindowColorScheme(ColorSchemeId.ONE, WindowColorScheme.getWindowColorScheme(ScyColors.green));
      addWindowColorScheme(ColorSchemeId.TWO, WindowColorScheme.getWindowColorScheme(ScyColors.purple));
      addWindowColorScheme(ColorSchemeId.THREE, WindowColorScheme.getWindowColorScheme(ScyColors.orange));
      addWindowColorScheme(ColorSchemeId.FOUR, WindowColorScheme.getWindowColorScheme(ScyColors.pink));
      addWindowColorScheme(ColorSchemeId.FIVE, WindowColorScheme.getWindowColorScheme(ScyColors.blue));
      addWindowColorScheme(ColorSchemeId.SIX, WindowColorScheme.getWindowColorScheme(ScyColors.magenta));
      addWindowColorScheme(ColorSchemeId.SEVEN, WindowColorScheme.getWindowColorScheme(ScyColors.brown));
      addWindowColorScheme(ColorSchemeId.EIGHT, WindowColorScheme.getWindowColorScheme(ScyColors.darkRed));
      addWindowColorScheme(ColorSchemeId.NINE, WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue));
   }

   public function getWindowColorScheme(colorSchemeId: ColorSchemeId): WindowColorScheme {
      return windowColorSchemeMap.get(colorSchemeId) as WindowColorScheme
   }

   function addWindowColorScheme(colorSchemeId: ColorSchemeId, windowColorScheme: WindowColorScheme): Void {
      windowColorScheme.colorSchemeId = colorSchemeId;
      windowColorSchemeMap.put(colorSchemeId, windowColorScheme);
   }

   public function getAllWindowColorSchemes(): WindowColorScheme[] {
      var colorSchemeIds = for (key in windowColorSchemeMap.keySet()) {
            key as ColorSchemeId
         }
      colorSchemeIds = Sequences.sort(colorSchemeIds) as ColorSchemeId[];
      for (colorSchemeId in colorSchemeIds) {
         getWindowColorScheme(colorSchemeId)
      }
   }

   public function getColorSchemes(): List {
      def windowColorSchemeList = new ArrayList();
      for (windowColorScheme in getAllWindowColorSchemes()) {
         windowColorSchemeList.add(windowColorScheme.getColorScheme())
      }
      windowColorSchemeList
   }

   public function setColorSchemes(colorSchemes: List): Void {
      windowColorSchemeMap.clear();
      for (colorSchemeObject in colorSchemes) {
         def windowColorScheme = WindowColorScheme {
            }
         windowColorScheme.setColorScheme(colorSchemeObject as ColorScheme);
         addWindowColorScheme(windowColorScheme.colorSchemeId, windowColorScheme)
      }
   }

}

public function getStandardWindowColorSchemes(): WindowColorSchemes {
   def windowColorSchemes = WindowColorSchemes {
      }
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.ONE, WindowColorScheme.getWindowColorScheme(ScyColors.green));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.TWO, WindowColorScheme.getWindowColorScheme(ScyColors.purple));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.THREE, WindowColorScheme.getWindowColorScheme(ScyColors.orange));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.FOUR, WindowColorScheme.getWindowColorScheme(ScyColors.pink));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.FIVE, WindowColorScheme.getWindowColorScheme(ScyColors.blue));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.SIX, WindowColorScheme.getWindowColorScheme(ScyColors.magenta));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.SEVEN, WindowColorScheme.getWindowColorScheme(ScyColors.brown));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.EIGHT, WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue));
//   windowColorSchemes.addWindowColorScheme(ColorSchemeId.NINE, WindowColorScheme.getWindowColorScheme(ScyColors.darkGray));

   windowColorSchemes
}

public function getWindowColorSchemes(colorSchemes: List): WindowColorSchemes{
   def windowColorSchemes = WindowColorSchemes {
      }
   windowColorSchemes.setColorSchemes(colorSchemes);
   windowColorSchemes
}
