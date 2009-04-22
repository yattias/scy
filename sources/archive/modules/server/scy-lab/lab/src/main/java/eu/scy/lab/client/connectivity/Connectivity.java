package eu.scy.lab.client.connectivity;

import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.localserver.LocalServer;
import com.google.gwt.user.client.Window;
import com.gwtext.client.widgets.ToolbarButton;

/**
 * Helper methods for Gears and management of online/offline mode.
 */
public class Connectivity {

    private static boolean online = true;
    private static ToolbarButton modeButton;
    
    public static boolean isOnline() {
        return online;
    }
    
    public static void setOnline(boolean b) {
        online = b;
        updateModeButton();
    }
    
    public static native boolean checkForGears() /*-{
        var available = $wnd.google && $wnd.google.gears;
        return available != null;
    }-*/;

    public static boolean canServeLocally() {
        if (checkForGears() == false) {
            return false;
        }
        try {
            LocalServer server = Factory.getInstance().createLocalServer();
            return server.canServeLocally(Window.Location.getPath());
        } catch (GearsException ex) {
            Window.alert("Exception: " + ex);
            return false;
        }
    }

    public static void setModeButton(ToolbarButton button) {
        modeButton = button;
        updateModeButton();
    }

    private static void updateModeButton() {
        if (checkForGears() == false) {
            modeButton.setText("offline mode not available: Please install Gears.");
            modeButton.disable();
        } else if (isOnline()) {
            modeButton.setText("work offline");
        } else {
            modeButton.setText("work online");
        }
    }
}
