/*
 * WindowPositioner.fx
 *
 * Created on 25-mrt-2009, 16:49:22
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;

/**
 * The WindowPositioner task is the position (or reposition) the windows on the desktop
 *
 * It works with the following kind of windows:
 * - global learning object windows
 *   this are resource like elos, which are present in all lasses
 * - anchor window
 *   the main anchor elo of the current las
 * - intermediate window
 *   this are the intermediate elos of the current las
 * - next anchor window
 *   the main anchor elos of the next lasses, from the current las
 * - previous anchor window
 *   the main anchor elos of the previous lasses, from the current las
 * - input anchor window
 *   all the input anchors of the main and intermediate anchor elos of the current las
 * - learning object windows
 *   this are resource like elos of the current las
 * - other window
 *   this are elos added by the user to the desktop, which are allways stored with the current las
 *   
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
    * all windows are removed from the desktop
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
