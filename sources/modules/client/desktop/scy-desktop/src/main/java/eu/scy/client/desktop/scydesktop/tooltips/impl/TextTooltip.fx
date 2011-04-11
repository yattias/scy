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
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author SikkenJ
 */
public class TextTooltip extends CustomNode {

   public var content: String;
   public var windowColorScheme: WindowColorScheme;

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


