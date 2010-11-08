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
import java.text.SimpleDateFormat;
import java.util.Date;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.EloFunctionalRole;

/**
 * @author SikkenJ
 */
public class ExtendedScyEloDisplayNode extends CustomNode {

   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var scyElo: ScyElo on replace { newScyElo() };
   public var eloIcon: EloIcon;
   def titleDisplay = Label {};
   def authorDisplay = Label {};
   def typeDisplay = Label {};
   def roleDisplay = Label {};
   def dateDisplay = Label {};
   def spacing = 5.0;
   def thumbnailBorder = 2.0;
   def thumbnailView = ImageView {
         layoutX: thumbnailBorder
         layoutY: thumbnailBorder
         fitWidth: ArtSource.thumbnailWidth
         fitHeight: ArtSource.thumbnailHeight
         preserveRatio: true
      }
   def eloIconScaleWithThumbnail = 2.0;
   def eloIconScaleWithoutThumbnail = 4.0;
   def dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

   public override function create(): Node {
      HBox {
         spacing: spacing
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
               visible: bind scyElo != null
               spacing: spacing / 2
               content: [
                  titleDisplay,
                  authorDisplay,
                  typeDisplay,
//                  roleDisplay,
                  dateDisplay
               ]
            }
         ]
      }
   }

   function newScyElo() {
      titleDisplay.text = "Title: {scyElo.getTitle()}";
      authorDisplay.text = "Author(s): {getAuthorsText()}";
      typeDisplay.text = "Format: {newEloCreationRegistry.getEloTypeName(scyElo.getTechnicalFormat())}";
//      roleDisplay.text = "Role: {getRoleString(scyElo.getFunctionalRole())}";
      dateDisplay.text = "Date: created at {getDateString(scyElo.getDateCreated())}, last modified at {getDateString(scyElo.getDateLastModified())}";
      def thumbnailImage = scyElo.getThumbnail();
      if (thumbnailImage != null) {
         thumbnailView.image = SwingUtils.toFXImage(scyElo.getThumbnail());
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
      def authors = scyElo.getAuthors();
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

   function getTechnicalFormatString(technicalFormat: String): String {
      return "{newEloCreationRegistry.getEloTypeName(technicalFormat)} ({technicalFormat})"
   }

   function getRoleString(role: EloFunctionalRole): String {
      if (role == null) {
         return ##"unknown";
      }
      return role.toString();
   }

   function getDateString(millis: java.lang.Long): String {
      if (millis == null) {
         return ##"unknown"
      }
      dateFormat.format(new Date(millis))
   }

}
