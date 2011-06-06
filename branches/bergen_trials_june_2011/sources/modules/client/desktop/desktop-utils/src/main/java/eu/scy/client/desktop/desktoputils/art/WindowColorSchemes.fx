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
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */
public class WindowColorSchemes {

   def windowColorSchemeMap = new HashMap();

   init {
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.ONE,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.ONE
         mainColor: Color.rgb(129, 163, 66, 1.0)
         mainColorLight: Color.rgb(230, 245, 213, 1.0)
         secondColor: Color.rgb(80, 108, 196, 1.0)
         secondColorLight: Color.rgb(246, 246, 206, 1.0)
         thirdColor: Color.rgb(74, 0, 198, 1.0)
         thirdColorLight: Color.rgb(218, 229, 244, 1.0)
         backgroundColor: Color.rgb(239, 255, 223, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.TWO,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.TWO
         mainColor: Color.rgb(0, 94, 177, 1.0)
         mainColorLight: Color.rgb(204, 225, 219, 1.0)
         secondColor: Color.rgb(0, 201, 255, 1.0)
         secondColorLight: Color.rgb(183, 191, 255, 1.0)
         thirdColor: Color.rgb(253, 44, 243, 1.0)
         thirdColorLight: Color.rgb(255, 169, 232, 1.0)
         backgroundColor: Color.rgb(235, 255, 255, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.THREE,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.THREE
         mainColor: Color.rgb(148, 108, 79, 1.0)
         mainColorLight: Color.rgb(233, 224, 207, 1.0)
         secondColor: Color.rgb(234, 150, 118, 1.0)
         secondColorLight: Color.rgb(242, 203, 0, 1.0)
         thirdColor: Color.rgb(255, 76, 13, 1.0)
         thirdColorLight: Color.rgb(236, 233, 218, 1.0)
         backgroundColor: Color.rgb(252, 244, 221, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.FOUR,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.FOUR
         mainColor: Color.rgb(151, 92, 143, 1.0)
         mainColorLight: Color.rgb(255, 213, 222, 1.0)
         secondColor: Color.rgb(116, 225, 255, 1.0)
         secondColorLight: Color.rgb(207, 222, 255, 1.0)
         thirdColor: Color.rgb(129, 24, 207, 1.0)
         thirdColorLight: Color.rgb(220, 255, 255, 1.0)
         backgroundColor: Color.rgb(250, 238, 233, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.FIVE,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.FIVE
         mainColor: Color.rgb(95, 130, 203, 1.0)
         mainColorLight: Color.rgb(224, 231, 225, 1.0)
         secondColor: Color.rgb(193, 147, 68, 0.98039216)
         secondColorLight: Color.rgb(232, 227, 213, 1.0)
         thirdColor: Color.rgb(0, 79, 121, 1.0)
         thirdColorLight: Color.rgb(235, 239, 225, 1.0)
         backgroundColor: Color.rgb(230, 234, 255, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.SIX,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.SIX
         mainColor: Color.rgb(14, 167, 191, 1.0)
         mainColorLight: Color.rgb(225, 238, 243, 1.0)
         secondColor: Color.rgb(200, 114, 44, 1.0)
         secondColorLight: Color.rgb(228, 254, 207, 1.0)
         thirdColor: Color.rgb(255, 200, 58, 1.0)
         thirdColorLight: Color.rgb(228, 251, 255, 1.0)
         backgroundColor: Color.rgb(234, 247, 252, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.SEVEN,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.SEVEN
         mainColor: Color.rgb(52, 115, 85, 1.0)
         mainColorLight: Color.rgb(230, 226, 212, 1.0)
         secondColor: Color.rgb(126, 255, 255, 1.0)
         secondColorLight: Color.rgb(220, 226, 239, 1.0)
         thirdColor: Color.rgb(65, 175, 255, 1.0)
         thirdColorLight: Color.rgb(228, 233, 156, 1.0)
         backgroundColor: Color.rgb(252, 255, 234, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.EIGHT,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.EIGHT
         mainColor: Color.rgb(171, 0, 44, 1.0)
         mainColorLight: Color.rgb(228, 217, 233, 1.0)
         secondColor: Color.rgb(218, 65, 145, 1.0)
         secondColorLight: Color.rgb(249, 227, 211, 1.0)
         thirdColor: Color.rgb(254, 3, 130, 1.0)
         thirdColorLight: Color.rgb(237, 236, 255, 1.0)
         backgroundColor: Color.rgb(254, 233, 226, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      });
      addWindowColorScheme(eu.scy.common.scyelo.ColorSchemeId.NINE,
      WindowColorScheme {
         colorSchemeId: eu.scy.common.scyelo.ColorSchemeId.NINE
         mainColor: Color.rgb(0, 3, 115, 0.60784316)
         mainColorLight: Color.rgb(167, 224, 237, 1.0)
         secondColor: Color.rgb(236, 197, 31, 0.7294118)
         secondColorLight: Color.rgb(232, 238, 179, 0.60784316)
         thirdColor: Color.rgb(239, 130, 155, 1.0)
         thirdColorLight: Color.rgb(215, 126, 100, 0.40392157)
         backgroundColor: Color.rgb(238, 238, 247, 1.0)
         emptyBackgroundColor: Color.rgb(255, 255, 255, 1.0)
      }); }

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

public function getWindowColorSchemes(colorSchemes: List): WindowColorSchemes {
   def windowColorSchemes = WindowColorSchemes {
           }
   windowColorSchemes.setColorSchemes(colorSchemes);
   windowColorSchemes
}
