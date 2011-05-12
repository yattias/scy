/*
 * EmptyBorderNode.fx
 *
 * Created on 19-apr-2010, 15:50:29
 */
package eu.scy.client.desktop.desktoputils;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author sikken
 */
public class EmptyBorderNode extends CustomNode {

   public-init var borderSize = 10.0;
   public-init var content: Node;
   public-init var widthCorrection = 0.0;
   public-init var heightCorrection = 0.0;

   public override function create(): Node {
      if (content instanceof Parent){
         var parent = content as Parent;
         parent.layout();
      }
//      println("EmptyBorderNode.content.layoutBounds:  {content.layoutBounds}");
//      println("EmptyBorderNode.content.boundsInLocal: {content.boundsInLocal}");
      return Group {
            content: [
               Rectangle {
                  x: 0, y: 0
                  width: content.layoutBounds.maxX+2*borderSize + widthCorrection,
                  height: content.layoutBounds.maxY+2*borderSize + heightCorrection
                  fill: Color.TRANSPARENT
//                  fill: Color.YELLOW
               }
//               Rectangle {
//                  x: borderSize, y: borderSize
//                  width: content.layoutBounds.maxX+widthCorrection
//                  height: content.layoutBounds.maxY+heightCorrection
//                  fill: Color.GREEN
//               }
               Group{
                  layoutX:borderSize
                  layoutY:borderSize
                  content:content
               }
            ]
         };
   }

}
