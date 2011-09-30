/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.util.Sequences;
import javafx.lang.FX;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.StandardWindowPositionsState;
import javafx.geometry.Point2D;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.desktoputils.XFX;

/**
 * @author giemza
 */

public class FunctionalRoleWindowPositioner extends WindowPositioner {

    def logger = Logger.getLogger(this.getClass());

    public var scyDesktop: ScyDesktop;

    public-init var debug:Boolean;

    public-init var ignoreResources:Boolean;

    var activeWindow = bind scyDesktop.windows.activeWindow on replace {
        XFX.runActionInBackgroundAndCallBack(function() {
            if(activeWindow!=null and windowInArea(activeWindow, mainWindows)) {
                delete activeWindow from mainWindows;
                insert activeWindow into mainWindows;
            }
        }, null);
    }

    var desktopWidth = bind scyDesktop.scene.width on replace {
        updateAreas();
        repositionWindowsOnResize();
    };
    var desktopHeight = bind scyDesktop.scene.height on replace {
        updateAreas();
        repositionWindowsOnResize();
    };

    var lockUpdate:Boolean = false;

    public var incomingWindows: ScyWindow[];

    public var centerWindows: ScyWindow[];

    public var outgoingWindows: ScyWindow[];

    public var otherWindows: ScyWindow[];

    public var mainWindows: ScyWindow[];

    // for debugging
    def rectangleColor = Color.color(0.0, 0.0, 0.0, 0.1);

