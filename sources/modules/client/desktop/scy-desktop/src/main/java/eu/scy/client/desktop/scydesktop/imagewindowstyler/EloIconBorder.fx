/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

/**
 * @author SikkenJ
 */
public class EloIconBorder extends CustomNode {

   public var size = 16.0;
   public var cornerRadius = 2.5;
   public var borderSize = 1.0;
   public var borderColor = Color.RED;

   public override function create(): Node {
      Group {
         content: [
            Line {
               startX: 0, startY: 0
               endX: bind size, endY: 0
               strokeWidth: borderSize
               stroke: bind borderColor
            }
            Line {
               startX: bind size, startY: 0
               endX: bind size, endY: bind size
               strokeWidth: borderSize
               stroke: bind borderColor
            }
            Line {
               startX: cornerRadius + borderSize, startY: bind size
               endX: bind size, endY: bind size
               strokeWidth: borderSize
               stroke: bind borderColor
            }
            Line {
               startX: 0, startY: 0
               endX: 0, endY: bind size - cornerRadius - borderSize
               strokeWidth: borderSize
               stroke: bind borderColor
            }
            Arc {
               centerX: cornerRadius, centerY: bind size - cornerRadius
               radiusX: cornerRadius, radiusY: cornerRadius
               startAngle: 180, length: 90
               type: ArcType.OPEN
               fill: null
               strokeWidth: borderSize
               stroke: bind borderColor
            }
         ]
      }
   }

}
