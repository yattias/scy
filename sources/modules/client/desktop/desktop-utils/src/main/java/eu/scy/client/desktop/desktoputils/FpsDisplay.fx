/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.layout.Stack;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;

/**
 * @author SikkenJ
 */
public class FpsDisplay extends CustomNode {
   def goodRate = 30.0;

   public override function create(): Node {
      var fpsMeter = FpsMeter {
         }
      var bgRect = Rectangle {
            x: 0, y: 0
            width: 50, height: 20
            fill: bind Color.hsb(fpsToHue(fpsMeter.fps), 1, 1)
         }

      Stack {
         content: [
            bgRect,
            Text {
               font: Font {
                  size: 12
               }
               textOrigin: TextOrigin.TOP
               content: bind "fps: {%.1f fpsMeter.fps}"
            }
            fpsMeter
         ]
       }
    }

    function fpsToHue(rate:Number):Number{
       var hue = rate/goodRate*100;
       return Math.max(0, Math.min(100,hue))
    }
}
