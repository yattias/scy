/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.geometry.Point2D;
import javafx.scene.shape.StrokeLineJoin;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author SikkenJ
 */
public class NodeBubble extends CustomNode {

   public var content: Node;
   public-init var arcSize = 10.0;
   public-init var arrowOffset = 12.0;
   public var arrowPosition = ArrowPosition.TOP_LEFT;
   public var arrowPoint: Point2D;
   public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def contentBorder = 3.0;
   def borderWidth = 2.0;
//   def backgroundX = bind content.boundsInLocal.minX - contentBorder - borderWidth - arcSize / 2;
//   def backgroundY = bind content.boundsInLocal.minY - contentBorder - borderWidth - arcSize / 2;
//   def backgroundWidth = bind content.boundsInLocal.width + 2 * contentBorder + 2 * borderWidth + arcSize;
//   def backgroundHeight = bind content.boundsInLocal.height + 2 * contentBorder + 2 * borderWidth + arcSize;
   def backgroundX = content.boundsInLocal.minX - contentBorder - borderWidth - arcSize / 2;
   def backgroundY = content.boundsInLocal.minY - contentBorder - borderWidth - arcSize / 2;
   def backgroundWidth = content.boundsInLocal.width + 2 * contentBorder + 2 * borderWidth + arcSize;
   def backgroundHeight = content.boundsInLocal.height + 2 * contentBorder + 2 * borderWidth + arcSize;
   public def positionBounds: Bounds = BoundingBox {
              minX: content.boundsInLocal.minX - contentBorder - borderWidth - arcSize / 2
              minY: content.boundsInLocal.minY - contentBorder - borderWidth - arcSize / 2
              width: content.boundsInLocal.width + 2 * contentBorder + 2 * borderWidth + arcSize
              height: content.boundsInLocal.height + 2 * contentBorder + 2 * borderWidth + arcSize
           }
   def arrowBackground = Polygon {
              fill: windowColorScheme.backgroundColor
           //              stroke: windowColorScheme.mainColorLight;
           //              strokeWidth: borderWidth;
           //              strokeLineJoin: StrokeLineJoin.ROUND
           }
   def arrowBackgroundBorder = Polyline {
              strokeWidth: borderWidth
              stroke: windowColorScheme.mainColorLight
              strokeLineJoin: StrokeLineJoin.ROUND
           }
   def arrowLine = Polyline {
              strokeWidth: borderWidth
              stroke: windowColorScheme.mainColor
              strokeLineJoin: StrokeLineJoin.ROUND
           }

   public function updateShape(): Void {
      def arrowPoints = ArrowPoints {
                 baseRect: positionBounds
                 arrowPosition: arrowPosition
                 arrowPoint: sceneToLocal(arrowPoint)
                 borderWidth: borderWidth
              }
      arrowBackground.points = [arrowPoints.backgroundPoint1.x, arrowPoints.backgroundPoint1.y,
                 arrowPoints.backgroundPoint2.x, arrowPoints.backgroundPoint2.y,
                 arrowPoints.backgroundPoint3.x, arrowPoints.backgroundPoint3.y];
      arrowBackgroundBorder.points = [arrowPoints.backgroundPoint1.x, arrowPoints.backgroundPoint1.y,
                 arrowPoints.backgroundPoint2.x, arrowPoints.backgroundPoint2.y,
                 arrowPoints.backgroundPoint3.x, arrowPoints.backgroundPoint3.y];
      arrowLine.points = [arrowPoints.linePoint1.x, arrowPoints.linePoint1.y,
                 arrowPoints.linePoint2.x, arrowPoints.linePoint2.y,
                 arrowPoints.linePoint3.x, arrowPoints.linePoint3.y];
   }

