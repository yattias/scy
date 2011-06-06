/*
 * AnchorDisplay.fx
 *
 * Created on 13-okt-2009, 14:24:18
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

/**
 * @author sikken
 */

import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseEventInScene;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

/**
 * @author sikkenj
 */


public class AnchorDisplay extends CustomNode {
   def size = 21.0;
   def defaultTitleColor = Color.WHITE;
   def defaultContentColor = Color.GRAY;
   public var selectedScale = 1.5;
   public var notSelectedScale = 1.25;
   public var notExistsOpacity = 0.5;
   public var positionScale = 1.0;

   public var las: LasFX;
   public var windowStyler:WindowStyler;
   public-read var xCenter = bind positionScale*las.xPos + size / 2;
   public-read var yCenter = bind positionScale*las.yPos + size / 2;
   public var selected = false on replace {
      setColors();
      eloIcon.selected = selected;
   };
   public var selectionAction: function(AnchorDisplay,MissionAnchorFX):Void;
   public var dragAndDropManager: DragAndDropManager;
   def eloIcon = windowStyler.getScyEloIcon(las.mainAnchor.scyElo);
   def anchorColor = las.mainAnchor.windowColorScheme.mainColor;
   var contentColor = defaultContentColor;

   init{
      las.color = anchorColor;
   }

   function setColors(){
      if (selected){
         contentColor = defaultTitleColor;
      }
      else {
         contentColor = las.mainAnchor.windowColorScheme.mainColor;
      }
   }

   public override function toString():String{
      "AnchorDisplay\{lasId:{las.id}\}"
   }

   public override function create(): Node {
      disable = not las.exists;
      eloIcon.selected = selected;
      centerNode(eloIcon);
      if (las.exists){
         cursor = Cursor.HAND;
      } else{
         eloIcon.opacity = notExistsOpacity;
      }
      Group {
         layoutX: bind positionScale*las.xPos;
         layoutY: bind positionScale*las.yPos;
         content: [
            eloIcon,
         ],
         onMouseClicked: function( e: MouseEvent ):Void {
            if (selectionAction != null){
               selectionAction(this,las.mainAnchor);
            }
            else {
               selected = not selected;
            }
         },
         onMousePressed:mousePressed
         onMouseDragged:mouseDragged
         onMouseReleased:mouseReleased

      };
   }

   function centerNode(node:Node):Void{
      node.layoutX = (size - node.boundsInParent.maxX - node.boundsInParent.minX)/2;
      node.layoutY = (size - node.boundsInParent.maxY - node.boundsInParent.minY)/2;
   }


   var dragging = false;
   var originalAnchorXPos:Number;
   var originalAnchorYPos:Number;
   
   function mousePressed( e: MouseEvent ):Void{
//      println("anchorDisplay.onMousePressed");
      if (not e.controlDown){
         var dragEloIcon = eloIcon.clone();
         dragEloIcon.layoutX = eloIcon.layoutX;
         dragEloIcon.layoutY = eloIcon.layoutY;
         dragAndDropManager.startDrag(dragEloIcon, las.mainAnchor.scyElo,this,e);
         return;
      }
      dragging = true;
      originalAnchorXPos = las.xPos;
      originalAnchorYPos = las.yPos;
   }

   function mouseDragged( e: MouseEvent ):Void{
//      println("anchorDisplay.onMouseDragged");
      if (not dragging){
         return;
      }
      var mouseEventInScene = MouseEventInScene{mouseEvent:e};
      las.xPos = originalAnchorXPos+mouseEventInScene.dragX/positionScale;
      las.yPos = originalAnchorYPos+mouseEventInScene.dragY/positionScale;
   }

   function mouseReleased( e: MouseEvent ):Void{
//      println("anchorDisplay.onMouseReleased");
      dragging = false;
   }

}

function run(){

//   var anchor1 = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "1";
//         xPos: 20;
//         yPos: 20;
//      }
//   }
//   var anchor2 = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "2";
//         xPos: 60;
//         yPos: 20;
//      }
//   }
//   var anchor3 = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "3";
//         xPos: 60;
//         yPos: 60;
//      }
//   }
//   var anchorg = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "g";
//         xPos: 20;
//         yPos: 100;
//      }
//   }
//   var anchorG = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "G";
//         xPos: 60;
//         yPos: 100;
//      }
//   }       //      anchor1.nextAnchors = [anchor2,anchor3];
//
//   Stage {
//      title: "Anchor test"
//      scene: Scene {
//         width: 200
//         height: 200
//         content: [
//            anchor1,
//            anchor2,
//            anchor3,
//            anchorg,
//            anchorG
//         ]
//      }
//   }
}
