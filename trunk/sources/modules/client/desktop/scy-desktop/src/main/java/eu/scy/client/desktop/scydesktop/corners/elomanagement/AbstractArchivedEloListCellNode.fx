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
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import javafx.scene.Group;

/**
 * @author SikkenJ
 */
public abstract class AbstractArchivedEloListCellNode extends CustomNode {

   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public var archivedElo: ArchivedElo on replace { newArchivedElo() };
   def spacing = 5.0;
   def emptyDisplay = Label {};
   def titleDisplay = Label {};
   def typeDisplay = Label {};
   def eloIconGroup = Group {};

   public override function create(): Node {
      HBox {
         spacing: spacing
         content: [
            eloIconGroup,
            VBox {
               spacing: -1
               content: [
                  getDetailsList()
               ]
            }
         ]
      }
   }

   protected function getDetailsList(): Node[] {
      [
         titleDisplay,
         typeDisplay,
      ]
   }

   function newArchivedElo(): Void {
      if (archivedElo != null) {
         updateForArchievedElo()
      } else {
         updateNotForArchievedElo
      };
   }

   protected function updateForArchievedElo(): Void {
      eloIconGroup.content = archivedElo.getEloIcon() as EloIcon;
      titleDisplay.text = archivedElo.getScyElo().getTitle();
      typeDisplay.text = ScyEloDisplayProperties.getTechnicalFormatString(archivedElo.getScyElo(), newEloCreationRegistry);
   }

   protected function updateNotForArchievedElo(): Void {
      eloIconGroup.content = Rectangle {
                 x: 0, y: 0
                 width: ArtSource.thumbnailWidth, height: ArtSource.thumbnailHeight
                 fill: Color.TRANSPARENT
              }
      titleDisplay.text = "";
      typeDisplay.text = ""
   }

}
