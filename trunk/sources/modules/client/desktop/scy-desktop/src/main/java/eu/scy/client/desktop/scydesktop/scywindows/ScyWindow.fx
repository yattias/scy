/*
 * ScyWindowMixin.fx
 *
 * Created on 2-sep-2009, 14:49:19
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;

import javafx.scene.CustomNode;

/**
 * @author sikkenj
 */

public abstract class ScyWindow extends CustomNode {
   public var title = "???";
	public var eloType = "?123";
   public var eloUri:URI;
   public var iconCharacter = "?";
	public var color = Color.GREEN;
	public var backgroundColor = color.WHITE;

	public var width: Number = 100;
	public var height: Number = 100;

   public var minimumHeight: Number = 50;
   public var minimumWidth: Number = 70;

   public var maximumHeight: Number = Number.MAX_VALUE;
   public var maximumWidth: Number = Number.MAX_VALUE;

   public-read protected var originalHeight: Number = 50;
   public-read protected var originalWidth: Number = 70;

   public var widthHeightProportion: Number = -1.0;

	public var scyContent: Node;
	public var scyTool: ScyTool;
   public var scyWindowAttributes: ScyWindowAttribute[];
   public var allowRotate = true;
   public var allowResize = true;
   public var allowDragging = true;
   public var allowClose = true;
   public var allowMinimize = true;
   public var closeIsHide = false;
   public var scyDesktop: WindowManager;
	public var windowEffect: Effect;
	public var minimizeAction: function(ScyWindow):Void;
	public var setScyContent: function(ScyWindow):Void;
	public var aboutToCloseAction: function(ScyWindow):Boolean;
	public var closedAction: function(ScyWindow):Void;

	// status variables
	public-read protected var isMinimized = false;
	public-read protected var isClosed = true;

   public abstract function open():Void;
   public abstract function close():Void;

   public abstract function openWindow(x:Number, y:Number):Void;

}
