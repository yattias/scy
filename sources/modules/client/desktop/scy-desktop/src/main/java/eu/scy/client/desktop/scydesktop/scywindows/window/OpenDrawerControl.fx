/*
 * OpenDrawerControl.fx
 *
 * Created on 12-apr-2010, 20:18:39
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;

/**
 * @author sikken
 */
public class OpenDrawerControl extends WindowActiveElement, TooltipCreator {

   public var tooltipManager: TooltipManager;
   public var tooltip: String;
   public-init var mainRadius = 5.0;
   public-init var secondRadius = 3.0;
   public-init var secondRadiusOffset = 6.5;
   public var controlLetter = "R";
   public var textRotate = 0;

   protected override var scaledTranslateXCorrection2 = 0;
   protected override var scaledTranslateYCorrection2 = 0;

   override public function create(): Node {
      def mainLineLength = size - 2 * mainRadius;
      def mainSeparation = borderWidth+4.0;
      def secondSeparation = borderWidth+2.0;
      Group {
         cursor: Cursor.HAND
         content: [
            Rectangle {
               x: borderWidth/2, y: -borderWidth/2
               width: mainSeparation, height: size+borderWidth
               fill: bind windowColorScheme.backgroundColor
            }
            Arc {
               centerX: mainSeparation, centerY: mainRadius
               radiusX: mainRadius, radiusY: mainRadius
               startAngle: 0, length: 90
               type: ArcType.ROUND
               fill: bind windowColorScheme.backgroundColor
               stroke: null
            }
             Rectangle {
               x: mainSeparation, y: mainRadius
               width: mainRadius, height: mainLineLength
               fill: bind windowColorScheme.backgroundColor
            }
           Arc {
               centerX: mainSeparation, centerY: mainRadius+mainLineLength
               radiusX: mainRadius, radiusY: mainRadius
               startAngle: 270, length: 90
               type: ArcType.ROUND
               fill: bind windowColorScheme.backgroundColor
               stroke: null
            }
            Arc {
               centerX: mainSeparation, centerY: mainRadius
               radiusX: mainRadius, radiusY: mainRadius
               startAngle: 0, length: 90
               type: ArcType.OPEN
               fill: null
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderWidth
            }
            Line {
               startX: mainRadius+mainSeparation, startY: mainRadius
               endX: mainRadius+mainSeparation, endY: mainRadius + mainLineLength
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderWidth
            }
            Arc {
               centerX: mainSeparation, centerY: mainRadius+mainLineLength
               radiusX: mainRadius, radiusY: mainRadius
               startAngle: 270, length: 90
               type: ArcType.OPEN
               fill: null
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderWidth
            }
            Text {
               rotate: bind textRotate
               font:Font{
                  size: 14
               }
               x: 1.5, y: size/2+4
               content: controlLetter
               fill: bind windowColorScheme.mainColor
            }
            Group{
               visible: controlLetter==""
               content:[
                     Arc {
                        centerX: secondSeparation, centerY: secondRadiusOffset
                        radiusX: secondRadius, radiusY: secondRadius
                        startAngle: 00, length: 60
                        type: ArcType.OPEN
                        fill: null
                        stroke: bind windowColorScheme.mainColor
                        strokeWidth: borderWidthSecondLine
                     }
                     Line {
                        startX: secondRadius+secondSeparation, startY: secondRadiusOffset
                        endX: secondRadius+secondSeparation, endY: size-secondRadiusOffset
                        stroke: bind windowColorScheme.mainColor
                        strokeWidth: borderWidthSecondLine
                     }
                     Arc {
                        centerX: secondSeparation, centerY: size-secondRadiusOffset
                        radiusX: secondRadius, radiusY: secondRadius
                        startAngle: 300, length: 60
                        type: ArcType.OPEN
                        fill: null
                        stroke: bind windowColorScheme.mainColor
                        strokeWidth: borderWidthSecondLine
                     }
                  ]
            }


         ]
         onMouseEntered: function (e: MouseEvent): Void {
            highLighted = true;
            tooltipManager.onMouseEntered(e, this);
         }
         onMouseExited: function (e: MouseEvent): Void {
            highLighted = false;
            tooltipManager.onMouseExited(e);
         }
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      if (tooltip != "") {
         return TextTooltip {
                    content: tooltip
                    windowColorScheme: windowColorScheme
                 }

      }
      return null;
   }
}

function run() {
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def width = 50.0;
   def height = 50.0;
   def borderWidth = 2.0;
   def controlLength = 10.0;
   def cornerRadius = 10;
   var emptyWindow = EmptyWindow {
         width: bind width;
         height: bind height;
         controlSize: cornerRadius;
         borderWidth: borderWidth;
         windowColorScheme: windowColorScheme
      }
   var openDrawerControl = OpenDrawerControl {
         size: 30;
         borderWidth: borderWidth;
         windowColorScheme: windowColorScheme
         //         activate: activate;
         //      rotateWindow:this;
         layoutX: bind width;
         layoutY: 10;
         textRotate:90
      }
   openDrawerControl.textRotate = 180;
   def scale = 4.0;
   Stage {
      title: "Test open drawer control"
      scene: Scene {
         width: 200
         height: 200
         fill: Color.LIGHTGRAY
         content: [
            Group {
               layoutX: 20
               layoutY: 85
               scaleX: scale
               scaleY: scale
               content: [
                  emptyWindow,
                  openDrawerControl
               ]
            }
         ]
      }
   }
}
