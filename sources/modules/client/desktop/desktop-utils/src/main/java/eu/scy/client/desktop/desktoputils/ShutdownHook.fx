package eu.scy.client.desktop.desktoputils;

import javafx.stage.Stage;
import java.awt.Window;
import com.sun.javafx.tk.swing.WindowStage;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

/**
 * Helper class to add a "real" shutdown hook to a given stage.
 * The function will be executed before the stage is made invisibile.
 *
 * @author weinbrenner
 */
public class ShutdownHook {

    public-init var stage: Stage;
    public-init var shutdownFunction: function():Void;

    init {
        var window: Window = extractWindow(stage);
        var listeners: WindowListener[] = window.getWindowListeners();
        for (wl in listeners) {
            window.removeWindowListener(wl);
        }
        window.addWindowListener(WindowListener {
            override function windowOpened(e: WindowEvent): Void {
            }

            override function windowClosing(e: WindowEvent) {
                shutdownFunction.invoke();
            }

            override function windowClosed(e: WindowEvent) {
            }

            override function windowIconified(e: WindowEvent) {
            }

            override function windowDeiconified(e: WindowEvent) {
            }

            override function windowActivated(e: WindowEvent) {
            }

            override function windowDeactivated(e: WindowEvent) {
            }
        });
        for (wl in listeners) {
            window.addWindowListener(wl);
        }
    }

    function extractWindow(stage: Stage): Window {
        if (stage == null) {
            return null;
        }
        var window = (stage.get$Stage$impl_peer() as WindowStage).window;
        return window;
    }

}
