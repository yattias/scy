/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.layout.Resizable;
import javafx.util.Math;
import org.apache.log4j.Logger;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SemiPermanentTooltipManager;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.desktoputils.XFX;
import javafx.fxd.FXDNode;
import javafx.scene.Group;
import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;

/**
 * @author SikkenJ
 */
public class BigMissionMap extends MissionMap, Resizable {

   def logger = Logger.getLogger(this.getClass());
   def missionBackgroundImageName = "missionBackground";
   public var bubbleManager: BubbleManager;
   public override var width on replace { adjustSize(); }
   public override var height on replace { adjustSize(); }
   public var anchorClicked: function(): Void;
   var scale = 1.0;
   def relativeMapBorder = 0.2;
   def relativeBackgroundImageBorder = 0.075;
   def uriLocalizer = new UriLocalizer();
   var missionMapNode: Node;
   var missionBackgroundImageNode: Node;
   var missionMapGroup: Group;
   var backgroundGroup: Group;
   def activeLas = bind missionModel.activeLas;

   init {
      tooltipManager = SemiPermanentTooltipManager {
         }
   }

   public override function create(): Node {
      missionMapNode = super.create();
      bubbleManager.createBubble(anchorDisplays[0], BubbleLayer.MISSION_MAP, BubbleKey.MISSION_MAP_ANCHOR_ELO_CLICK, anchorDisplays[0].las.mainAnchor.windowColorScheme);
      bubbleManager.createBubble(anchorDisplays[1], BubbleLayer.MISSION_MAP, BubbleKey.MISSION_MAP_ANCHOR_ELO_HOVER, anchorDisplays[1].las.mainAnchor.windowColorScheme);
      missionBackgroundImageNode = getBackgroundImageNode();
      placeNodeOn00(missionMapNode);
      if (missionBackgroundImageNode != null) {
         placeNodeOn00(missionBackgroundImageNode);
      }

      def missionMapBounds = missionMapNode.layoutBounds;
      Group {
         content: [
            backgroundGroup = Group {
                  content: [
                     missionBackgroundImageNode,
                  ]
               }
            missionMapGroup = Group {
                  content: [
                     missionMapNode
                  ]
               }
         ]
         onMouseClicked: function(e: MouseEvent): Void {
            tooltipManager.removeTooltip();
         }
      }
   }

   function getBackgroundImageNode(): Node {
      if (missionModel.missionMapBackgroundImageUri != null and missionModel.missionMapBackgroundImageUri.toString().length() > 0) {
         def missionMapBackgroundImageNode = FXDNode {
               url: uriLocalizer.localizeUriWithChecking(missionModel.missionMapBackgroundImageUri.toString())
               backgroundLoading: false
            }
         def node = missionMapBackgroundImageNode.getNode(missionBackgroundImageName);
         if (node==null){
            logger.warn("Could not find node named {missionBackgroundImageName} in {missionModel.missionMapBackgroundImageUri}");
            return null;
         }
         node.visible = true;
         return node;
      }
      return null;
   }

   function placeNodeOn00(node: Node) {
//      println(" before : layoutBounds :{node.layoutBounds}");
//      println(" before : boundsInLocal:{node.boundsInLocal}");
      node.translateX = -node.layoutBounds.minX;
      node.translateY = -node.layoutBounds.minY;
//      println(" after  : layoutBounds :{node.layoutBounds}");
//      println(" after  : boundsInLocal:{node.boundsInLocal}");
   }

   package function adjustSize() {
      if (width == 0 or height == 0) {
         return;
      }
      scale = adjustSize(missionMapNode, relativeMapBorder);
      centerGroup(missionMapGroup);
      if (missionBackgroundImageNode != null) {
         adjustSize(missionBackgroundImageNode, relativeBackgroundImageBorder);
         centerGroup(backgroundGroup);
      }
   }

   package function adjustSize(node: Node, relativeBorder: Number): Number {
      def desiredWidth = (1 - 2 * relativeBorder) * width;
      def desiredHeight = (1 - 2 * relativeBorder) * height;

      def scaleX = desiredWidth / node.layoutBounds.width;
      def scaleY = desiredHeight / node.layoutBounds.height;
      def scale = Math.min(scaleX, scaleY);
      node.scaleX = scale;
      node.scaleY = scale;
      node.layoutX -= node.boundsInParent.minX;
      node.layoutY -= node.boundsInParent.minY;
   }

   function centerGroup(group: Group) {
       group.layoutX = width/2-group.layoutBounds.width/2;
       group.layoutY = height/2-group.layoutBounds.height/2;
   }


   override function getPrefWidth(width: Number): Number {
      missionMapNode.layoutBounds.width * scale
   }

   override function getPrefHeight(height: Number): Number {
      missionMapNode.layoutBounds.height * scale
   }

   public override function anchorSelected(anchorDisplay: AnchorDisplay, anchor: MissionAnchorFX): Void {
      ProgressOverlay.startShowWorking();
      XFX.runActionInBackgroundAndCallBack(function(): Object {
         super.anchorSelected(anchorDisplay, anchor);
         return null;
      }, function(obj) {
         FX.deferAction(function() {
            anchorClicked();
            ProgressOverlay.stopShowWorking();
         });
      });
   }


}
