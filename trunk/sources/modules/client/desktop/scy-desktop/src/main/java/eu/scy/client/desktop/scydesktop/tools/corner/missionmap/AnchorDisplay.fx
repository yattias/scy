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
import javafx.scene.Scene;
import javafx.stage.Stage;
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

   public var las: Las;
   public var windowStyler:WindowStyler;
   public-read var xCenter = bind las.xPos + size / 2;
   public-read var yCenter = bind las.yPos + size / 2;
   public var selected = false on replace {
      setColors();
      eloIcon.selected = selected;
      eloContour.visible = selected;
   };
   public var selectionAction: function(AnchorDisplay,MissionAnchorFX):Void;
   public var dragAndDropManager: DragAndDropManager;

   def eloIcon = windowStyler.getScyEloIcon(las.mainAnchor.eloUri);
   def anchorColor = windowStyler.getScyColor(las.mainAnchor.eloUri);
   var contentColor = defaultContentColor;

	var eloContour = EloContour{
		width: size;
		height: size;
		controlLength: 5;
		borderWidth: 2;
		borderColor: anchorColor;
		fillColor: bind contentColor;
      visible:selected;
	}

   function setColors(){
      if (selected){
         contentColor = defaultTitleColor;
      }
      else {
         contentColor = las.mainAnchor.color;
      }
   }

   public override function create(): Node {
      disable = not las.exists;
      if (las.exists){
         cursor = Cursor.HAND;
      } else{
         eloIcon.opacity = 0.5;
      }

      eloIcon.translateX = (size - eloIcon.boundsInLocal.maxX - eloIcon.boundsInLocal.minX) / 2 + 0;
      eloIcon.translateY = (size - eloIcon.boundsInLocal.maxY - eloIcon.boundsInLocal.minY) / 2 + 0;
      return Group {
         layoutX: bind las.xPos;
         layoutY: bind las.yPos;
         content: [
            eloContour,
            eloIcon
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

   var dragging = false;
   var originalAnchorXPos:Number;
   var originalAnchorYPos:Number;
   function mousePressed( e: MouseEvent ):Void{
//      println("anchorDisplay.onMousePressed");
      if (not e.controlDown){
         var dragEloIcon = windowStyler.getScyEloIcon(las.mainAnchor.eloUri);
         dragEloIcon.translateX = eloIcon.translateX;
         dragEloIcon.translateY = eloIcon.translateY;
         dragEloIcon.selected = eloIcon.selected;
         var dragNode = Group{
            content:[
               EloContour{
                  width: size;
                  height: size;
                  controlLength: 5;
                  borderWidth: 2;
                  borderColor: anchorColor;
                  fillColor: contentColor;
                  visible: selected;
               }
               dragEloIcon
            ]
         }
         dragAndDropManager.startDrag(dragNode, las.mainAnchor.metadata,this,e);
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
      las.xPos = originalAnchorXPos+mouseEventInScene.dragX;
      las.yPos = originalAnchorYPos+mouseEventInScene.dragY;
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
