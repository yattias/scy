/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils.metalthemes;

import eu.scy.common.mission.ColorScheme;
import java.awt.Color;

/**
 *
 * @author SikkenJ
 */
public class CustomMetalThemeFactory {

   public static CustomMetalTheme createCustumMetalTheme(ColorScheme colorScheme){
      CustomMetalTheme customMetalTheme = new CustomMetalTheme();
//      customMetalTheme.setColor(CustomMetalTheme.primary1, colorScheme.getMainColor());
//      customMetalTheme.setColor(CustomMetalTheme.primary2, colorScheme.getSecondColor());
//      customMetalTheme.setColor(CustomMetalTheme.primary3, colorScheme.getThirdColor());
//      customMetalTheme.setColor(CustomMetalTheme.secondary1, colorScheme.getMainColorLight());
//      customMetalTheme.setColor(CustomMetalTheme.secondary2, colorScheme.getSecondColorLight());
//      customMetalTheme.setColor(CustomMetalTheme.secondary3, colorScheme.getThirdColorLight());
//      customMetalTheme.setColor(CustomMetalTheme.white, colorScheme.getBackgroundColor());
      customMetalTheme.setColor(CustomMetalTheme.primary1, colorScheme.getMainColor());
      customMetalTheme.setColor(CustomMetalTheme.primary2, colorScheme.getSecondColor());
      customMetalTheme.setColor(CustomMetalTheme.primary3, colorScheme.getThirdColor());
      customMetalTheme.setColor(CustomMetalTheme.secondary1, colorScheme.getMainColor());
      customMetalTheme.setColor(CustomMetalTheme.secondary2, makeDisabledColor(colorScheme.getMainColor()));
      customMetalTheme.setColor(CustomMetalTheme.secondary3, colorScheme.getBackgroundColor());
      customMetalTheme.setColor(CustomMetalTheme.white, colorScheme.getBackgroundColor());
      customMetalTheme.applyScyThemeSetup();
      return customMetalTheme;
   }

   private static Color makeDisabledColor(Color color){
      return new Color(makeLighterColorComponent(color.getRed()),makeLighterColorComponent(color.getGreen()),makeLighterColorComponent(color.getBlue()),color.getAlpha());
   }

   private static int makeLighterColorComponent(int value){
      return 255 - (255-value)/2;
   }

}
