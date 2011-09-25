/*
 * ScyDesktopImpl.fx
 *
 * Created on 22-jun-2009, 15:49:10
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Sequences;
import javafx.scene.Group;
import java.net.URI;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import java.lang.Void;

/**
 * @author sikkenj
 */
public class WindowManagerImpl extends WindowManager {

    def logger = Logger.getLogger(this.getClass());
    public-read override var scyWindows = Group {};
    public override var activeWindow: ScyWindow on replace previousActiveWindow {
        if (previousActiveWindow != null) {
            setDeactiveWindowState(previousActiveWindow);
        }
        if (activeWindow != null) {
            setActiveWindowState(activeWindow);
        }
    };
    var windowStateListMap: Map = new HashMap();

    function setActiveWindowState(scyWindow: ScyWindow) {
        scyWindow.activated = true;
    }

    function setDeactiveWindowState(scyWindow: ScyWindow) {
        scyWindow.activated = false;
    }

    public override function addScyWindow(scyWindow: ScyWindow) {
        logger.info("addScyWindow({scyWindow.eloUri})");
        scyWindow.windowManager = this;
        setDeactiveWindowState(scyWindow);
        if (not desktopContainsWindow(scyWindow)) {
            insert scyWindow into scyWindows.content;
        } else {
            logger.warn("Trying to add scyWindow {scyWindow.eloUri}, but it is allready there");
        }
    }

    public override function removeScyWindow(scyWindow: ScyWindow) {
        logger.info("removeScyWindow({scyWindow.eloUri})");
        if (activeWindow == scyWindow){
           activeWindow = null;
           setDeactiveWindowState(scyWindow)
        }
        var index = Sequences.indexOf(scyWindows.content, scyWindow);
        if (index >= 0) {
            delete  scyWindows.content[index];
        } else {
            logger.warn("Trying to remove scyWindow {scyWindow.eloUri}, but it is not there");
        }
    }

    public override function activateScyWindow(scyWindow: ScyWindow) {
        logger.info("activateScyWindow({scyWindow.eloUri})");
        if (desktopContainsWindow(scyWindow)) {
            activeWindow = scyWindow;
            activeWindow.toFront();
        } else {
            logger.warn("There is no scyWindow {scyWindow.eloUri}");
        }
    }

    public override function hideScyWindow(scyWindow: ScyWindow) {
        logger.info("hideScyWindow({scyWindow.eloUri})");
        if (desktopContainsWindow(scyWindow)) {
            scyWindow.visible = false;
        } else {
            logger.warn("There is no scyWindow {scyWindow.eloUri}");
        }
    }

    public override function showScyWindow(scyWindow: ScyWindow) {
        logger.info("showScyWindow({scyWindow.id})");
        if (desktopContainsWindow(scyWindow)) {
            scyWindow.visible = true;
        } else {
            logger.warn("There is no scyWindow {scyWindow.eloUri}");
        }
    }

    public override function findScyWindow(uri: URI): ScyWindow {
        for (node in scyWindows.content) {
            if (node instanceof ScyWindow) {
                var window = node as ScyWindow;
                if (window.eloUri == uri) {
                    return window;
                }
            }
        }
        return null;
    }

    public override function getScyWindows(): ScyWindow[] {
        return for (window in scyWindows.content) {
                    window as ScyWindow;
                }
    }

    public override function hasWindow(scyWindow:ScyWindow): Boolean {
       desktopContainsWindow(scyWindow);
    }

    function desktopContainsWindow(scyWindow: ScyWindow): Boolean {
        var index = Sequences.indexOf(scyWindows.content, scyWindow);
        return index >= 0;
    }

    public override function removeAllScyWindows(): Void {
        delete  scyWindows.content;
        activeWindow = null;
    }

    public override function getWindowUnderMouse(sceneX: Number, sceneY: Number): ScyWindow {
        for (index in [sizeof scyWindows.content - 1..0 step -1]) {
            var window = scyWindows.content[index] as ScyWindow;
            var localMousePosition = window.sceneToLocal(sceneX, sceneY);
            if (window.contains(localMousePosition)) {
                return window;
            }
        }
        return null;
    }

}
