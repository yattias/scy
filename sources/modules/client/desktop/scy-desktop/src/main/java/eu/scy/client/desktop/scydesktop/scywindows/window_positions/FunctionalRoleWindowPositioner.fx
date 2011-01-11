/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.util.Sequences;
import javafx.lang.FX;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import javafx.util.Math;
import javafx.scene.CacheHint;

/**
 * @author giemza
 */

public class FunctionalRoleWindowPositioner extends WindowPositioner {

    def logger = Logger.getLogger(this.getClass());

    public var scyDesktop: ScyDesktop;

    public-init var debug:Boolean;

    public-init var ignoreResources:Boolean;

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

    def mainArea = Rectangle {
            x: 0;
            y: 0;
            width: 0;
            height: 0;
            fill: Color.color(0.1, 0.1, 0.1, 0.1);
            stroke: rectangleColor
    }

    def debugAreas = Group {
            content: [incomingArea, centerArea, outgoingArea, mainArea]
    };

    var debugAreasAdded = false;

    public override function addGlobalLearningObjectWindow(window:ScyWindow):Boolean {
        logger.info("addGlobalLearningObjectWindow with title {window.title}");
        if (ignoreResources) {
            return false;
        }

        // lower area
        if (not windowAlreadyAdded(window)) {
            insert window into centerWindows;
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
            window.open();
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
            insert window into centerWindows;
            return true;
        }
        return false;
    }


    public override function addOtherWindow(window:ScyWindow):Boolean {
        logger.info("addOtherWindow with title {window.title}");
        // animate opening into lower left center ELO
        if (not windowAlreadyAdded(window)) {
            insert window into centerWindows;
            return true;
        }
        return false;
    }


    public override function removeOtherWindow(window:ScyWindow):Void {
        logger.info("removeOtherWindow with title {window.title}");
        delete window from centerWindows;
    }

    public override function placeOtherWindow(window:ScyWindow):Boolean {
        logger.info("placeOtherWindow with title {window.title}");
        if (not windowAlreadyAdded(window)) {
            insert window into centerWindows;
            updateAreas();
            positionWindowsInArea(centerWindows, centerArea, 2);
            return true;
        }
        return false;
    }

    public override function positionWindows(windowPositionsState:WindowPositionsState):Void {
        logger.info("positionWindowsWithState");
        positionWindows();

    }

    function windowAlreadyAdded(window:ScyWindow):Boolean {
        // checks if window is already in any of the window lists
        if(Sequences.indexOf(incomingWindows, window) >= 0) {
            //logger.info("#!?WTF!?# Window with title {window.title} already in incomingWindows");
            return true;
        }
        if(Sequences.indexOf(centerWindows, window) >= 0) {
            //logger.info("#!?WTF!?# Window with title {window.title} already in centerWindows");
            return true;
        }
        if(Sequences.indexOf(outgoingWindows, window) >= 0) {
            //logger.info("#!?WTF!?# Window with title {window.title} already in outgoingWindows");
            return true;
        }
        return false;
    }


    public override function positionWindows():Void {
        logger.info("positionWindows");

        lockUpdate = true;

        if (sizeof incomingWindows == 0 and sizeof centerWindows == 0 and sizeof outgoingWindows == 0) {
            return;
        }

        updateAreas();

        positionWindowsInArea(incomingWindows, incomingArea, 1);
        positionWindowsInArea(centerWindows, centerArea, 3);
        positionWindowsInArea(outgoingWindows, outgoingArea, 1);
        
        // only for debug purpose
        if (debug and not debugAreasAdded) {
            insert debugAreas into scyDesktop.highDebugGroup.content;
            debugAreasAdded = true;
        }

        lockUpdate = false;
    }

