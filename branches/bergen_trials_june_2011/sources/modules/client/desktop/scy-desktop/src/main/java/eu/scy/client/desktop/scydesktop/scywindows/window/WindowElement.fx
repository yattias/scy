/*
 * WindowElement.fx
 *
 * Created on 3-sep-2009, 17:38:47
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;

import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */

// place your code here
public abstract class WindowElement extends CustomNode {

   public var windowColorScheme:WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
//   public var color = Color.RED;
//   public var subColor = Color.WHITE;
   public var strokeWidth = 4.0;

   public var activate: function():Void;

}

