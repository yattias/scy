/*
 * SimpleDragAndDropManager.fx
 *
 * Created on 8-jan-2010, 10:43:12
 */
package eu.scy.client.desktop.scydesktop.draganddrop.impl;

import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseEventInScene;
import java.lang.Exception;

/**
 * @author sikken
 */
public class SimpleDragAndDropManager extends DragAndDropManager {
   def logger = Logger.getLogger(this.getClass());

   public var windowManager:WindowManager;

   def dragOpacity = 0.35;
   def dropOpacity = 0.85;

   var dropObject:Object;
   var dragNode: Node;
   var orginalDragNodeX: Number;
   var orginalDragNodeY: Number;
   var sourceNode: Node;
   var sourceNodeOnMouseDragged:function(e:MouseEvent):Void;
   var sourceNodeOnMouseReleased:function(e:MouseEvent):Void;

   var currentWindowUnderMouse:ScyWindow;

   override public function startDrag(node: Node, object: Object, source: Node, e:MouseEvent): Void {
      MouseBlocker.startMouseBlocking();
      dropObject = object;
      dragNode = node;
      sourceNode = e.node;

      dragNode.layoutX = e.sceneX - e.x;
      dragNode.layoutY = e.sceneY - e.y;

      currentWindowUnderMouse = null;
      dragNode.opacity = dragOpacity;

      var sceneContent = source.scene.content;
      insert dragNode into sceneContent;
      source.scene.content = sceneContent;

      orginalDragNodeX = dragNode.layoutX;
      orginalDragNodeY = dragNode.layoutY;

      sourceNodeOnMouseDragged = sourceNode.onMouseDragged;
      sourceNodeOnMouseReleased = sourceNode.onMouseReleased;
      sourceNode.onMouseDragged = mouseDragged;
      sourceNode.onMouseReleased = mouseReleased;
   }

   function mouseDragged(e: MouseEvent): Void {
      if (dragNode != null) {
         var mouseEventInScene = MouseEventInScene{mouseEvent:e};
         dragNode.layoutX = orginalDragNodeX + mouseEventInScene.dragX;
         dragNode.layoutY = orginalDragNodeY + mouseEventInScene.dragY;
//         println("SimpleDragAndDropManager.mouseDragged ({dragNode.layoutX},{dragNode.layoutY})");
         checkDropStatus(e);
      } else {
         logger.warn("SimpleDragAndDropManager.mouseDragged, but no dragNode!!!");
      }

   }

   function mouseReleased(e: MouseEvent): Void {
      if (dragNode != null) {
//         println("SimpleDragAndDropManager.mouseReleased");
         try{
            tryDrop(e);
         }
         finally{
            var sceneContent = dragNode.scene.content;
            delete dragNode from sceneContent;
            dragNode.scene.content = sceneContent;

            dragNode = null;

            sourceNode.onMouseDragged = sourceNodeOnMouseDragged;
            sourceNode.onMouseReleased = sourceNodeOnMouseReleased;
            sourceNode = null;
            MouseBlocker.stopMouseBlocking();
         }
      } else {
         logger.warn("SimpleDragAndDropManager.mouseReleased, but no dragNode!!!");
      }
   }

   function checkDropStatus(e: MouseEvent): Void {
      var windowUnderMouse = windowManager.getWindowUnderMouse(e.sceneX, e.sceneY);
      //println("windowUnderMouse: {windowUnderMouse.eloUri}");
      if (windowUnderMouse!=currentWindowUnderMouse){
         if (currentWindowUnderMouse!=null){
            logger.debug("mouse left {currentWindowUnderMouse.eloUri}")
         }
         currentWindowUnderMouse = windowUnderMouse;
         var dropAcceptable = false;
         if (currentWindowUnderMouse!=null){
            try{
               dropAcceptable = currentWindowUnderMouse.canAcceptDrop(dropObject);
            }
            catch (ex:Exception){
               logger.error("an exception occured while checking for the drop status of {dropObject.getClass()}", ex);
            }
         }
         if (currentWindowUnderMouse!=null){
            logger.debug("mouse entered {currentWindowUnderMouse.eloUri}, dropAcceptable: {dropAcceptable} ")
         }
         if (dropAcceptable){
            dragNode.opacity = dropOpacity;
         }
         else{
            dragNode.opacity = dragOpacity;
         }
      }

   }

   function tryDrop(e: MouseEvent): Void {
      var windowUnderMouse = windowManager.getWindowUnderMouse(e.sceneX, e.sceneY);
      if (windowUnderMouse!=null and windowUnderMouse.canAcceptDrop(dropObject)){
         windowUnderMouse.acceptDrop(dropObject);
         logger.debug("dropped on {windowUnderMouse.eloUri}");
      } else{
         logger.debug("no drop!");
      }

   }


}
