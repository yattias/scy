/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art;

import java.awt.Color;

/**
 *
 * @author sikken
 */
public enum ScyColors
{
   green("#8db800","#c4e000","#e3eee9"),
   purple("#7243db","#b886e0","#dbd2ee"),
   orange("#ff5400","#ffa880","#f9f0e3"),
   pink("#fb06a2","#ff86cc","#eae1e7"),
   blue("#0042f1","#4080f8","#e1e5ef"),
   magenta("#0ea7bf","#90c8e0","#d6edf1"),
   brown("#9F8B55","#c0b899","#faf4e5"),
   darkBlue("#00015F","#000890","#eeeef7"),
   darkRed("#9F1938","#d02070","#f0eced"),
   darkGray("#474747","#808080","#efefef");

   public final String mainColorName;
   public final String mainColorLightName;
   public final String secondColorName;
   public final String secondColorLightName;
   public final String thirdColorName;
   public final String thirdColorLightName;
   public final String backgroundColorName;
   public final String emptyBackgroundColorName;

   private final static String defaultMainColorLightName = "#FFFFFF";
   private final static String defaultSecondColorName = "#FFFFFF";
   private final static String defaultSecondColorLightName = "#FFFFFF";
   private final static String defaultThirdColorName = "#FFFFFF";
   private final static String defaultThirdColorLightName = "#FFFFFF";
   private final static String defaultBackgroundColorName = "#f0ffed";
   private final static String defaultEmptyBackgroundColor = "#FFFFFF";

   private ScyColors(String mainColorName, String mainColorLightName, String secondColorName, String secondColorLightName, String thirdColorName, String thirdColorLightName, String backgroundColorName, String emptyBackgroundColorName)
   {
      this.mainColorName = mainColorName;
      this.mainColorLightName = mainColorLightName;
      this.secondColorName = secondColorName;
      this.secondColorLightName = secondColorLightName;
      this.thirdColorName = thirdColorName;
      this.thirdColorLightName = thirdColorLightName;
      this.backgroundColorName = backgroundColorName;
      this.emptyBackgroundColorName = emptyBackgroundColorName;
   }

   private ScyColors(String mainColorName)
   {
      this(mainColorName,defaultMainColorLightName,defaultSecondColorName,defaultSecondColorLightName,defaultThirdColorName,defaultThirdColorLightName,defaultBackgroundColorName,defaultEmptyBackgroundColor);
   }

   private ScyColors(String mainColorName, String mainColorLightName)
   {
      this(mainColorName,mainColorLightName,defaultSecondColorName,defaultSecondColorLightName,defaultThirdColorName,defaultThirdColorLightName,defaultBackgroundColorName,defaultEmptyBackgroundColor);
   }

   private ScyColors(String mainColorName, String mainColorLightName, String defaultBackgroundColorName)
   {
      this(mainColorName,mainColorLightName,defaultSecondColorName,defaultSecondColorLightName,defaultThirdColorName,defaultThirdColorLightName,defaultBackgroundColorName,defaultEmptyBackgroundColor);
   }

   private static String calculateTitleStartGradientColorName(String colorName){
      Color color = new Color(Integer.parseInt(colorName, 16));
      Color titleStartGradientColor = new Color(calculateLighterColor(color.getRed()),calculateLighterColor(color.getGreen()),calculateLighterColor(color.getBlue()));
      return Integer.toHexString(titleStartGradientColor.getRGB());
   }

   private static int calculateLighterColor(int color){
      return 255-(255-color)/2;
   }
}
