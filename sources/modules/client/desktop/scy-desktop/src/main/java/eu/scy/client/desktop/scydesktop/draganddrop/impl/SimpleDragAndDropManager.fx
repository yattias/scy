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
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseEventInScene;
import java.lang.Exception;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import java.lang.IllegalArgumentException;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.util.Math;
import javafx.scene.shape.Line;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;

/**
 * @author sikken
 */

public-read var dragAndDropLayer = Group{};

public class SimpleDragAndDropManager extends DragAndDropManager {

   def logger = Logger.getLogger(this.getClass());
   public var windowManager: WindowManager;
   public var bubbleManager: BubbleManager;
   var dropTargets: Node[];
   def dragOpacity = 0.35;
   def dropOpacity = 0.85;
   var dropObject: Object;
   var dragNode: Group;
   var orginalDragNodeX: Number;
   var orginalDragNodeY: Number;
   var sourceNode: Node;
   var sourceNodeOnMouseDragged: function(e: MouseEvent): Void;
   var sourceNodeOnMouseReleased: function(e: MouseEvent): Void;
   var mousehasBeenDragged = false;
   var currentDropTargetUnderMouse: DropTarget;
   var needToCallDropLeft = false;
   var forbiddenNode : Group;

   override public function startDrag(node: Node, object: Object, source: Node, e: MouseEvent): Void {
      MouseBlocker.startMouseBlocking();
      bubbleManager.pauze();
      def sqrt2 = 0.707106781;

      def redCircle = Circle {
        centerX: node.boundsInParent.width / 2, centerY: node.boundsInParent.height / 2
        radius: Math.max(node.boundsInParent.width, node.boundsInParent.height) / 2 + 10
        fill: Color.TRANSPARENT
        stroke: Color.RED
        strokeWidth: 8
        cache: true
      };
      def redLine = Line {
        startX: redCircle.centerX + sqrt2 * redCircle.radius,  startY: redCircle.centerY - sqrt2 * redCircle.radius
        endX: redCircle.centerX - sqrt2 * redCircle.radius,  endY: redCircle.centerY + sqrt2 * redCircle.radius
        strokeWidth: 8
        stroke: Color.RED
      };

      forbiddenNode = Group {
            content: [
                redCircle,
                redLine
            ]
            translateX: node.translateX
            translateY: node.translateY
            visible: true
            opacity: 0.7
      };


      dropObject = object;
      dragNode = Group {
            content: [
                node, forbiddenNode
            ]
      }
      sourceNode = e.node;
      dragNode.opacity = 0.0;

      currentDropTargetUnderMouse = null;
      needToCallDropLeft = false;

      dragNode.layoutX = e.sceneX - e.x;
      dragNode.layoutY = e.sceneY - e.y;

      orginalDragNodeX = dragNode.layoutX;
      orginalDragNodeY = dragNode.layoutY;

      sourceNodeOnMouseDragged = sourceNode.onMouseDragged;
      sourceNodeOnMouseReleased = sourceNode.onMouseReleased;
      sourceNode.onMouseDragged = mouseDragged;
      sourceNode.onMouseReleased = mouseReleased;
      mousehasBeenDragged = false;
   }

   function realStartDrag(): Void {

   }

   function mouseDragged(e: MouseEvent): Void {
      if (dragNode != null) {
         if (not mousehasBeenDragged) {
            dragNode.opacity = dragOpacity;

//            var sceneContent = sourceNode.scene.content;
//            insert dragNode into sceneContent;
//            sourceNode.scene.content = sceneContent;

//            insert dragNode into sourceNode.scene.content;
            insert dragNode into dragAndDropLayer.content;
         }

         mousehasBeenDragged = true;
         var mouseEventInScene = MouseEventInScene { mouseEvent: e };
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
         try {
            if (mousehasBeenDragged) {
               tryDrop(e);
            }
         }
         catch (ex: Exception) {
            logger.error("an exception occured while trying to drop {dropObject.getClass()}", ex);
         }
         finally {
            if (needToCallDropLeft and currentDropTargetUnderMouse instanceof DropTarget2) {
               try {
                  (currentDropTargetUnderMouse as DropTarget2).dropLeft(dropObject);
               }
               catch (ex: Exception) {
                  logger.error("an exception occured while calling dropLeft of {getDropTargetDescription(currentDropTargetUnderMouse)}", ex);
               }
               needToCallDropLeft = false;
            }

//            var sceneContent = dragNode.scene.content;
//            delete dragNode from sceneContent;
//            dragNode.scene.content = sceneContent;
            delete dragNode from dragAndDropLayer.content;

            dragNode = null;
            dropObject = null;

            sourceNode.onMouseDragged = sourceNodeOnMouseDragged;
            sourceNode.onMouseReleased = sourceNodeOnMouseReleased;
            sourceNode = null;
            MouseBlocker.stopMouseBlocking();
         }
      } else {
         logger.warn("SimpleDragAndDropManager.mouseReleased, but no dragNode!!!");
      }
      bubbleManager.resume();
   }

