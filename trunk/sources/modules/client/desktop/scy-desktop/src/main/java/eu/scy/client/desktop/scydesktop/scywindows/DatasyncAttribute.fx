/*
 * DatasyncAttribute.fx
 *
 * Created on 09.02.2010, 11:29:49
 */

package eu.scy.client.desktop.scydesktop.scywindows;

/**
 * @author lars
 */

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.EloContour;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Transform;

/**
 * @author lars
 */
public class DatasyncAttribute extends ScyWindowAttribute {

    public-init var dragAndDropManager:DragAndDropManager;
    public-init var dragObject:Object;

    public-read def cableStartX = 3;
    public-read def cableStartY = -12;
    def logger = Logger.getLogger(this.getClass());
    def size = 16.0;
    def strokeWidth = 2;
    def defaultTitleColor = Color.WHITE;
    def defaultContentColor = Color.GRAY;
    def dragScale = 2.0;
    var titleColor = defaultTitleColor;
    var contentColor = defaultContentColor;

    def color = bind scyWindow.windowColorScheme.mainColor;

   public override function clone():DatasyncAttribute{
      DatasyncAttribute{
         dragAndDropManager: dragAndDropManager
         dragObject: dragObject
         priority: priority
      }
   }

    function mousePressed(e: MouseEvent) {
       var dragNode = dragIcon();
       dragNode.layoutX = layoutX;
       dragNode.layoutY = layoutY;
       // correct for the  "layouting" in the drag and drop manager
       dragNode.scaleX = dragScale;
       dragNode.scaleY = dragScale;
//       dragNode.translateX = -e.sceneX + e.x;
//       dragNode.translateY = -e.sceneY + e.y;
        dragAndDropManager.startDrag(dragNode, dragObject, this, e);
    };

    function mouseDragged(e: MouseEvent) {};

    function mouseReleased(e: MouseEvent) {};

    function dragIcon(): Node {
        return Group {
                    cursor: Cursor.HAND;
                    //translateY: -size - 2;
                    content: [
                SVGPath {
                  fill: null
                  stroke: bind color
                  strokeWidth: 1.5
                  content: "M2.94,10.95 Q2.47,11.36 2.02,11.95 C0.64,13.67 -0.75,16.94 9.61,15.82 C20.28,14.67 15.24,20.11 14.74,20.11 "
               },
               Rectangle {
                  fill: Color.WHITE
                  stroke: bind color
                  strokeWidth: 1.0
                  x: 13.15
                  y: 18.9
                  width: 5.73
                  height: 3.02
               },
              Rectangle {
                  transforms: [Transform.rotate(10.85, 2.40, 2.09)]
                  fill: bind color
                  stroke: null
                  x: 1.9
                  y: 0.06
                  width: 1.02
                  height: 4.07
               },
               Rectangle {
                  transforms: [Transform.rotate(10.84, 4.24, 2.44)]
                  fill: bind color
                  stroke: null
                  x: 3.73
                  y: 0.41
                  width: 1.02
                  height: 4.07
               },
               Rectangle {
                  transforms: [Transform.rotate(10.86, 6.12, 2.81)]
                  fill: bind color
                  stroke: null
                  x: 5.61
                  y: 0.77
                  width: 1.02
                  height: 4.07
               },
               SVGPath {
                  fill: bind color
                  stroke: null
                  content: "M6.26,8.66 C5.93,10.37 4.10,12.43 2.39,12.10 L2.39,12.10 C0.68,11.77 -0.26,9.18 0.07,7.47 L0.53,5.04 C0.86,3.34 0.44,3.37 3.93,4.04 L3.93,4.04 C6.47,4.53 7.07,4.40 6.75,6.11 Z "
               },
                    ]
            }
    }

    public override function create(): Node {
        return Group {
                    cursor: Cursor.HAND;
                    translateY: -size - 2;
                    blocksMouse: true;
                    content: [
               SVGPath {
                  fill: null
                  stroke: bind color
                  strokeWidth: 1.5
                  content: "M2.94,10.95 Q2.47,11.36 2.02,11.95 C0.64,13.67 -0.75,16.94 9.61,15.82 C20.28,14.67 15.24,20.11 14.74,20.11 "
               },
               Rectangle {
                  fill: Color.WHITE
                  stroke: bind color
                  strokeWidth: 1.0
                  x: 13.15
                  y: 18.9
                  width: 5.73
                  height: 3.02
               },
               Rectangle {
                  transforms: [Transform.rotate(10.85, 2.40, 2.09)]
                  fill: bind color
                  stroke: null
                  x: 1.9
                  y: 0.06
                  width: 1.02
                  height: 4.07
               },
               Rectangle {
                  transforms: [Transform.rotate(10.84, 4.24, 2.44)]
                  fill: bind color
                  stroke: null
                  x: 3.73
                  y: 0.41
                  width: 1.02
                  height: 4.07
               },
               Rectangle {
                  transforms: [Transform.rotate(10.86, 6.12, 2.81)]
                  fill: bind color
                  stroke: null
                  x: 5.61
                  y: 0.77
                  width: 1.02
                  height: 4.07
               },
               SVGPath {
                  fill: bind color
                  stroke: null
                  content: "M6.26,8.66 C5.93,10.37 4.10,12.43 2.39,12.10 L2.39,12.10 C0.68,11.77 -0.26,9.18 0.07,7.47 L0.53,5.04 C0.86,3.34 0.44,3.37 3.93,4.04 L3.93,4.04 C6.47,4.53 7.07,4.40 6.75,6.11 Z "
               },

                    ]
                    onMousePressed:mousePressed;
                    onMouseDragged:mouseDragged;
                    onMouseReleased:mouseReleased;
                /*
                onMouseClicked: function( e: MouseEvent ):Void {
                if (anchorDisplay.selectionAction != null){
                anchorDisplay.selectionAction(anchorDisplay);
                }
                else {
                selected = not selected;
                }
                },
                };*/
                }
    }
}


function run() {

   Stage {
      title: "DatasyncAttribute"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Group{
               translateX: 20;
               translateY: 40;
               content: [
                  Rectangle {
                     x: 0,
                     y: 0
                     width: 100,
                     height: 20
                     fill: Color.GRAY
                  }
                  DatasyncAttribute{
                     translateX: 10;
                     translateY: 0;
                   }
               ]
            }

         ]
      }
   }
}
