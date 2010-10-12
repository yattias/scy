/*
 * WindowPositioner.fx
 *
 * Created on 25-mrt-2009, 16:49:22
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;

/**
 * The WindowPositioner task is the position (or reposition) the windows on the desktop.
 * It works with three kinds of windows:
 * - active anchor elo window (setCenterWindow)
 * - other anchor windows (addLinkedWindows, direction is calculated from the mission map)
 * - other windows (addOtherWindows)
 * - fixed windows, I don't remember
 *
 * @author sikkenj
 */

public mixin class WindowPositioner {
    public var width;
    public var height;
    public var forbiddenNodes:Node[];

    /**
    * adds a global learning object window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addGlobalLearningObjectWindow(window:ScyWindow):Boolean;

    /**
    * all windows are rmoved from the desktop
    */
    public abstract function clearWindows():Void;

    public abstract function makeMainWindow(window:ScyWindow):Void;

    /**
    * set the main anchor window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function setAnchorWindow(window:ScyWindow):Boolean;

    /**
    * adds a intermediate anchor window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addIntermediateWindow(window:ScyWindow):Boolean;

    /**
    * adds a next anchor window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addNextAnchorWindow(window:ScyWindow, direction:Number):Boolean;

    /**
    * adds a previous anchor window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addPreviousAnchorWindow(window:ScyWindow, direction:Number):Boolean;

    /**
    * adds an input anchor window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addInputAnchorWindow(window:ScyWindow, direction:Number):Boolean;

    /**
    * adds a learning object window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addLearningObjectWindow(window:ScyWindow):Boolean;

    /**
    * adds an other window.
    *
    * Positioning could be postphoned until positionWindows is called
    */
    public abstract function addOtherWindow(window:ScyWindow):Boolean;

    /**
    * removes an other window.
    *
    * there is no need to position any thing
    */
    public abstract function removeOtherWindow(window:ScyWindow):Void;

    /**
    * adds an other window.
    *
    * The window should be positioned immediately
    */
    public abstract function placeOtherWindow(window:ScyWindow):Boolean;

    /**
    * restore the position and state of the windows
    */
    public abstract function positionWindows(windowPositionsState:WindowPositionsState):Void;

    /**
    * no previous state information available, place the windows automaticly
    */
    public abstract function positionWindows():Void;

    /**
    * returns the WindowPositionsState object, containing the position and state information of the current windows
    */
    public abstract function getWindowPositionsState():WindowPositionsState;

}
