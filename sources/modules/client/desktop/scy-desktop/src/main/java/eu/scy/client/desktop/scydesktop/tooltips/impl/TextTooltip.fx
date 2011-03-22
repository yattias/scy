/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
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
   def contentBorder = 3;
   def borderWidth = 2;

   public override function create(): Node {
      var text = Text {
                 font: Font {
                    size: 12
                 }
                 x: 0, y: 0
                 content: content
              };
      return Group {
                 cache: true
                 blocksMouse: true;
                 content: [
                    Rectangle {
                       x: text.boundsInLocal.minX - contentBorder
                       y: text.boundsInLocal.minY - contentBorder
                       width: text.boundsInLocal.width + 2 * contentBorder
                       height: text.boundsInLocal.height + 2 * contentBorder
                       fill: windowColorScheme.backgroundColor;
                       stroke: windowColorScheme.mainColor;
                       strokeWidth: borderWidth;
                    }
                    text
                 ]
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


