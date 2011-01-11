/*
 * NewJavaFXEmpty.fx
 *
 * Created on 10-dec-2009, 10:14:30
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.CustomNode;
import javafx.geometry.Bounds;
import javafx.util.Math;

/**
 * @author sikken
 */
// place your code here
public abstract class EloIcon extends CustomNode {

   public var selected: Boolean = false;
   public var size = 40.0;

   public override function clone(): EloIcon {
      null
   }

   protected function calculateScale(bounds: Bounds): Number {
      return calculateScale(bounds.width, bounds.height);
   }

   protected function calculateScale(width: Number, height: Number): Number {
      def scaleX = size / width;
      def scaleY = size / height;
      return Math.min(scaleX, scaleY);
   }

}
