/*
 * ColoredTextTooltip.fx
 *
 * Created on 1-feb-2010, 16:12:28
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.CustomNode;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author sikken
 */

public class ColoredTextTooltip extends CustomNode {
   public var content:String;
   public var color:Color;
   public var backgroundOpacity = 0.95;

   def contentBorder = 3;
   def borderWidth = 2;

   public override function create(): Node {
      var text = Text {
               font : Font {
                  size: 12
               }
               x: 0, y: 0
               content:
                  content
            };
      var backgroundColor = Color{
         red:color.red
         green:color.green
         blue:color.blue
         opacity:backgroundOpacity
      }
      return Group{
         cache:true
         blocksMouse:true;
         content:[
            Rectangle {
               x: text.boundsInLocal.minX-contentBorder
               y: text.boundsInLocal.minY-contentBorder
               width: text.boundsInLocal.width+2*contentBorder
               height: text.boundsInLocal.height+2*contentBorder
               fill: Color.WHITE;
               stroke:color;
               strokeWidth:borderWidth;
            }
            text
         ]
      }
   }

}

function run(){

   Stage {
      title : "AnchorDisplayTooltip test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            ColoredTextTooltip{
               content:"testing"
               color:Color.GREEN
               layoutX:20
               layoutY:20
            }

         ]
      }
   }


}


