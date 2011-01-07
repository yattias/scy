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
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import java.text.SimpleDateFormat;
import java.util.Date;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.client.desktop.scydesktop.art.javafx.NoThumbnailView;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;

/**
 * @author SikkenJ
 */
public class ExtendedScyEloDisplayNode extends CustomNode {

   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var scyElo: ScyElo on replace { newScyElo() };
   public var eloIcon: EloIcon on replace { newEloIcon() };
   def titleDisplay = Label {};
   def authorDisplay = Label {};
   def typeDisplay = Label {};
   def roleDisplay = Label {};
   def dateDisplay = Label {};
   def uriDisplay = Label {};
   def spacing = 5.0;
   def eloIconScale = 2.5;
   def eloIconSize = 16.0;
   def eloIconOffset = 10.0;
   def thumbnailBorder = 2.0;
   def thumbnailView = ImageView {
         layoutX: eloIconOffset
         //         layoutY: thumbnailBorder
         fitWidth: ArtSource.thumbnailWidth
         fitHeight: ArtSource.thumbnailHeight
         preserveRatio: true
      }
   def noThumbnailView = NoThumbnailView {
         layoutX: eloIconOffset
      //         layoutY: 1.0
      }
   def dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

   public override function create(): Node {
      newScyElo();
      HBox {
         spacing: spacing
         content: [
            Group {
               content: bind [
                  Rectangle {
                     x: eloIconOffset - thumbnailBorder
                     y: -thumbnailBorder
                     width: ArtSource.thumbnailWidth + 2 * thumbnailBorder
                     height: ArtSource.thumbnailHeight + 2 * thumbnailBorder
                     fill: null
                     stroke: Color.GRAY
                  }
                  noThumbnailView,
                  thumbnailView,
                  Group {
                     layoutX: 0
                     layoutY: noThumbnailView.layoutBounds.maxY - eloIconScale*eloIconSize + eloIconOffset
                     content: [
                        Rectangle {
                           x: 0
                           y: 0
                           width: eloIconScale*eloIconSize
                           height: eloIconScale*eloIconSize
                           fill: Color.TRANSPARENT
                        }
                        Group {
                           layoutX:(eloIconScale-1)*eloIconOffset
                           layoutY: (eloIconScale-1)*eloIconOffset
                           scaleX: eloIconScale
                           scaleY: eloIconScale
                           content: [
                              eloIcon
                           ]
                        }
                     ]
                  }
               ]
            }
            VBox {
//               visible: bind scyElo != null
               spacing: spacing / 2
               content: [
                  titleDisplay,
                  authorDisplay,
                  typeDisplay,
                  //                  roleDisplay,
                  dateDisplay,
                  uriDisplay
               ]
            }
         ]
      }
   }

   def titleLabel = ##"Title";
   def authorsLabel = ##"Author(s)";
   def formatLabel = ##"Format";
   def roleLabel = ##"Role";
   def dateLabel = ##"Date";
   def createdAtLabel = ##"created at";
   def lastModifiedAtLabel = ##"last modified at";

   function newScyElo() {
      titleDisplay.text = "{titleLabel} : {scyElo.getTitle()}";
      authorDisplay.text = "{authorsLabel}: {getAuthorsText()}";
      typeDisplay.text = "{formatLabel}: {newEloCreationRegistry.getEloTypeName(scyElo.getTechnicalFormat())}";
      //      roleDisplay.text = "{roleLabel}: {getRoleString(scyElo.getFunctionalRole())}";
      dateDisplay.text = "{dateLabel}: {createdAtLabel} {getDateString(scyElo.getDateCreated())}, {lastModifiedAtLabel} {getDateString(scyElo.getDateLastModified())}";
      uriDisplay.text = "URI: {scyElo.getUri()}";
      def thumbnailImage = scyElo.getThumbnail();
      if (thumbnailImage != null) {
         thumbnailView.image = SwingUtils.toFXImage(scyElo.getThumbnail());
         thumbnailView.visible = true;
         noThumbnailView.visible = false;
      } else {
         thumbnailView.image = null;
         thumbnailView.visible = true;
         noThumbnailView.visible = true;
      }
   }

   function newEloIcon(): Void {
      //      eloIcon.layoutX = 0;
      //      eloIcon.layoutY = noThumbnailView.layoutBounds.maxY - 20;
      //      eloIcon.scaleX = eloIconScale;
      //      eloIcon.scaleY = eloIconScale;
      println("eloIcon: {eloIcon.layoutBounds}");
   }

   function getAuthorsText(): String {
      if (scyElo == null) {
         return ""
      }
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
      if (scyElo == null) {
         return ""
      }
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

function run() {
   Stage {
      title: "ExtendedScyEloDisplayNode test"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            ExtendedScyEloDisplayNode {
               layoutX: 10
               layoutY: 10
               eloIcon: CharacterEloIcon {
               }
            }
            ExtendedScyEloDisplayNode {
               layoutX: 120
               layoutY: 10
            }
         ]
      }
   }

}
