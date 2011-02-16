/*
 * NewJavaFXEmpty.fx
 *
 * Created on 10-dec-2009, 10:14:30
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.CustomNode;
import javafx.geometry.Bounds;
import javafx.util.Math;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikken
 */
// place your code here

public def defaultEloIconSize = 40.0;

public abstract class EloIcon extends CustomNode {

   public var selected: Boolean = false;
   public var size = defaultEloIconSize;
   public var windowColorScheme: WindowColorScheme;
   public def borderSize = 2.0;
   public def cornerRadius = 7;

   public override function clone(): EloIcon {
      null
   }

   protected function calculateScale(bounds: Bounds): Number {
      return calculateScale(bounds.width, bounds.height);
   }

   protected function calculateScale(width: Number, height: Number): Number {
      def scaleX = (size-borderSize) / width;
      def scaleY = (size-borderSize) / height;
      return Math.min(scaleX, scaleY);
   }

   protected function scaleNode(node: Node){
      def scale = calculateScale(node.layoutBounds);
      //      println("fxdNode.layoutBounds: {fxdNode.layoutBounds}, scale: {scale}");
      //      println("fxdNode.boundsInParent: {fxdNode.boundsInParent}");
      node.scaleX = scale;
      node.scaleY = scale;
      node.layoutX = -node.layoutBounds.minX + (scale - 1) * node.layoutBounds.width / 2 + borderSize/2;
      node.layoutY = -node.layoutBounds.minY + (scale - 1) * node.layoutBounds.height / 2 + borderSize/2;
      //      println("fxdNode.boundsInParent: {fxdNode.boundsInParent}");
      //      println("");
   }


}
