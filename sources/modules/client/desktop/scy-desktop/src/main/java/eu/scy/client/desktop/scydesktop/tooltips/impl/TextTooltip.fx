/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author SikkenJ
 */
public class TextTooltip extends CustomNode {

   public var content: String;
   public-init var arcSize = 0.0;
   public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   public override function create(): Node {
      var text = Text {
                 font: Font {
                    size: 12
                 }
                 x: 0, y: 0
                 content: content
              };
      NodeTooltip{
         content: text
         arcSize: arcSize
         windowColorScheme: windowColorScheme
      }
   }

}

function run() {

   Stage {
      title: "TextTooltip test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            TextTooltip {
               content: "testing"
               windowColorScheme: WindowColorScheme {}
               layoutX: 20
               layoutY: 20
            }
         ]
      }
   }

}