    def incomingArea = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.7, 0.4, 0.3, 0.1);
            stroke: rectangleColor
    }

    def centerArea = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.4, 0.4, 0.1, 0.1);
            stroke: rectangleColor
    }

    def outgoingArea = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.2, 0.4, 0.7, 0.1);
            stroke: rectangleColor
    }

    def otherArea: Rectangle = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.2, 0.4, 0.7, 0.1);
            stroke: rectangleColor
    }

    def mainArea = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.1, 0.1, 0.1, 0.1);
            stroke: rectangleColor
    }

    def debugAreas = Group {
            content: [incomingArea, centerArea, outgoingArea, mainArea, otherArea]
    };

    var debugAreasAdded = false;

    public override function addGlobalLearningObjectWindow(window:ScyWindow):Boolean {
        logger.info("addGlobalLearningObjectWindow with title {window.title}");
        if (ignoreResources) {
            return false;
        }

        // lower area
        if (not windowAlreadyAdded(window)) {
            insert window into otherWindows;
            return true;
        }
        return false;
    }

    public override function clearWindows():Void {
        logger.info("clearWindows");
        resetLayout();
        delete incomingWindows;
        delete centerWindows;
        delete outgoingWindows;
        delete otherWindows;
        delete mainWindows;
    }

    public override function makeMainWindow(window:ScyWindow):Void {
        logger.info("makeMainWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            logger.error("Cannot make a window {window.title} the main window as it has not yet been added.");
        } else {
            delete window from mainWindows;
            // insert it again to be the last
            insert window into mainWindows;
            window.isCentered = true;
            (window as StandardScyWindow).reorganizeOtherMainWindows = function() {
                // defere to next UI cycle
                FX.deferAction(positionMainWindows);
                (window as StandardScyWindow).reorganizeOtherMainWindows = null;
            }
        }
    }

    public override function setAnchorWindow(window:ScyWindow):Boolean {
        logger.info("setAnchorWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into outgoingWindows;
            return true;
        }
        return false;
    }

    public override function addIntermediateWindow(window:ScyWindow):Boolean {
        logger.info("addIntermediateWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into centerWindows;
            return true;
        }
        return false;
    }

    public override function addNextAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addNextAnchorWindow with title {window.title}");
        //insert window into outgoingWindows;
        return false;
    }

    public override function addPreviousAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addPreviousAnchorWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into incomingWindows;
            return true;
        }
        return false;
    }

    public override function addInputAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addInputAnchorWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into incomingWindows;
            return true;
        }
        return false;
    }

    public override function addLearningObjectWindow(window:ScyWindow):Boolean {
        logger.info("addLearningObjectWindow with title {window.title}");
        if (ignoreResources) {
            return false;
        }
        // lower area
        if (not windowAlreadyAdded(window)) {
            insert window into otherWindows;
            return true;
        }
        return false;
    }


    public override function addOtherWindow(window:ScyWindow):Boolean {
        logger.info("addOtherWindow with title {window.title}");
        // animate opening into lower left center ELO
        if (not windowAlreadyAdded(window)) {
            insert window into otherWindows;
            makeMainWindow(window);
            positionNewOtherWindow(window);
            return true;
        }
        return false;
    }


    public override function removeOtherWindow(window:ScyWindow):Void {
        logger.info("removeOtherWindow with title {window.title}");
        delete window from otherWindows;
    }

    public override function placeOtherWindow(window:ScyWindow):Boolean {
        logger.info("placeOtherWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into centerWindows;
            updateAreas();
            positionWindowsInArea(centerWindows, centerArea, 3);
            FX.deferAction(function() {
                makeMainWindow(window);
                positionWindows();
            });
            return true;
        }
        return false;
    }

    public override function positionWindows(windowPositionsState:WindowPositionsState):Void {
        logger.info("positionWindowsWithState");
        def windowStates: StandardWindowPositionsState = windowPositionsState as StandardWindowPositionsState;
        for (window in incomingWindows) {
            windowStates.applyStateForWindow(window);
        }
        for (window in centerWindows) {
            windowStates.applyStateForWindow(window);
        }
        for (window in outgoingWindows) {
            windowStates.applyStateForWindow(window);
        }
        for (window in otherWindows) {
            windowStates.applyStateForWindow(window);
        }
        positionWindows();
        for (window in incomingWindows) {
            openWindowAndBringToFront(window);
        }
        for (window in centerWindows) {
            openWindowAndBringToFront(window);
        }
        for (window in outgoingWindows) {
            openWindowAndBringToFront(window);
        }
        for (window in otherWindows) {
            openWindowAndBringToFront(window);
        }
    }

    function openWindowAndBringToFront(window : ScyWindow) : Void {
        if (not window.isClosed) {
            window.openWindow(window.layoutX, window.layoutY, window.width, window.height, window.rotate);
        }
        window.toFront();
    }


    function windowAlreadyAdded(window:ScyWindow):Boolean {
        // checks if window is already in any of the window lists
        if(windowInArea(window, incomingWindows)) {
            return true;
        }
        if(windowInArea(window, centerWindows)) {
            return true;
        }
        if(windowInArea(window, outgoingWindows)) {
            return true;
        }
        if(windowInArea(window, otherWindows)) {
            return true;
        }
        return false;
    }

    function windowInArea(window:ScyWindow, area:ScyWindow[]): Boolean {
        return Sequences.indexOf(area, window) >= 0;
    }

    function positionNewOtherWindow(window:ScyWindow) {
        updateAreas();
        if (window.layoutX == 0 and window.layoutY == 0 and window.relativeLayoutCenterX == 0 and window.relativeLayoutCenterY == 0) {
            positionWindowsInArea(otherWindows, otherArea, sizeof otherWindows); // layout at bottom of window
        }
    }


    public override function positionWindows():Void {
        logger.info("positionWindows");

        lockUpdate = true;

        if (sizeof incomingWindows == 0 and sizeof centerWindows == 0 and sizeof outgoingWindows == 0) {
            return;
        }

        updateAreas();

        positionWindowsInArea(incomingWindows, incomingArea, 1);
        positionWindowsInArea(centerWindows, centerArea, 2);
        positionWindowsInArea(outgoingWindows, outgoingArea, 1);
        positionWindowsInArea(otherWindows, otherArea, sizeof otherWindows);
        positionMainWindows();
        
        // only for debug purpose
        if (debug and not debugAreasAdded) {
            insert debugAreas into scyDesktop.highDebugGroup.content;
            debugAreasAdded = true;
        }

        lockUpdate = false;
    }

    public override function makeWindowFullScreen(window:ScyWindow):Void {
        var newX = offset;
        var newY = offset;
        var newWidth = desktopWidth - 2 * offset;
        var newHeight = desktopHeight - 2 * offset;

        window.openWindow(newX, newY, newWidth, newHeight, 0);
    }

    function positionWindowsInArea(windowList:ScyWindow[], area:Rectangle, maxColumns:Integer) {
        var topOffset = 25.0;
        var padding = 140.0;
        // positioning incoming
        var numberOfWindows = sizeof windowList;
        var columns = maxColumns;
        if (numberOfWindows < columns) {
            columns = numberOfWindows;
        }
        var row = 0;
        var column = 1;
        def shift = area.width / (columns + 1);
        for (window in windowList) {
            if (not window.isCentered and not window.isManuallyRepositioned) { // window.layoutX == 0 and window.layoutY == 0 and window.relativeLayoutCenterX == 0 and window.relativeLayoutCenterY == 0) {
                
                window.layoutX = area.layoutX + (column * shift) + ((column-1) * 25) - (window.width / 2);
                window.layoutY = area.layoutY + topOffset + (row * padding);
                if (window.closedPosition == null) {
                    window.closedPosition = Point2D {
                        x: window.layoutX;
                        y: window.layoutY;
                    }
                }

                window.relativeLayoutCenterX = (window.layoutX + window.width / 2) / desktopWidth;
                window.relativeLayoutCenterY = (window.layoutY + window.height / 2) / desktopHeight;

                padding = Math.max(padding, window.height + 15);

                if (column mod columns != 0) {
                    column++;
                } else if (column mod columns == 0) {
                    row++;
                    column = 1;
                }
            } else {
                if (column mod columns != 0) {
                    column++;
                } else if (column mod columns == 0) {
                    row++;
                    column = 1;
                }
            }
        }
    }

    function positionMainWindows():Void {
        if (sizeof mainWindows == 0) {
            return;
        }

//      var newX = centerArea.layoutX - 5 * offset;
//      Moved the window a little bit more to the left of the center
        var newX = centerArea.layoutX - 2 * offset;
        var newY = centerArea.layoutY + (0.12 * centerArea.height); // value ist just estimated
        var newWidth = centerArea.width + 4 * offset;
        var newHeight = 0.75 * centerArea.height;

        mainArea.layoutX = newX;
        mainArea.layoutY = newY;
        mainArea.width = newWidth;
        mainArea.height = newHeight;

        // remove non centered windows
        for (nextWindow in mainWindows) {
            if (not nextWindow.isCentered) {
                delete nextWindow from mainWindows;
            }
        }

        def topWindow: StandardScyWindow = mainWindows[sizeof mainWindows - 1] as StandardScyWindow;
        // first we register the function to rotate all the others...
        topWindow.finishedOpeningWindow = function() {
            delete topWindow from mainWindows;
            var size = sizeof mainWindows;
            var windowCounter = 0;
            for (nextWindow in mainWindows) {
                def angle = (size - windowCounter++) * 4;
                (nextWindow as StandardScyWindow).finishedOpeningWindow = null;
                nextWindow.openWindow(newX, newY, newWidth, newHeight, angle);
            }
            insert topWindow into mainWindows;
        }
        // then we open the last (latest) mainWindow
        topWindow.openWindow(newX, newY, newWidth, newHeight, 0);
    }

    def incomingAreaRatio = 0.2;
    def centerAreaRatio = 0.6;
    def outgoingAreaRatio = 0.2;
    def offset = 10;
    def margin = 50;

    function repositionWindowsOnResize():Void {
        FX.deferAction(function():Void {
            lockUpdate = true;

            repositionWindowsInArea(incomingWindows, false);
            repositionWindowsInArea(centerWindows, false);
            repositionWindowsInArea(outgoingWindows, false);
            repositionWindowsInArea(otherWindows, true);

            lockUpdate = false;
        });
    }

    function repositionWindowsInArea(windowList:ScyWindow[], includeYAxis: Boolean):Void {
        for(window in windowList) {
            if (not window.isClosed) {
                window.layoutY = (window.relativeLayoutCenterY * desktopHeight) - window.height / 2;
                window.width = window.relativeWidth * desktopWidth;
                window.height = window.relativeHeight * desktopHeight;
            } else if (includeYAxis) {
                window.layoutY = (window.relativeLayoutCenterY * desktopHeight) - window.height / 2;
            }
            window.layoutX = (window.relativeLayoutCenterX * desktopWidth) - window.width / 2;
        }
    }

    function updateAreas():Void {
        // good link for bounds of nodes http://weblogs.java.net/blog/2009/07/09/javafx12-understanding-bounds
        //def topLeftX = Math.max(scyDesktop.topLeftCorner.boundsInParent.maxX, scyDesktop.bottomLeftCorner.boundsInParent.maxX);
        //def topRightX = Math.min(scyDesktop.topRightCorner.boundsInParent.minX, scyDesktop.bottomRightCorner.boundsInParent.minX);
        //def layoutWidth = topRightX - topLeftX;

        incomingArea.layoutX = offset + margin;
        incomingArea.layoutY = offset;
        incomingArea.width = (incomingAreaRatio * desktopWidth) - (2 * offset);
        incomingArea.height = desktopHeight - (2 * offset);

        centerArea.layoutX = incomingArea.layoutX + incomingArea.width + (2 * offset);
        centerArea.layoutY = offset;
        centerArea.width = (centerAreaRatio * desktopWidth) - (2 * offset) - (2 * margin);
        centerArea.height = desktopHeight - (2 * offset);

        outgoingArea.layoutX = centerArea.layoutX + centerArea.width + (2 * offset);
        outgoingArea.layoutY = offset;
        outgoingArea.width = (outgoingAreaRatio * desktopWidth) - (2 * offset);
        outgoingArea.height = desktopHeight - (2 * offset);

        otherArea.layoutX = offset;
        otherArea.layoutY = centerArea.layoutY + 0.70 * centerArea.height;
        otherArea.width = desktopWidth - (2 * offset);
        otherArea.height = 0.3 * centerArea.height;
    }

    function resetLayout() {
        resetWindowLayoutInArea(incomingWindows);
        resetWindowLayoutInArea(centerWindows);
        resetWindowLayoutInArea(outgoingWindows);
        resetWindowLayoutInArea(otherWindows);
    }

    function resetWindowLayoutInArea(windowList:ScyWindow[]) {
        for (window in windowList) {
            // close might take too long or too much memory
            // maybe set heigth and width manually?
            // UPDATE 30.09.2011
            // yes it took too long and created some weired layouting shit
            // which led to the broken ELO positioning
            window.close(false);
            logger.debug("resetting window {window.title} with URI {window.eloUri}");
        }
    }

    public override function getWindowPositionsState():WindowPositionsState {
        logger.info("getWindowPositionsState");
        def windowStates: StandardWindowPositionsState = StandardWindowPositionsState {};
        for (window in incomingWindows) {
            windowStates.persistWindowState(window);
        }
        for (window in centerWindows) {
            windowStates.persistWindowState(window);
        }
        for (window in outgoingWindows) {
            windowStates.persistWindowState(window);
        }
        for (window in otherWindows) {
            windowStates.persistWindowState(window);
        }
        return windowStates;
    }
}
