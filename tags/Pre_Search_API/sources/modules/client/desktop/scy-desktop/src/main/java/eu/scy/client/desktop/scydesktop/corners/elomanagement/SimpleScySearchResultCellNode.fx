/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.layout.VBox;

/**
 * @author SikkenJ
 */
public class SimpleScySearchResultCellNode extends CustomNode {

   public var scySearchResult: ScySearchResult on replace { newScySearchResult() };
   def titleDisplay = Label {};
   def authorDisplay = Label {};
   def dateDisplay = Label {};
   def spacing = 5.0;
   def hBox = HBox {
         spacing: spacing
         content: []
      }

   public override function create(): Node {
      hBox
   }

   function newScySearchResult() {
      titleDisplay.text = scySearchResult.getScyElo().getTitle();
      authorDisplay.text = ScyEloDisplayProperties.getAuthorsText(scySearchResult.getScyElo());
      dateDisplay.text = ScyEloDisplayProperties.getDateString(scySearchResult.getScyElo());
      hBox.content = [
            scySearchResult.getEloIcon() as EloIcon,
            VBox {
               spacing: -1
               content: [
                  titleDisplay,
                  authorDisplay,
                  dateDisplay
               ]
            }
         ];
   }

}
