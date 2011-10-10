/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.scydesktop.tools.search.ScySearchResult;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.tools.search.ScyEloDisplayProperties;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

/**
 * @author SikkenJ
 */
public class TemplateEloListCellNode extends CustomNode {

   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public var templateElo: ScySearchResult on replace { newTemplateElo() };
   def spacing = 5.0;
   def hBox = HBox {
              spacing: spacing
           }
   def titleDisplay = Label {};
   def typeDisplay = Label {};

   public override function create(): Node {
      hBox
   }

   function newTemplateElo() {
      titleDisplay.text = templateElo.getScyElo().getTitle();
      typeDisplay.text = ScyEloDisplayProperties.getTechnicalFormatString(templateElo.getScyElo(), newEloCreationRegistry);
      hBox.content = [
                 templateElo.getEloIcon() as EloIcon,
                 VBox {
                    spacing: -1
                    content: [
                       titleDisplay,
                       typeDisplay,
                    ]
                 }
              ];
   }

}