   public override function create(): Node {
      updateShape();
      return Group {
                 cache: true
                 blocksMouse: true;
                 content: [
                    Rectangle {
                       x: bind backgroundX
                       y: bind backgroundY
                       width: bind backgroundWidth
                       height: bind backgroundHeight
                       arcHeight: arcSize
                       arcWidth: arcSize
                       fill: windowColorScheme.backgroundColor;
                       stroke: windowColorScheme.mainColorLight;
                       strokeWidth: borderWidth;
                    }
                    Rectangle {
                       x: bind backgroundX + borderWidth
                       y: bind backgroundY + borderWidth
                       width: bind backgroundWidth - 2 * borderWidth
                       height: bind backgroundHeight - 2 * borderWidth
                       arcHeight: arcSize
                       arcWidth: arcSize
                       fill: null;
                       stroke: windowColorScheme.mainColor;
                       strokeWidth: borderWidth;
                    }
                    arrowBackground,
                    arrowBackgroundBorder,
                    arrowLine,
                    //                    Rectangle {
                    //                       x: bind backgroundX
                    //                       y: bind backgroundY
                    //                       width: bind backgroundWidth
                    //                       height: bind backgroundHeight
                    //                       fill: null;
                    //                       stroke: Color.BLACK
                    //                       strokeWidth: 0.25
                    //                    }
                    //                    Rectangle {
                    //                       x: bind backgroundX + borderWidth
                    //                       y: bind backgroundY + borderWidth
                    //                       width: bind backgroundWidth - 2 * borderWidth
                    //                       height: bind backgroundHeight - 2 * borderWidth
                    //                       fill: null;
                    //                       stroke: Color.BLACK
                    //                       strokeWidth: 0.25
                    //                    }
                    content
                 ]
              }
   }

}

function run() {
   def scale = 1.00;
   def translate = (scale - 1) * 40;
   var topLeftBubble: NodeBubble;
   var topRightBubble: NodeBubble;
   var bottomRightBubble: NodeBubble;
   var bottomLeftBubble: NodeBubble;
   Stage {
      title: "NodeBubble test"
      scene: Scene {
         width: 400
         height: 400

         fill: Color.GRAY
         content: [
            topLeftBubble = NodeBubble {
                       scaleX: scale
                       scaleY: scale
                       translateX: translate
                       translateY: translate
                       content: Text {
                          font: Font {
                             size: 12
                          }
                          x: 0, y: 0
                          content: "Hello Worlds"
                       }
                       arrowPoint: Point2D {
                          x: -20
                          y: -30
                       }

                       arrowOffset: 12
                       arcSize: 10
                       layoutX: 70
                       layoutY: 40
                    }
            topRightBubble = NodeBubble {
                       visible: scale == 1.0
                       content: Text {
                          font: Font {
                             size: 12
                          }
                          x: 0, y: 0
                          content: "Hello Worlds"
                       }
                       arrowPoint: Point2D {
                          x: 100
                          y: -30
                       }
                       arrowPosition: ArrowPosition.TOP_RIGHT
                       arrowOffset: 10
                       arcSize: 10
                       layoutX: 170
                       layoutY: 40
                    }
            bottomRightBubble = NodeBubble {
                       visible: scale == 1.0
                       content: Text {
                          font: Font {
                             size: 12
                          }
                          x: 0, y: 0
                          content: "Hello Worlds"
                       }
                       arrowPoint: Point2D {
                          x: 100
                          y: 30
                       }
                       arrowPosition: ArrowPosition.BOTTOM_RIGHT
                       arrowOffset: 10
                       arcSize: 10
                       layoutX: 170
                       layoutY: 100
                    }
            bottomLeftBubble = NodeBubble {
                       visible: scale == 1.0
                       content: Text {
                          font: Font {
                             size: 12
                          }
                          x: 0, y: 0
                          content: "Hello Worlds"
                       }
                       arrowPoint: Point2D {
                          x: -30
                          y: 30
                       }
                       arrowPosition: ArrowPosition.BOTTOM_LEFT
                       arrowOffset: 10
                       arcSize: 10
                       layoutX: 70
                       layoutY: 100
                    }
         ]
      }
   }
   topLeftBubble.updateShape();
   topRightBubble.updateShape();
   bottomRightBubble.updateShape();
   bottomLeftBubble.updateShape();

}

