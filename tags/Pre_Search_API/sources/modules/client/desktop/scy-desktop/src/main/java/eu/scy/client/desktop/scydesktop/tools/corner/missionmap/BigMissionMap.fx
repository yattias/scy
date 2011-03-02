/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.layout.Resizable;
import javafx.util.Math;
import org.apache.log4j.Logger;
import javafx.scene.Node;
import javafx.scene.layout.Stack;
import javafx.scene.layout.Container;
import javafx.geometry.Insets;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SemiPermanentTooltipManager;
import javafx.scene.input.MouseEvent;

/**
 * @author SikkenJ
 */
public class BigMissionMap extends MissionMap, Resizable {

   def logger = Logger.getLogger(this.getClass());
   public override var width on replace { adjustSize(); }
   public override var height on replace { adjustSize(); }
   public var anchorClicked: function(): Void;
   var scale = 1.0;
   def relativeBorder = 0.2;
   var missionMapNode: Node;
   var myStack: Stack;
   def activeLas = bind missionModel.activeLas on replace { };

   init {
      tooltipManager = SemiPermanentTooltipManager {
         }
   }

   public override function create(): Node {
      missionMapNode = super.create();
      def missionMapBounds = missionMapNode.layoutBounds;
      myStack = Stack {
            padding: Insets {
               top: relativeBorder * missionMapBounds.height
               right: relativeBorder * missionMapBounds.width
               bottom: relativeBorder * missionMapBounds.height
               left: relativeBorder * missionMapBounds.width
            }
            content: [
               missionMapNode
            ]
            onMouseClicked: function(e: MouseEvent): Void {
               tooltipManager.removeTooltip();
            }
         }
   }

   package function adjustSize() {
      //      Container.resizeNode(myStack, missionMapNode.layoutBounds.width, missionMapNode.layoutBounds.height);
      def scaleX = width / ((1 + 2 * relativeBorder) * missionMapNode.layoutBounds.width);
      def scaleY = height / ((1 + 2 * relativeBorder) * missionMapNode.layoutBounds.height);
      scale = Math.min(scaleX, scaleY);
      //      scale = 1.0;
      logger.info("layoutBounds: {missionMapNode.layoutBounds} + ({width},{height}) -> scale: {scale}");
      //      logger.info("boundsInLocal: {missionMapNode.boundsInLocal}");
      this.scaleX = scale;
      this.scaleY = scale;
      missionMapNode.translateX = -missionMapNode.layoutBounds.minX + (scale - 1) * missionMapNode.layoutBounds.width / 2;
      missionMapNode.translateY = -missionMapNode.layoutBounds.minY + (scale - 1) * missionMapNode.layoutBounds.height / 2;
      //      logger.info("translate: {missionMapNode.translateX},{missionMapNode.translateY}");
      var stackX = (width - scale * missionMapNode.layoutBounds.width) / 2;
      var stackY = (height - scale * missionMapNode.layoutBounds.height) / 2;
      //      logger.info("stack pos: {stackX},{stackY}");
      Container.positionNode(myStack, stackX, stackY);
   }

   override function getPrefWidth(width: Number): Number {
      missionMapNode.layoutBounds.width * scale
   }

   override function getPrefHeight(height: Number): Number {
      missionMapNode.layoutBounds.height * scale
   }

   public override function anchorSelected(anchorDisplay: AnchorDisplay, anchor: MissionAnchorFX): Void {
      anchorClicked();
      super.anchorSelected(anchorDisplay, anchor);
   }

}