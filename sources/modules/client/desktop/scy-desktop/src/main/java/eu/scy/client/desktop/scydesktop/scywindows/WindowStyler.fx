/*
 * WindowStyler.fx
 *
 * Created on 30-jun-2009, 14:35:14
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.common.scyelo.EloFunctionalRole;

/**
 * @author sikkenj
 */
public mixin class WindowStyler {

   public function getWindowColorScheme(type: String): WindowColorScheme {
      return WindowColorScheme.getWindowColorScheme(ScyColors.green);
   }

   protected function getScyIconCharacter(type: String): String {
      return "?";
   }

   public function getScyEloIcon(type: String): EloIcon {
      return CharacterEloIcon {
            iconCharacter: "?";
            color: getWindowColorScheme(type).mainColor
         };
   }

   public function getScyEloIcon(scyElo: ScyElo): EloIcon {
      def displayIconType = getDisplayIconType(scyElo);
      return getScyEloIcon(displayIconType);
   }

   public function getWindowColorScheme(scyElo: ScyElo): WindowColorScheme {
      def displayIconType = getDisplayIconType(scyElo);
      def windowColorScheme = getWindowColorScheme(displayIconType);
      return windowColorScheme;
   }

   public function style(window: ScyWindow) {
      var windowColorScheme: WindowColorScheme;
      var eloIcon: EloIcon;
      if (window.scyElo != null) {
         windowColorScheme = getWindowColorScheme(window.scyElo);
         eloIcon = getScyEloIcon(window.scyElo);
      }
      else {
         windowColorScheme = getWindowColorScheme(window.eloType);
         eloIcon = getScyEloIcon(window.eloType);
      }
      window.windowColorScheme.assign(windowColorScheme);
      window.eloIcon = eloIcon;
   }

   public function getDisplayIconType(scyElo: ScyElo) {
      var displayIconType = scyElo.getIconType();
      if (displayIconType == "") {
         displayIconType = scyElo.getTechnicalFormat();
         if (displayIconType == EloImageInformation.url.type) {
            if (EloFunctionalRole.INFORMATION_ASSIGNMENT == scyElo.getFunctionalRole()) {
               displayIconType = EloImageInformation.assignment.type
            }
         }
      }
      return displayIconType
   }

}
