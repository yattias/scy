/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.art.WindowColorSchemes;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import java.lang.String;
import eu.scy.common.mission.ColorSchemesElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ColorSchemeId;

/**
 * @author SikkenJ
 */
public class JavaFxWindowStyler extends WindowStyler {

   public var eloIconFactory: EloIconFactory;
   public var colorSchmesElo: ColorSchemesElo on replace {
         windowColorSchemes = WindowColorSchemes {
            }
         windowColorSchemes.setColorSchemes(colorSchmesElo.getTypedContent().getColorSchemes());
      }
   var windowColorSchemes: WindowColorSchemes;

   public override function getWindowColorScheme(type: String): WindowColorScheme {
      def colorSchemeId = StyleMappings.getColorSchemeId(type);
      return windowColorSchemes.getWindowColorScheme(colorSchemeId);
   }

   public override function getWindowColorScheme(scyElo: ScyElo): WindowColorScheme {
      var colorSchemeId = scyElo.getColorSchemeId();
      if (colorSchemeId==null){
         def functionalRole = scyElo.getFunctionalRole();
         if (functionalRole != null) {
            colorSchemeId = StyleMappings.getColorSchemeId(functionalRole.toString());
         }
      }
      if (colorSchemeId == null) {
         colorSchemeId = StyleMappings.getColorSchemeId(scyElo.getTechnicalFormat());
      }
      if (colorSchemeId==null){
         colorSchemeId = ColorSchemeId.NINE;
      }
      return windowColorSchemes.getWindowColorScheme(colorSchemeId);
   }

   public override function getScyEloIcon(type: String): EloIcon {
      def eloIconName = StyleMappings.getEloIconName(type);
      return eloIconFactory.createEloIcon(eloIconName);
   }

   public override function getDisplayIconType(scyElo: ScyElo): String {
      var displayIconType = scyElo.getIconType();
      if (displayIconType == null) {
         def functionalRole = scyElo.getFunctionalRole();
         if (functionalRole != null) {
            displayIconType = StyleMappings.getEloIconName(functionalRole.toString());
         }
      }
      if (displayIconType == null) {
         displayIconType = StyleMappings.getEloIconName(scyElo.getTechnicalFormat());
      }

      return displayIconType
   }

}
