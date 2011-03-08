/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.art;

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
   public final String titleStartGradientColorName;
   public final String backgroundColorName;
   public final String titleEndGradientColorName;
   public final String emptyBackgroundColorName;

   private final static String defaultTitleEndGradientColorName = "#FFFFFF";
   private final static String defaultBackgroundColorName = "#f0ffed";
   private final static String defaultEmptyBackgroundColor = "#FFFFFF";

   private ScyColors(String mainColorName, String titleStartGradientColorName, String backgroundColorName, String titleEndGradientColorName, String emptyBackgroundColorName)
   {
      this.mainColorName = mainColorName;
      this.titleStartGradientColorName = titleStartGradientColorName;
      this.backgroundColorName = backgroundColorName;
      this.titleEndGradientColorName = titleEndGradientColorName;
      this.emptyBackgroundColorName = emptyBackgroundColorName;
   }

   private ScyColors(String mainColorName)
   {
      this(mainColorName,mainColorName,defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
//      this(mainColorName,calculateTitleStartGradientColorName(mainColorName),defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
   }

   private ScyColors(String mainColorName, String titleStartGradientColorName)
   {
      this(mainColorName,titleStartGradientColorName,defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
   }

   private ScyColors(String mainColorName, String titleStartGradientColorName, String defaultBackgroundColorName)
   {
      this(mainColorName,titleStartGradientColorName,defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
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