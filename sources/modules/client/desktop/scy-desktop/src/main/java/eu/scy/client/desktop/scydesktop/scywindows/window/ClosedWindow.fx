/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import eu.scy.client.desktop.desktoputils.art.EloImageInformation;
import eu.scy.client.desktop.desktoputils.art.FxdImageLoader;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Stack;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextOrigin;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.AnchorAttribute;

/**
 * @author SikkenJ
 */
public class ClosedWindow extends WindowElement {

   public var window: ScyWindow;
   public var scyElo: ScyElo;
   public var eloIcon: EloIcon;
   public var title = "elo title";
   public var activated = false on replace { activatedChanged() };
   public var buddiesDisplay: TitleBarBuddies;
   public-init var startDragIcon: function(e: MouseEvent ):Void;
   def titleFontsize = 11;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def bgColor = bind if (activated) windowColorScheme.emptyBackgroundColor else windowColorScheme.mainColor;
   def thumbnailView = ThumbnailView {
      windowColorScheme: windowColorScheme
      scyElo: bind scyElo
      eloIcon: bind eloIcon
      startDragIcon:startDragIcon
   }
   def windowAttributes = bind window.scyWindowAttributes on replace { windowAttributesChanged() };
   def windowAttributeGroup = Group{

   }
   def buddiesDisplayGroup = Group{
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

   function windowAttributesChanged(): Void{
      def sortedWindowAttributes = Sequences.sort(windowAttributes, null) as ScyWindowAttribute[];
      for (windowAttribute in sortedWindowAttributes){
         if (windowAttribute instanceof AnchorAttribute){
            windowAttributeGroup.content = windowAttribute;
            windowAttributeGroup.layoutX = -windowAttribute.layoutBounds.width + ThumbnailView.eloIconOffset -1;
            windowAttributeGroup.layoutY = -3;
            return;
         }
      }
   }

   public function buddiesDisplayChanged(): Void{
      FX.deferAction(updateBuddiesDisplay);
   }

   function updateBuddiesDisplay(): Void{
      def lb = buddiesDisplay.layoutBounds;
      buddiesDisplayGroup.content = buddiesDisplay;
      buddiesDisplayGroup.layoutX = -buddiesDisplay.layoutBounds.width + ThumbnailView.eloIconOffset -1;
      buddiesDisplayGroup.layoutY = ArtSource.thumbnailHeight/2-buddiesDisplay.layoutBounds.height/2 - ThumbnailView.eloIconOffset - 0;
   }

   public override function create(): Node {
      buddiesDisplayChanged();
      textBackgroundFillRect = Rectangle {
            x: 0
            y: 0
            width: bind titleText.layoutBounds.width+2
            height: bind titleText.layoutBounds.height+2
         }
      titleText = Text {
            font: textFont
            textOrigin: TextOrigin.TOP
            textAlignment: TextAlignment.CENTER
            x: 0
            y: 0
//            wrappingWidth: bind thumbnailView.layoutBounds.width;
            content: bind title;
         }
      activatedChanged();

      Group {
         content: [
            thumbnailView,
            windowAttributeGroup,
            buddiesDisplayGroup,
            Stack {
               layoutX: bind thumbnailView.layoutBounds.width/2 - titleText.layoutBounds.width/2
               layoutY: thumbnailView.layoutBounds.height + 2
               nodeHPos: HPos.CENTER
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
               title: "Mission Description"
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
