/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import javafx.util.Sequences;

/**
 * @author SikkenJ
 */
public class TitleBarWindowAttributes extends TitleBarItemList {

   public var scyWindowAttributes: ScyWindowAttribute[] on replace { updateItems() };

   override function updateItems(): Void {
      delete  displayBox.content;
      def sortedScyWindowAttributes =
              Sequences.sort(scyWindowAttributes, null) as ScyWindowAttribute[];
      displayBox.content =
              for (scyWindowAttribute in reverse sortedScyWindowAttributes) {
                 scyWindowAttribute.clone();
              }
      itemListChanged();
   }

}
