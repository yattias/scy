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
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import java.text.SimpleDateFormat;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.art.javafx.NoThumbnailView;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import javafx.scene.CacheHint;

/**
 * @author SikkenJ
 */
public class ExtendedScyEloDisplayNode extends CustomNode {

   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var scyElo: ScyElo on replace { newScyElo() };
   public var relevance:Number on replace { newScyElo() };
   public var eloIcon: EloIcon on replace { newEloIcon() };
   def titleDisplay = Label {};
//   def relevanceDisplay = Label {};
   def authorDisplay = Label {};
   def typeDisplay = Label {};
   def roleDisplay = Label {};
   def dateDisplay = Label {};
   def uriDisplay = Label {};
   def spacing = 5.0;
   def eloIconScale = 1.0;
   def eloIconSize = 40.0;
   def eloIconOffset = 10.0;
   def thumbnailBorder = 2.0;
   def thumbnailView = ImageView {
         layoutX: eloIconOffset
         fitWidth: ArtSource.thumbnailWidth
         fitHeight: ArtSource.thumbnailHeight
         preserveRatio: true
      }
   def noThumbnailView = NoThumbnailView {
         layoutX: eloIconOffset
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
                           x: -8
                           y: 0
                           width: eloIconScale*eloIconSize
                           height: eloIconScale*eloIconSize
                           fill: Color.TRANSPARENT
                        }
                        Group {
                           layoutX:(eloIconScale-1)*eloIconOffset-10
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
//                  relevanceDisplay,
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

   function newScyElo() {
      titleDisplay.text = "{ScyEloDisplayProperties.titleLabel} : {scyElo.getTitle()}";
//      relevanceDisplay.text = "{ScyEloDisplayProperties.relevanceLabel} : {relevance}";
      authorDisplay.text = "{ScyEloDisplayProperties.authorsLabel}: {ScyEloDisplayProperties.getAuthorsText(scyElo)}";
      typeDisplay.text = "{ScyEloDisplayProperties.formatLabel}: {ScyEloDisplayProperties.getTechnicalFormatString(scyElo,newEloCreationRegistry)}";
      //      roleDisplay.text = "{roleLabel}: {getRoleString()}";
      dateDisplay.text = "{ScyEloDisplayProperties.dateLabel}: {ScyEloDisplayProperties.getDateString(scyElo)}";
      uriDisplay.text = "URI: {ScyEloDisplayProperties.getUriString(scyElo)}";
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
//      println("eloIcon: {eloIcon.layoutBounds}");
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
