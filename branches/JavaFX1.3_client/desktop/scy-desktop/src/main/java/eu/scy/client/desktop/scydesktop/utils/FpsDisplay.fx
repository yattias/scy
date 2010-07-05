/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;

/**
 * @author SikkenJ
 */
public class FpsDisplay extends CustomNode {

   public override function create(): Node {
      var fpsMeter = FpsMeter {
         }
      Group {
         content: [
            Text {
               font: Font {
                  size: 10
               }
               textOrigin: TextOrigin.TOP
               content: bind "fps: {%.1f fpsMeter.fps}"
            }
            fpsMeter
         ]
       }
    }

}
