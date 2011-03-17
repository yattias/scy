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
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.mission.EloToolConfig;
import javafx.geometry.Point2D;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;

/**
 * @author sikkenj
 */
public abstract class ScyWindow extends CustomNode, DropTarget {

    public var title = "???";
    public var eloType = "?123";
    public var relativeLayoutCenterX: Number;
    public var relativeLayoutCenterY: Number;
    public var relativeWidth: Number;
    public var relativeHeight: Number;
    public var eloUri: URI;
    public var scyElo: ScyElo;
    public var eloIcon: EloIcon;
    public var eloToolConfig: EloToolConfig;
    public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
    public var activated = false; // TODO, make only changeable from (sub) package
    public var width: Number = 150;
    public var height: Number = 100;
    public var closedPosition: Point2D;
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
    public var closedBoundsWidth: Number;
    public var closedBoundsHeight: Number;
    public var widthHeightProportion: Number = -1.0;
    public var scyContent: Node;
    public var topDrawerTool: Node;
    public var rightDrawerTool: Node;
    public var bottomDrawerTool: Node;
    public var leftDrawerTools: Node[];
    public var scyToolsList: ScyToolsList;
    public var scyWindowAttributes: ScyWindowAttribute[];
    public var allowRotate = true;
    public var allowResize = true;
    public var allowDragging = true;
    public var allowClose = true;
    public var allowMinimize = true;
    public var allowMaximize = true;
    public var allowCenter = true;
    public var windowManager: WindowManager;
    public var windowEffect: Effect;
    public var minimizeAction: function(ScyWindow): Void;
    public var setScyContent: function(ScyWindow): Void;
    public var aboutToCloseAction: function(ScyWindow): Boolean;
    public var closedAction: function(ScyWindow): Void;
    public var windowControl: ScyWindowControl;
    public var tooltipManager: TooltipManager;
    public var dragAndDropManager: DragAndDropManager;
    public var mucId: String;
    public var tbi: ToolBrokerAPI;
    public var ownershipManager: OwnershipManager;
    // status variables
    public-read protected var isMinimized = false;
    public-read protected var isClosed = true;
    public var isCentered = false;
    public-read protected var isMaximized = false;
    public-read protected var isManuallyRepositioned = false;
    public var isCollaborative = false;
    public var isQuiting = false;
    public var isScyContentSet: Boolean = false;
    
    public function open(): Void {
        openWindow(minimumWidth, minimumHeight)
    }

    public abstract function copyWindowColorSchemeColors(newColors: WindowColorScheme): Void;

    public abstract function close(): Void;

    public abstract function openBoundWindow(openWidth: Number, openHeight: Number): Void;

    public abstract function openWindow(width: Number, height: Number): Void;

    public abstract function openWindow(layoutX: Number, layoutY: Number, width: Number, height: Number): Void;

    public abstract function openWindow(layoutX: Number, layoutY: Number, width: Number, height: Number, rotation: Number): Void;

    public abstract function openWindow(posX: Number, posY: Number, openWidth: Number, openHeight: Number, rotation: Number, hideDrawersAfterOpenning: Boolean): Void;

    public abstract function addChangesListener(wcl: WindowChangesListener): Void;

    public abstract function removeChangesListener(wcl: WindowChangesListener): Void;

    // this is probably not the best way to handle it
    public abstract function openDrawer(which: String): Void;

}
