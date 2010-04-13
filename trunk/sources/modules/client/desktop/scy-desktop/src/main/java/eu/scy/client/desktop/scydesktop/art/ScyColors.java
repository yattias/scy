/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.art;

/**
 *
 * @author sikken
 */
public enum ScyColors
{
   green("#8db800"),
   purple("#7243db"),
   orange("#ff5400"),
   pink("#fb06a2"),
   blue("#0042f1"),
   magenta("#0ea7bf"),
   brown("#9F8B55"),
   darkBlue("#00015F"),
   darkRed("#9F1938"),
   darkGray("#474747");

   public final String mainColorName;
   public final String titleStartGradientColorName;
   public final String backgroundColorName;
   public final String titleEndGradientColorName;
   public final String emptyBackgroundColorName;

   private final static String defaultTitleEndGradientColorName = "#FFFFFF";
   private final static String defaultBackgroundColorName = "#f0f8db";
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
   }

   private ScyColors(String mainColorName, String titleStartGradientColorName)
   {
      this(mainColorName,titleStartGradientColorName,defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
   }

   private ScyColors(String mainColorName, String titleStartGradientColorName, String defaultBackgroundColorName)
   {
      this(mainColorName,titleStartGradientColorName,defaultBackgroundColorName,defaultTitleEndGradientColorName,defaultEmptyBackgroundColor);
   }
}
