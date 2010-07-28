/*
 * ScyWindowMixin.fx
 *
 * Created on 2-sep-2009, 14:49:19
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;
import javafx.scene.effect.Effect;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;

import javafx.scene.CustomNode;

import eu.scy.client.desktop.scydesktop.scywindows.window.ScyToolsList;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowChangesListener;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ScyColors;

/**
 * @author sikkenj
 */

public abstract class ScyWindow extends CustomNode {
   public var title = "???";
	public var eloType = "?123";
   public var eloUri:URI;
   public var eloIcon:EloIcon;
//   public var iconCharacter = "?";
//	public var color = Color.GREEN;
//	public var drawerColor = Color.LIGHTGREEN;
//	public var backgroundColor = color.WHITE;
   public-init var windowColorScheme:WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   public var activated = false; // TODO, make only changeable from (sub) package
	public var width: Number = 150;
	public var height: Number = 100;

   // set the desired width/height are applied after the scyContent of the window has changed
   // both the desired width and height must be set
   // the desiredWidth/desiredHeight specifies the window sizes and takes presence over the desired content size
   public var desiredWidth: Number = -1;
	public var desiredHeight: Number = -1;
   public var desiredContentWidth: Number = -1;
	public var desiredContentHeight: Number = -1;

   public var minimumWidth: Number = -1;
   public var minimumHeight: Number = -1;

   public var maximumWidth: Number = Number.MAX_VALUE;
   public var maximumHeight: Number = Number.MAX_VALUE;

   public-read protected var originalWidth: Number = 70;
   public-read protected var originalHeight: Number = 50;

   public var closedBoundsWidth:Number;
   public var closedBoundsHeight:Number;

   public var widthHeightProportion: Number = -1.0;

	public var scyContent: Node;
//	public var scyTool: ScyTool;

   public var topDrawerTool:Node;
   public var rightDrawerTool:Node;
   public var bottomDrawerTool:Node;
   public var leftDrawerTool:Node;

   public var scyToolsList: ScyToolsList;

   public var scyWindowAttributes: ScyWindowAttribute[];

   public var allowRotate = true;
   public var allowResize = true;
   public var allowDragging = true;
   public var allowClose = true;
   public var allowMinimize = true;
//   public var closeIsHide = false;
   public var windowManager: WindowManager;
	public var windowEffect: Effect;
	public var minimizeAction: function(ScyWindow):Void;
	public var setScyContent: function(ScyWindow):Void;
	public var aboutToCloseAction: function(ScyWindow):Boolean;
	public var closedAction: function(ScyWindow):Void;

   public var windowControl: ScyWindowControl;
   public var tooltipManager:TooltipManager;
   public var dragAndDropManager:DragAndDropManager;
   public var mucId: String;

	// status variables
	public-read protected var isMinimized = false;
	public-read protected var isClosed = true;

   public function open():Void{
//      var openWidth = minimumWidth;
//      var openHeight = minimumHeight;
//      if (scyContent instanceof Resizable){
//         var resizableContent = scyContent as Resizable;
//         openWidth = resizableContent.getPrefWidth(openWidth);
//         openHeight = resizableContent.getPrefHeight(openHeight);
//      }
      openWindow(minimumWidth,minimumHeight)
   }

   public abstract function close():Void;

   public abstract function openWindow(width:Number, height:Number):Void;
   
   public abstract function setMinimize(state: Boolean):Void;

   public abstract function addChangesListener(wcl:WindowChangesListener):Void;
   public abstract function removeChangesListener(wcl:WindowChangesListener):Void;


   public abstract function canAcceptDrop(object:Object):Boolean;
   public abstract function acceptDrop(object:Object):Void;

   // this is probably not the best way to handle it
   public abstract function openDrawer(which:String):Void;

}
