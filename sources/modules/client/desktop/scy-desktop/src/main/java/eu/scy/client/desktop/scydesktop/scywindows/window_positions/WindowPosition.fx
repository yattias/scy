/*
 * WindowPosition.fx
 *
 * Created on 30-mrt-2009, 11:24:26
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import javafx.geometry.Rectangle2D;

/**
 * @author sikkenj
 */

public class WindowPosition {
   public var window: ScyWindow;
   public var preferredDirection: Number;
   public var currentDirection: Number;
   public var useDirection: Number;
   public var x: Number;
   public var y: Number;
   public var width: Number;
   public var height: Number;
   public var minimized: Boolean;
   public var rectangle: Rectangle2D;
   public var intersection: Number;
   public var correctionCount: Integer;

   public function copyFrom(windowPosition:WindowPosition){
      window = windowPosition.window;
      preferredDirection = windowPosition.preferredDirection;
      currentDirection = windowPosition.currentDirection;
      useDirection = windowPosition.useDirection;
      x = windowPosition.x;
      y = windowPosition.y;
      width = windowPosition.width;
      height = windowPosition.height;
      minimized = windowPosition.minimized;
      rectangle = windowPosition.rectangle;
      intersection = windowPosition.intersection;
      correctionCount = windowPosition.correctionCount;
   }
}

public function clone(windowPosition:WindowPosition){
   var newWindowPosition = WindowPosition{
   };
   newWindowPosition.copyFrom(windowPosition);
   return newWindowPosition;
}
