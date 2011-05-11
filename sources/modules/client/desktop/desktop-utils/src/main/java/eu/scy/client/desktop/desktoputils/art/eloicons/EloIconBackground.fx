/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */
public class EloIconBackground extends CustomNode {

   public var size = 16.0;
   public var cornerRadius = 2.5;
   public var borderSize = 1.0;
   public var backgroundColor = Color.WHITE;

   public override function create(): Node {
      Group {
         content: [
            Rectangle {
               x: borderSize / 2, y: borderSize / 2
               width: bind size - borderSize, height: bind size - cornerRadius - borderSize
               fill: backgroundColor
            }
            Rectangle {
               x: cornerRadius + borderSize / 2, y: bind size - cornerRadius - borderSize / 2
               width: bind size - cornerRadius - borderSize, height: cornerRadius
               fill: backgroundColor
            }
            Arc {
               centerX: cornerRadius + borderSize / 2, centerY: bind size - cornerRadius - borderSize / 2
               radiusX: cornerRadius, radiusY: cornerRadius
               startAngle: 180, length: 90
               type: ArcType.ROUND
               fill: backgroundColor
            }
         ]
      }
   }

}
