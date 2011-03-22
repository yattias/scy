/*
 * ScyWindowAttribute.fx
 *
 * Created on 26-mrt-2009, 15:03:09
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import java.lang.Comparable;
import java.lang.Object;
import javafx.scene.CustomNode;

/**
 * @author sikkenj
 */
public abstract class ScyWindowAttribute extends CustomNode, Comparable {

   public var scyWindow: ScyWindow;
   public var priority: Integer;

   public override function compareTo(object: Object): Integer {
      var result: Integer = 0;
      if (object instanceof ScyWindowAttribute) {
         var swa = object as ScyWindowAttribute;
         result = priority - swa.priority;
      //			 println("result:{result} ({priority}-{swa.priority})");

      }
      return result;
   }

   abstract public override function clone():ScyWindowAttribute;

}
