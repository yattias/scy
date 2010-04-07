/*
 * PleaseWait.fx
 *
 * Created on 14-jan-2010, 11:17:49
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.TextOrigin;

/**
 * @author sikken
 */
// place your code here
public class PleaseWait extends CustomNode {
   def border = 10.0;

   public override function create(): Node{
      var text = Text {
                       font: Font {
                          size: 14
                       }
                       x: border, y: border
                       textOrigin:TextOrigin.TOP
                       content: ##"Loading, please wait..."
                    }
       return  Group{
                 content:
                 [
                    Rectangle {
                        x: 0, y: 0
                        width: text.boundsInLocal.width+2*border, height: text.boundsInLocal.height+2*border
                        fill: Color.WHITE
                     }
                     text
                 ]
              };
   }
}

function run() {
   Stage {
      title: "Test PleaseWait"
      scene: Scene {
         width: 200
         height: 200
         content: [PleaseWait {}]
      }
   }

}