    function positionWindowsInArea(windowList:ScyWindow[], area:Rectangle, maxColumns:Integer) {
        var padding = 50;
        // positioning incoming
        var numberOfWindows = sizeof windowList;
        var columns = maxColumns;
        if (numberOfWindows < columns) {
            columns = numberOfWindows;
        }
        var row = 1;
        var column = 1;
        def shift = area.width / (columns + 1);
        for (window in windowList) {
            if (Sequences.indexOf(mainWindows, window) >= 0 and not window.isClosed) {
                positionAsMainWindow(window);
            } else if (window.layoutX == 0 and window.layoutY == 0 and window.relativeLayoutCenterX == 0 and window.relativeLayoutCenterY == 0) {
                
                window.layoutX = area.layoutX + (column * shift) - (window.width / 2);
                window.layoutY = area.layoutY + (row * padding);

                window.relativeLayoutCenterX = (window.layoutX + window.width / 2) / desktopWidth;
                window.relativeLayoutCenterY = (window.layoutY + window.height / 2) / desktopHeight;

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

    function positionAsMainWindow(window:ScyWindow):Void {
        var newX = centerArea.layoutX - 5 * offset;
        var newY = centerArea.layoutY + (0.2 * centerArea.height);
        var newWidth = centerArea.width + 10 * offset;
        var newHeight = 0.7 * centerArea.height;

        def size = sizeof mainWindows;
        if (size > 1) {
            for (i in [0 .. size - 2]) {
                def mainWindow = mainWindows[i];
                var xDiff = Math.abs(mainWindow.layoutX - newX);
                var yDiff = Math.abs(mainWindow.layoutY - newY);
                if (xDiff > 4 * offset or yDiff > 4 * offset) {
                    delete mainWindow from mainWindows;
                }
            }
        }

        mainArea.layoutX = newX;
        mainArea.layoutY = newY;
        mainArea.width = newWidth;
        mainArea.height = newHeight;
        var windowCounter = 0;
        if (size > 1) {
            for (nextWindow in mainWindows) {
                def angle = ((size - 1) - windowCounter++) * 4;
                positionMainWindow(nextWindow, newX, newY, newWidth, newHeight, angle);
            }
        } else {
            positionMainWindow(mainWindows[0], newX, newY, newWidth, newHeight, 0);
        }
    }

    function positionMainWindow(mWindow:ScyWindow, newX:Number, newY:Number, newWidth:Number, newHeight:Number, angle:Number):Void {
        def window = mWindow;
        if (window.isClosed) {
            window.open();
        }
        window.cache = true;
        window.cacheHint = CacheHint.SCALE_AND_ROTATE;
        var repositionAndResize:Timeline = Timeline {
            repeatCount: 1
            autoReverse: false
            keyFrames: [
                KeyFrame {
                    canSkip: false;
                    time: 1s
                    values: [ window.width => newWidth tween Interpolator.EASEOUT,
                    window.height => newHeight tween Interpolator.EASEOUT,
                    window.layoutX => newX tween Interpolator.EASEOUT,
                    window.layoutY => newY tween Interpolator.EASEOUT,
                    window.rotate => angle tween Interpolator.EASEOUT]
                    action: function() {
                        window.relativeWidth = window.width / desktopWidth;
                        window.relativeHeight = window.height / desktopHeight;
                        window.relativeLayoutCenterX = (window.layoutX + window.width / 2) / desktopWidth;
                        window.relativeLayoutCenterY = (window.layoutY + window.height / 2) / desktopHeight;
                        window.cache = false;
                        window.cacheHint = CacheHint.SPEED;
                    }
                }
            ]
        };
        repositionAndResize.play();
    }

    
    def incomingAreaRatio = 0.2;
    def centerAreaRatio = 0.6;
    def outgoingAreaRatio = 0.2;
    def offset = 10;

    function repositionWindowsOnResize():Void {
        FX.deferAction(function():Void {
            lockUpdate = true;

            repositionWindowsInArea(incomingWindows);
            repositionWindowsInArea(centerWindows);
            repositionWindowsInArea(outgoingWindows);

            lockUpdate = false;
        });
    }

    function repositionWindowsInArea(windowList:ScyWindow[]):Void {
        for(window in windowList) {
            if (not window.isClosed) {
                window.layoutY = (window.relativeLayoutCenterY * desktopHeight) - window.height / 2;
                window.width = window.relativeWidth * desktopWidth;
                window.height = window.relativeHeight * desktopHeight;
            }
            window.layoutX = (window.relativeLayoutCenterX * desktopWidth) - window.width / 2;
        }
    }


    function updateAreas():Void {

        incomingArea.layoutX = offset;
        incomingArea.layoutY = offset;
        incomingArea.width = (incomingAreaRatio * desktopWidth) - (2 * offset);
        incomingArea.height = desktopHeight - (2 * offset);

        centerArea.layoutX = incomingArea.layoutX + incomingArea.width + (2 * offset);
        centerArea.layoutY = offset;
        centerArea.width = (centerAreaRatio * desktopWidth) - (2 * offset);
        centerArea.height = desktopHeight - (2 * offset);

        outgoingArea.layoutX = centerArea.layoutX + centerArea.width + (2 * offset);
        outgoingArea.layoutY = offset;
        outgoingArea.width = (outgoingAreaRatio * desktopWidth) - (2 * offset);
        outgoingArea.height = desktopHeight - (2 * offset);
    }

    function resetLayout() {
        resetWindowLayoutInArea(incomingWindows);
        resetWindowLayoutInArea(centerWindows);
        resetWindowLayoutInArea(outgoingWindows);
    }

    function resetWindowLayoutInArea(windowList:ScyWindow[]) {
        for (window in windowList) {
            // close might take too long or too much memory
            // maybe set heigth and width manually?
            window.close();
            window.layoutX = 0;
            window.layoutY = 0;
            window.relativeLayoutCenterX = 0;
            window.relativeLayoutCenterY = 0;
            window.rotate = 0;
            logger.info("resetting window {window.title} with URI {window.eloUri}");
        }
    }



    public override function getWindowPositionsState():WindowPositionsState {
        logger.info("getWindowPositionsState");
        return WindowPositionsState {};
    }
}
