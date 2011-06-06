/*
 * EmptyWindow.fx
 *
 * Created on 4-sep-2009, 16:27:39
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */

// place your code here
public class EmptyWindow extends CustomNode {

   public var width = 100.0;
   public var height = 100.0;
   public var controlSize = 10.0;
   public var borderWidth = 2.0;
   public var windowColorScheme:WindowColorScheme;
   public var borderColor = Color.color(0,70/255.0,231/255.0);
   public var backgroundColor = Color.color(240/255.0,248/255.0,220/255.0);


   public override function create(): Node {
      return Group {
         content: [
            Group{ // the white background of the window
					content: [
                  Rectangle { // top part until the arc
							x: 0,
							y: 0
							width: bind width,
							height: bind height-controlSize
							strokeWidth: borderWidth
							fill: bind windowColorScheme.backgroundColor
							stroke: null
						},
                  Rectangle { // bottom left part until the arc
							x: controlSize,
							y: bind height - controlSize
							width: bind width - controlSize,
							height: bind controlSize
							strokeWidth: borderWidth
							fill: bind windowColorScheme.backgroundColor
							stroke: null
						},
                  Arc { // the bottom left rotate arc part
							centerX: controlSize,
							centerY: bind height - controlSize,
							radiusX: controlSize,
							radiusY: controlSize
							startAngle: 180,
							length: 90
							type: ArcType.ROUND
							strokeWidth: borderWidth
							fill: bind windowColorScheme.backgroundColor
							stroke: null
						}
					]
				}
            // main border
            WindowBorder{
               width: bind width;
               height: bind height;
               controlSize: controlSize;
               borderWidth: borderWidth;
               color: bind windowColorScheme.mainColor;
            }
         ]
      };
   }
}

class WindowBorder extends CustomNode {

   var width = 100.0;
   var height = 100.0;
   var controlSize = 10.0;
   var borderWidth = 1.0;
   var color = Color.RED;


   public override function create(): Node {
      return Group {
         content: [
            Line { // the left border line
					startX: 0,
					startY: bind height - controlSize - borderWidth
					endX: 0,
					endY: 0
					strokeWidth: borderWidth
					stroke: bind color
				}
//            Line { // the top border line
//					startX: 0,
//					startY: 0
//					endX: bind width,
//					endY: 0
//					strokeWidth: borderWidth
//					stroke: bind color
//				}
            Line { // the right border line
					startX: bind width,
					startY: 0
					endX: bind width,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
				}
            Line { // the bottom border line
					startX: bind width,
					startY: bind height
					endX: bind controlSize + borderWidth,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
				}
            Arc { // the bottom left "disabled" rotate arc
					centerX: controlSize,
					centerY: bind height - controlSize,
					radiusX: controlSize,
					radiusY: controlSize
					startAngle: 180,
					length: 90
					type: ArcType.OPEN
					fill: null
					strokeWidth: borderWidth
					stroke: bind color
            }
         ]
      };
   }
}



public function run(){
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

      Stage {
      title : "test empty window"
      scene: Scene {
         width: 200
         height: 200
         fill:Color.YELLOW
         content: [
            EmptyWindow{
               translateX:10;
               translateY:10;
               controlSize:18;
               windowColorScheme:windowColorScheme
            }

         ]
      }
   }


}