   function checkDropStatus(e: MouseEvent): Void {
      def dropTarget = findDropTarget(e);
      if (dropTarget != currentDropTargetUnderMouse) {
         if (currentDropTargetUnderMouse != null) {
            if (currentDropTargetUnderMouse instanceof DropTarget2) {
               try {
                  (currentDropTargetUnderMouse as DropTarget2).dropLeft(dropObject);
               }
               catch (ex: Exception) {
                  logger.error("an exception occured while calling dropLeft of {getDropTargetDescription(currentDropTargetUnderMouse)}", ex);
               }
               needToCallDropLeft = false;
            }
            logger.debug("mouse left {getDropTargetDescription(currentDropTargetUnderMouse)}")
         }
         var dropAcceptable = false;
         currentDropTargetUnderMouse = dropTarget;
         if (currentDropTargetUnderMouse != null) {
            try {
               dropAcceptable = currentDropTargetUnderMouse.canAcceptDrop(dropObject);
            }
            catch (ex: Exception) {
               logger.error("an exception occured while checking for the drop status of {getDropTargetDescription(currentDropTargetUnderMouse)}", ex);
            }
         }
         if (currentDropTargetUnderMouse != null) {
            if (currentDropTargetUnderMouse instanceof DropTarget2) {
               try {
                  (currentDropTargetUnderMouse as DropTarget2).dropEntered(dropObject, dropAcceptable);
               }
               catch (ex: Exception) {
                  logger.error("an exception occured while calling dropEntered of {getDropTargetDescription(currentDropTargetUnderMouse)}", ex);
               }
               needToCallDropLeft = true;
            }

            logger.debug("mouse entered {getDropTargetDescription(currentDropTargetUnderMouse)}, dropAcceptable: {dropAcceptable} ")
         }
         if (dropAcceptable) {
            dragNode.opacity = dropOpacity;
            forbiddenNode.visible = false;
         } else {
            dragNode.opacity = dropOpacity;
            forbiddenNode.visible = true;
         }
      } else if (dropTarget == null) {
         forbiddenNode.visible = false;
         dragNode.opacity = dragOpacity;
      }
   }

   function tryDrop(e: MouseEvent): Void {
      def dropTarget = findDropTarget(e);
      if (dropTarget != null) {
         try {
            if (dropTarget.canAcceptDrop(dropObject)) {
               try {
                  dropTarget.acceptDrop(dropObject);
                  logger.debug("dropped on {getDropTargetDescription(dropTarget)}");
               }
               catch (ex1: Exception) {
                  logger.error("an exception occured while dropping on {getDropTargetDescription(currentDropTargetUnderMouse)}", ex1);
               }
            } else {
               logger.debug("dropped refused by {getDropTargetDescription(dropTarget)}");
            }
         }
         catch (ex2: Exception) {
            logger.error("an exception occured while checking for the drop status of {getDropTargetDescription(currentDropTargetUnderMouse)}", ex2);
         }
      } else {
         logger.debug("no drop target!");
      }
   }

   public override function addDropTaget(node: Node): Void {
      if (not (node instanceof DropTarget)) {
         throw new IllegalArgumentException("node must implement the DropTarget interface: {node.getClass()}");
      }
      if (Sequences.indexOf(dropTargets, node) < 0) {
         insert node into dropTargets;
      }
   }

   function findDropTarget(e: MouseEvent): DropTarget {
      for (node in dropTargets) {
         def nodeMouseLoc = node.sceneToLocal(e.sceneX, e.sceneY);
         if (node.contains(nodeMouseLoc)) {
            def dropTarget = node as DropTarget;
            return dropTarget;
         }
      }
      var windowUnderMouse = windowManager.getWindowUnderMouse(e.sceneX, e.sceneY);
      if (windowUnderMouse != null) {
         return windowUnderMouse as DropTarget;
      }
      return null;
   }

   function getDropTargetDescription(dropTarget: DropTarget): String {
      if (dropTarget instanceof ScyWindow) {
         def window = dropTarget as ScyWindow;
         return "elo: {window.eloUri}"
      } else {
         return "dropTaget class: {dropTarget.getClass()}"
      }
   }

}
