/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.tools.search.ScyEloDisplayProperties;
import eu.scy.common.mission.ArchivedElo;

/**
 * @author SikkenJ
 */

public abstract class AbstractArchivedEloListCellNode extends CustomNode {

   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public var archivedElo: ArchivedElo on replace { newArchivedElo() };
   def spacing = 5.0;
   def hBox = HBox {
              spacing: spacing
           }
   def emptyDisplay = Label {};
   def titleDisplay = Label {};
   def typeDisplay = Label {};

   public override function create(): Node {
      hBox
   }

   function newArchivedElo(): Void {
      if (archivedElo==null){
         hBox.content = []
      }

      hBox.content = [
                 archivedElo.getEloIcon() as EloIcon,
                 VBox {
                    spacing: -1
                    content: getDetailsList()
                 }
              ];
   }

   protected function getDetailsList(): Node[] {
      titleDisplay.text = archivedElo.getScyElo().getTitle();
      typeDisplay.text = ScyEloDisplayProperties.getTechnicalFormatString(archivedElo.getScyElo(), newEloCreationRegistry);
      [
         titleDisplay,
         typeDisplay,
      ]
   }

}
