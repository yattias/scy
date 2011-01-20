/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.image.ImageView;
import eu.scy.client.desktop.scydesktop.art.javafx.NoThumbnailView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.ext.swing.SwingUtils;

/**
 * @author SikkenJ
 */

public def eloIconOffset = 10.0;

public class ThumbnailView extends WindowElement {

   public var scyElo: ScyElo on replace { newScyElo() };
   public var eloIcon:EloIcon on replace oldEloIcon {eloIconChanged(oldEloIcon)};
//   def eloIconOffset = 10.0;
   def thumbnailBorder = 2.0;
   def eloIconSize = EloIcon.defaultEloIconSize;
   def thumbnailView = ImageView {
         layoutX: eloIconOffset
         fitWidth: ArtSource.thumbnailWidth
         fitHeight: ArtSource.thumbnailHeight
         preserveRatio: true
      }
   def noThumbnailView = NoThumbnailView {
         layoutX: eloIconOffset
         color: bind windowColorScheme.mainColor
      }
   var eloIconGroup: Group;

   public override function create(): Node {
      eloIconChanged(eloIcon);
      Group {
         content: [
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
               layoutX: -eloIconOffset
               layoutY: noThumbnailView.layoutBounds.maxY - eloIconSize + eloIconOffset
               content: [
                  Rectangle {
                     x: 0
                     y: 0
                     width: eloIconSize
                     height: eloIconSize
                     fill: Color.TRANSPARENT
                  }
                  eloIconGroup = Group {
                     layoutX: 0
                     layoutY: 0
                     content: [
                        eloIcon
                     ]
                  }
               ]
            }
         ]
      }
   }

   function newScyElo() {
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

   function eloIconChanged(oldEloIcon:EloIcon) {
      eloIcon.size = eloIconSize;
      delete oldEloIcon from eloIconGroup.content;
      insert eloIcon into eloIconGroup.content;
   }

}

function loadEloIcon(type: String): EloIcon {
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.blue);
   def imageLoader = FxdImageLoader {
         sourceName: ArtSource.plainIconsPackage
      };
   var name = EloImageInformation.getIconName(type);
   //   println("name: {name}");
   FxdEloIcon {
      selected: true
      fxdNode: imageLoader.getNode(name)
      windowColorScheme: windowColorScheme
   }
}

function run() {
   Stage {
      title: "Thumbnail view test"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            ThumbnailView {
               layoutX: 30
               layoutY: 10
               eloIcon: loadEloIcon("scy/mapping")
            }
            ThumbnailView {
               layoutX: 150
               layoutY: 10
            }
         ]
      }
   }

}
