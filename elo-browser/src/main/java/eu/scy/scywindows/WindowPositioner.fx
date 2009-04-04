/*
 * WindowPositioner.fx
 *
 * Created on 25-mrt-2009, 16:49:22
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyWindow;
import javafx.scene.Node;

/**
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
