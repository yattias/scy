/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowClose;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

/**
 * @author SikkenJ
 */
public class MoreInfoWindow extends CustomNode {

   public var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   public var width = 300.0;
   public var height = 200.0;
   public var title = "Title";
   public var content: Node;
   public var closeAction: function(): Void;
   def borderLineWidth = 2.0;
   def borderWidth = 5.0;
   def iconSize = 40.0;
   def closeSize = 10.0;
   def topBorderWidth = 2 * borderWidth + iconSize;
   def titleHeight = 16.0;
   def textInset = 3.0;
   def icon: EloIcon = CharacterEloIcon {
         iconSize: iconSize;
         fontSize: 32
         iconGap: 7
         color: bind windowColorScheme.mainColor
         iconCharacter: bind title.substring(0, 1)
         selected: true
         layoutX: borderWidth
         layoutY: borderWidth
      }
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def titleBar = Group {
         layoutX: iconSize + 2 * borderWidth
         layoutY: borderWidth + (iconSize - titleHeight)/2.0 + 3
         content: [
            Rectangle {
               width: bind width - iconSize - 3 * borderWidth
               height: titleHeight
               fill: bind windowColorScheme.mainColor
            }
            Text {
               font: textFont
               x: textInset, y: titleHeight - textInset
               content: bind title
               fill: bind windowColorScheme.backgroundColor
            }
         ]
      }
   def windowClose = WindowClose {
         windowColorScheme: windowColorScheme
         layoutX: bind width - borderWidth - closeSize
         layoutY: borderWidth
         activated: false
         size: closeSize
         closeAction:closeAction
      }
   def contentElement = WindowContent {
         windowColorScheme: bind windowColorScheme
         width: bind width - 2 * borderWidth - borderLineWidth
         height: bind height - topBorderWidth - borderWidth - borderLineWidth
         content: bind content;
         //            activated: bind activated;
         //            activate: activate
         layoutX: borderWidth + borderLineWidth / 2.0;
         layoutY: topBorderWidth + borderLineWidth / 2.0;
      }

   public override function create(): Node {
      Group {
         content: [
            Rectangle {
               x: 0, y: 0
               width: bind width, height: bind height
               fill: bind windowColorScheme.backgroundColor
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderLineWidth
            }
            icon,
            titleBar,
            windowClose,
            contentElement
         ]
      }
   }

}

function run() {
   def spacing = 5.0;
   def moreInfoWindow = MoreInfoWindow {
         layoutX: 20
         layoutY: 40
         windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue)
         content: Rectangle {
            width: 500
            height: 500
            fill: Color.YELLOW
         }
      }
   Stage {
      title: "test more info window"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            moreInfoWindow,
            HBox {
               spacing: spacing
               padding: Insets {
                  top: spacing
                  left: spacing
               }
               content: [
                  Button {
                     text: "Gray"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray)
                     }
                  }
                  Button {
                     text: "Dark blue"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue)
                     }
                  }
                  Button {
                     text: "Pink"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.pink)
                     }
                  }
               ]
            }
         ]
      }
   }

}

