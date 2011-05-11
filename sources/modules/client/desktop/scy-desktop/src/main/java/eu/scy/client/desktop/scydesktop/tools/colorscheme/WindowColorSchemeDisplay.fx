/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.layout.VBox;

/**
 * @author SikkenJ
 */
public class WindowColorSchemeDisplay extends CustomNode {

   public var windowColorScheme: WindowColorScheme;
   def rectWidth = 16.0;
   def rectHeight = 12.5;
   def borderWidth = 1.0;

   public override function create(): Node {
      VBox {
         visible: bind windowColorScheme != null
         spacing: 0
         content: [
            Text {
               x: 0, y: 0
               textOrigin: TextOrigin.TOP
               content: bind "{windowColorScheme.colorSchemeId}"
            }

            Group {
               content: [
                  Rectangle {
                     width: 2 * rectWidth + borderWidth
                     height: 4 * rectHeight + borderWidth
                     fill: Color.WHITE
                     stroke: Color.BLACK
                  }
                  Rectangle {
                     x: borderWidth / 2
                     y: borderWidth / 2
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.mainColor
                  }
                  Rectangle {
                     x: borderWidth / 2 + rectWidth
                     y: borderWidth / 2
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.mainColorLight
                  }
                  Rectangle {
                     x: borderWidth / 2
                     y: borderWidth / 2 + rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.secondColor
                  }
                  Rectangle {
                     x: borderWidth / 2 + rectWidth
                     y: borderWidth / 2 + rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.secondColorLight
                  }
                  Rectangle {
                     x: borderWidth / 2
                     y: borderWidth / 2 + 2 * rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.thirdColor
                  }
                  Rectangle {
                     x: borderWidth / 2 + rectWidth
                     y: borderWidth / 2 + 2 * rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.thirdColorLight
                  }
                  Rectangle {
                     x: borderWidth / 2
                     y: borderWidth / 2 + 3 * rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.backgroundColor
                  }
                  Rectangle {
                     x: borderWidth / 2 + rectWidth
                     y: borderWidth / 2 + 3 * rectHeight
                     width: rectWidth
                     height: rectHeight
                     fill: bind windowColorScheme.emptyBackgroundColor
                  }
               ]
            }
         ]
      }

   }

}
