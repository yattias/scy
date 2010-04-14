/*
 * WindowElement.fx
 *
 * Created on 3-sep-2009, 17:38:47
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;

import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */

// place your code here
public abstract class WindowElement extends CustomNode {

   public var windowColorScheme:WindowColorScheme;
//   public var color = Color.RED;
//   public var subColor = Color.WHITE;
   public var strokeWidth = 4.0;

   public var activate: function():Void;

}

