/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

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
               width: size - borderSize, height: size - cornerRadius - borderSize
               fill: backgroundColor
            }
            Rectangle {
               x: cornerRadius + borderSize / 2, y: size - cornerRadius - borderSize / 2
               width: size - cornerRadius - borderSize, height: cornerRadius
               fill: backgroundColor
            }
            Arc {
               centerX: cornerRadius + borderSize / 2, centerY: size - cornerRadius - borderSize / 2
               radiusX: cornerRadius, radiusY: cornerRadius
               startAngle: 180, length: 90
               type: ArcType.ROUND
               fill: backgroundColor
            }
         ]
      }
   }

}
