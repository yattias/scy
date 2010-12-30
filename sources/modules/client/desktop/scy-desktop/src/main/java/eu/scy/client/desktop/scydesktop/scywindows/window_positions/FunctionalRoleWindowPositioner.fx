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

/**
 * @author giemza
 */

public class FunctionalRoleWindowPositioner extends WindowPositioner {

    def logger = Logger.getLogger(this.getClass());

    public var scyDesktop: ScyDesktop;

    var desktopWidth = bind scyDesktop.scene.width on replace {
        positionWindows();
    };
    var desktopHeight = bind scyDesktop.scene.height on replace {
        positionWindows();
    };

    public var incomingWindows: ScyWindow[];

    public var centerWindows: ScyWindow[];

    public var outgoingWindows: ScyWindow[];

    // for debugging
    def rectangleColor = Color.color(0.0, 0.0, 0.0, 0.1);

//    def incomingPositioner = AreaPositioner {
//        name : "Incoming ELOs";
//    }
//
//    def centerPositioner = AreaPositioner {
//        name : "Intermediate ELOs";
//    }
//
//    def outgoingPositioner = AreaPositioner {
//        name : "Outgoing ELOs";
//    }

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

    def debugAreas = Group {
            content: [incomingArea, centerArea, outgoingArea]
    };

    var debugAreasAdded = false;

    public override function addGlobalLearningObjectWindow(window:ScyWindow):Boolean {
        logger.info("addGlobalLearningObjectWindow with title {window.title}");
        //insert window into windows;
        return false;
    }

    public override function clearWindows():Void {
        logger.info("clearWindows");
        delete incomingWindows;
        delete centerWindows;
        delete outgoingWindows;
    }

    public override function makeMainWindow(window:ScyWindow):Void {
        logger.info("makeMainWindow with title {window.title}");
    }

    public override function setAnchorWindow(window:ScyWindow):Boolean {
        logger.info("setAnchorWindow with title {window.title}");
        insert window into outgoingWindows;
        return true;
    }

    public override function addIntermediateWindow(window:ScyWindow):Boolean {
        logger.info("addIntermediateWindow with title {window.title}");
        insert window into centerWindows;
        return true;
    }

    public override function addNextAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addNextAnchorWindow with title {window.title}");
        insert window into outgoingWindows;
        return true;
    }

    public override function addPreviousAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addPreviousAnchorWindow with title {window.title}");
        insert window into incomingWindows;
        return true;
    }

    public override function addInputAnchorWindow(window:ScyWindow, direction:Number):Boolean {
        logger.info("addInputAnchorWindow with title {window.title}");
        insert window into incomingWindows;
        return true;
    }

    public override function addLearningObjectWindow(window:ScyWindow):Boolean {
        logger.info("addLearningObjectWindow with title {window.title}");
        //insert window into windows;
        return false;
    }


    public override function addOtherWindow(window:ScyWindow):Boolean {
        logger.info("addOtherWindow with title {window.title}");
        insert window into centerWindows;
        return true;
    }


    public override function removeOtherWindow(window:ScyWindow):Void {
        logger.info("removeOtherWindow with title {window.title}");
        delete window from centerWindows;
    }

    public override function placeOtherWindow(window:ScyWindow):Boolean {
        logger.info("placeOtherWindow with title {window.title}");
        insert window into centerWindows;
        return true;
    }

    public override function positionWindows(windowPositionsState:WindowPositionsState):Void {
        logger.info("positionWindowsWithState");
        positionWindows();

    }

    public override function positionWindows():Void {
        logger.info("positionWindows");

        if (sizeof incomingWindows == 0 and sizeof centerWindows == 0 and sizeof outgoingWindows == 0) {
            return;
        }

        updateAreas();

        var padding = 50;
        // positioning incoming
        var numberOfWindows = sizeof incomingWindows;
        var windowNumber = 0;
        for (window in incomingWindows) {
            window.layoutX = incomingArea.layoutX + (incomingArea.width / 2) - (window.width / 2);
            window.layoutY = incomingArea.layoutY + padding + ( windowNumber * padding);
            windowNumber++;
        }

        // positioning center
        numberOfWindows = sizeof centerWindows;
        windowNumber = 0;
        for (window in centerWindows) {
            window.layoutX = centerArea.layoutX + (centerArea.width / 2) - (window.width / 2);
            window.layoutY = centerArea.layoutY + padding + ( windowNumber * padding);
            windowNumber++;
        }

        // positioning outgoing
        numberOfWindows = sizeof outgoingWindows;
        windowNumber = 0;
        for (window in outgoingWindows) {
            window.layoutX = outgoingArea.layoutX + (outgoingArea.width / 2) - (window.width / 2);
            window.layoutY = outgoingArea.layoutY + padding + ( windowNumber * padding);
            windowNumber++;
        }

        // debug
        if (not debugAreasAdded) {
            insert debugAreas into scyDesktop.highDebugGroup.content;
            debugAreasAdded = true;
        }
    }
    
    def incomingAreaRatio = 0.2;
    def centerAreaRatio = 0.6;
    def outgoingAreaRatio = 0.2;
    def offset = 10;

    function updateAreas():Void {

        incomingArea.layoutX = offset;
        incomingArea.layoutY = offset;
        incomingArea.width = (incomingAreaRatio * desktopWidth) - (2 * offset);
        incomingArea.height = desktopHeight - (2 * offset);
        //incomingPositioner.area = incomingArea.boundsInLocal;

        centerArea.layoutX = incomingArea.layoutX + incomingArea.width + (2 * offset);
        centerArea.layoutY = offset;
        centerArea.width = (centerAreaRatio * desktopWidth) - (2 * offset);
        centerArea.height = desktopHeight - (2 * offset);
        //centerPositioner.area = centerArea.boundsInLocal;

        outgoingArea.layoutX = centerArea.layoutX + centerArea.width + (2 * offset);
        outgoingArea.layoutY = offset;
        outgoingArea.width = (outgoingAreaRatio * desktopWidth) - (2 * offset);
        outgoingArea.height = desktopHeight - (2 * offset);
        //outgoingPositioner.area = outgoingArea.boundsInLocal;
    }


    public override function getWindowPositionsState():WindowPositionsState {
        logger.info("getWindowPositionsState");
        return WindowPositionsState {};
    }
}
