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

public abstract class WindowPositioner {
    public var width;
    public var height;
    public var forbiddenNodes:Node[];
    public abstract function clearWindows():Void;
    public abstract function setCenterWindow(window:ScyWindow):Void;
    public abstract function addLinkedWindow(window:ScyWindow, direction:Number):Void;
    public abstract function addOtherWindow(window:ScyWindow):Void;
    public abstract function setFixedWindows(fixedWindows:ScyWindow[]):Void;

    public abstract function positionWindows():Void;

}
