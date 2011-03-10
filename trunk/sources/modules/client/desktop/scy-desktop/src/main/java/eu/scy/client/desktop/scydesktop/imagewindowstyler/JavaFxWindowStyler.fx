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
import eu.scy.client.desktop.scydesktop.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.StyleMappings;
import eu.scy.common.scyelo.ScyElo;
import java.lang.String;
import org.apache.log4j.Logger;

/**
 * @author SikkenJ
 */
public class JavaFxWindowStyler extends WindowStyler {

   def logger = Logger.getLogger(JavaFxWindowStyler.getClass());
   public var eloIconFactory: EloIconFactory;
   public var colorSchemesElo: ColorSchemesElo on replace {
         windowColorSchemes = WindowColorSchemes {
            }
         if (colorSchemesElo != null) {

            windowColorSchemes.setColorSchemes(colorSchemesElo.getTypedContent().getColorSchemes());
         }
      }
   var windowColorSchemes: WindowColorSchemes;

   init{
      StyleMappings.validateEloIconNames(eloIconFactory.getNames());
   }

   public override function getWindowColorScheme(type: String): WindowColorScheme {
      def colorSchemeId = StyleMappings.getColorSchemeId(type);
      return windowColorSchemes.getWindowColorScheme(colorSchemeId);
   }

   public override function getWindowColorScheme(scyElo: ScyElo): WindowColorScheme {
      var colorSchemeId: ColorSchemeId = null;
      if (scyElo != null) {
         colorSchemeId = scyElo.getColorSchemeId();
         if (colorSchemeId == null) {
            def functionalRole = scyElo.getFunctionalRole();
            if (functionalRole != null) {
               colorSchemeId = StyleMappings.getColorSchemeId(functionalRole.toString());
            }
         }
         if (colorSchemeId == null) {
            colorSchemeId = StyleMappings.getColorSchemeId(scyElo.getTechnicalFormat());
         }
      }
      if (colorSchemeId == null) {
         logger.warn("failed to find colorSchemeId for scyElo:uri:{scyElo.getUri()}, type:{scyElo.getTechnicalFormat()}, role:{scyElo.getFunctionalRole()}, colorSchemeId:{scyElo.getColorSchemeId()}");
         colorSchemeId = ColorSchemeId.NINE;
      }
      return windowColorSchemes.getWindowColorScheme(colorSchemeId);
   }

   public override function getScyEloIcon(scyElo: ScyElo): EloIcon {
      def displayIconType = getDisplayIconType(scyElo);
      def eloIcon = getScyEloIcon(displayIconType);
      eloIcon.windowColorScheme = getWindowColorScheme(scyElo);
      if (eloIcon instanceof LogoEloIcon){
         logger.warn("failed to find eloIcon for scyElo:uri:{scyElo.getUri()}, type:{scyElo.getTechnicalFormat()}, role:{scyElo.getFunctionalRole()}, iconTYpe:{scyElo.getIconType()}");
      }
      return eloIcon;
   }

   public override function getScyEloIcon(type: String): EloIcon {
      def eloIconName = StyleMappings.getEloIconName(type);
      def eloIcon = eloIconFactory.createEloIcon(eloIconName);
//      println("getScyEloIcon({type}) -> {eloIconName} -> {eloIcon}");
      eloIcon.windowColorScheme = getWindowColorScheme(type);
      if (eloIcon instanceof LogoEloIcon){
         logger.warn("failed to find elo icon for type: {type} -> eloIconName: {eloIconName}");
      }
      return eloIcon
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
