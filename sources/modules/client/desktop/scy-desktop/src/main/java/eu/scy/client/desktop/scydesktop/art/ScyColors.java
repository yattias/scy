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
   public final String colorName;

   private ScyColors(String colorName)
   {
      this.colorName = colorName;
   }

}
