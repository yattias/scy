/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.control.Label;

/**
 * @author SikkenJ
 */
public class LastModifiedScyEloCellView extends CustomNode {

   public var scyElo: ScyElo on replace { scyEloChanged() };
   def titleDisplay = Label {};
   def lastModificationDisplay = Label {};
   def spacing = 5.0;

   public override function create(): Node {
      HBox {
         spacing: spacing
         content: [
            titleDisplay,
            lastModificationDisplay
         ]
      }
   }

   function scyEloChanged() {
      if (scyElo == null) {
         titleDisplay.text = "";
         lastModificationDisplay.text = "";
      }
      else {
         titleDisplay.text = scyElo.getTitle();
         lastModificationDisplay.text = "({getLastModifiedDisplay()})";
      }
   }

   function getLastModifiedDisplay(): String {
      var lastModified = scyElo.getDateLastModified();
      if (lastModified == null) {
         lastModified = scyElo.getDateCreated();
      }
      if (lastModified == null) {
         return "";
      }
      return LastModifiedDisplay.getLastModifiedDisplay(lastModified)
   }
}
