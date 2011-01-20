/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author SikkenJ
 */
public class ClosedWindow extends WindowElement {

   public var window: ScyWindow;
   public var scyElo: ScyElo;
   public var eloIcon: EloIcon;
   public var title = "elo title";
   public var activated = false on replace { activatedChanged() };
   def titleFontsize = 11;
   def textFont = Font.font("Verdana", FontWeight.REGULAR, titleFontsize);
   def bgColor = bind if (activated) windowColorScheme.emptyBackgroundColor else windowColorScheme.mainColor;
   def textBarHeight = 15.0;
   def thumbnailView = ThumbnailView {
         windowColorScheme: windowColorScheme
         scyElo: bind scyElo
         eloIcon: bind eloIcon
      }
   var textBackgroundFillRect: Rectangle;
   var titleText: Text;

   function activatedChanged() {
      eloIcon.selected = activated;
      if (activated) {
         textBackgroundFillRect.fill = windowColorScheme.mainColor;
         titleText.fill = windowColorScheme.emptyBackgroundColor;
      } else {
         textBackgroundFillRect.fill = windowColorScheme.backgroundColor;
         titleText.fill = windowColorScheme.mainColor;
      }
   }

   public override function create(): Node {
      textBackgroundFillRect = Rectangle {
            x: 0
            y: 0
            width: bind titleText.layoutBounds.width
            height: textBarHeight
         }
      titleText = Text {
            font: textFont
            textOrigin: TextOrigin.TOP;
            x: 0
            y: 0
            content: bind title;
         }
      activatedChanged();
      Group {
         content: [
            thumbnailView,
            Group {
               layoutX: bind thumbnailView.layoutBounds.width/2 - titleText.layoutBounds.width/2
               layoutY: thumbnailView.layoutBounds.height
               content: [
                  textBackgroundFillRect,
                  titleText
               ]
            }
         ]
      }
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
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.blue);
   Stage {
      title: "ClosedWindow test"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            ClosedWindow {
               layoutX: 30
               layoutY: 10
               windowColorScheme:windowColorScheme
               eloIcon: loadEloIcon("scy/mapping")
            }
            ClosedWindow {
               layoutX: 150
               layoutY: 10
               windowColorScheme:windowColorScheme
               title: "a very, very, very, very, very, very, very, very, very long title"
            }
            ClosedWindow {
               layoutX: 30
               layoutY: 150
               windowColorScheme:windowColorScheme
               eloIcon: loadEloIcon("scy/mapping")
               activated: true
            }
            ClosedWindow {
               layoutX: 150
               layoutY: 150
               windowColorScheme:windowColorScheme
               activated: true
            }
         ]
      }
   }

}
