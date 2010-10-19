/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.ext.swing.SwingUtils;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import javafx.scene.layout.Stack;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;

/**
 * @author SikkenJ
 */
public class ExtendedScySearchResultCellNode extends CustomNode {

   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var scySearchResult: ScySearchResult on replace { newScySearchResult() };
   def scyElo = bind scySearchResult.getScyElo();
   def titleDisplay = Label {};
   def authorDisplay = Label {};
   def typeDisplay = Label {};
   def roleDisplay = Label {};
   def spacing = 5.0;
   def thumbnailBorder = 2.0;
   def thumbnailView = ImageView {
         layoutX: thumbnailBorder
         layoutY: thumbnailBorder
         fitWidth: ArtSource.thumbnailWidth
         fitHeight: ArtSource.thumbnailHeight
         preserveRatio: true
      }
   var eloIcon: EloIcon;
   def eloIconScaleWithThumbnail = 2.0;
   def eloIconScaleWithoutThumbnail = 4.0;

   public override function create(): Node {
      HBox {
         spacing: spacing
//         visible: bind scySearchResult!=null
         content: [
            Stack {
               content: bind [
                  Rectangle {
                     x: -thumbnailBorder
                     y: -thumbnailBorder
                     width: ArtSource.thumbnailWidth + 2 * thumbnailBorder
                     height: ArtSource.thumbnailHeight + 2 * thumbnailBorder
                     fill: Color.GRAY
                  }
                  thumbnailView,
                  eloIcon
               ]
            }
            VBox {
               spacing: spacing/2
               content: [
                  titleDisplay,
                  authorDisplay,
                  typeDisplay,
                  roleDisplay
               ]
            }
         ]
      }
   }

   function newScySearchResult() {
      titleDisplay.text = "title: {scyElo.getTitle()}";
      authorDisplay.text = "author(s): {getAuthorsText()}";
      typeDisplay.text = "format: {newEloCreationRegistry.getEloTypeName(scyElo.getTechnicalFormat())}";
      roleDisplay.text = "role: {scyElo.getFunctionalRole()}";
      eloIcon = (scySearchResult.getEloIcon() as EloIcon).clone();
      def thumbnailImage = scyElo.getThumbnail();
      if (thumbnailImage != null) {
         thumbnailView.image = SwingUtils.toFXImage(scySearchResult.getScyElo().getThumbnail());
         thumbnailView.visible = true;
         eloIcon.scaleX = eloIconScaleWithThumbnail;
         eloIcon.scaleY = eloIconScaleWithThumbnail;
      } else {
         thumbnailView.image = null;
         thumbnailView.visible = true;
         eloIcon.scaleX = eloIconScaleWithoutThumbnail;
         eloIcon.scaleY = eloIconScaleWithoutThumbnail;
      }

   }

   function getAuthorsText(): String {
      def authors = scySearchResult.getScyElo().getAuthors();
      var authorsText = "";
      if (authors.size() > 0) {
         authorsText = "";
         for (author in authors) {
            if (indexof author > 0) {
               authorsText = "{authorsText}, ";
            }
            authorsText = "{authorsText}{author}";
         }
      }
      authorsText
   }

}
