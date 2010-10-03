/*
 * WelcomeNode.fx
 *
 * Created on 20-dec-2009, 20:35:29
 */

package eu.scy.client.desktop.scydesktop.login;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author sikken
 */

// place your code here
public class WelcomeNode extends CustomNode {

   public-init var name = "????";

   def leftBorder = 15.0;

   public override function create(): Node {
      return Group {
         content:[
            Rectangle {
               x: 0, y: 0
               width: 262, height: 107
               fill: Color.TRANSPARENT
            }

            Text {
               font : Font {
                  size: 12
               }
               x: leftBorder, y: 30
               content: ##"Welcome to SCY-Lab"
            }
            Text {
               font : Font {
                  size: 12
               }
               x: leftBorder, y: 70
               content: ##"Please wait, while SCY-Lab is loading..."
            }

         ]
      };
   }
}

function run()    {
      Stage {
         title: "Welcome node test"
         scene: Scene {
            width: 300
            height: 200
            content: [
               WelcomeNode{

               }

            ]
         }
      }

   }
