/*
 * EmptyBorderNode.fx
 *
 * Created on 19-apr-2010, 15:50:29
 */
package eu.scy.client.desktop.scydesktop.utils;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author sikken
 */
public class EmptyBorderNode extends CustomNode {

   public-init var borderSize = 10.0;
   public-init var content: Node;

   public override function create(): Node {
      return Group {
            content: [
               Rectangle {
                  x: 0, y: 0
                  width: content.layoutBounds.maxX+2*borderSize, height: content.layoutBounds.maxY+2*borderSize
                  fill: Color.TRANSPARENT
               }
               Group{
                  layoutX:borderSize
                  layoutY:borderSize
                  content:content
               }
            ]
         };
   }

}
