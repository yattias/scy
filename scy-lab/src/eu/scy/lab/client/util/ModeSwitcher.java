package eu.scy.lab.client.util;

import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.offline.client.Offline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

/**
 * Switches between online/offline mode
 */
public class ModeSwitcher extends ButtonListenerAdapter {

    public void onClick(Button button, EventObject event) {
        if (Gears.checkForGears() == false) {
            return;
        }

        if (Gears.isOnline() == false) {
            LoadIndicator.start("Switching to online mode.");
            Gears.setOnline(true);
            LoadIndicator.stop();
            return;
        }
        
        final String message = "Preparing offline mode";
        LoadIndicator.start(message);
        
        try {
            final ManagedResourceStore managedResourceStore = Offline.getManagedResourceStore();

            new Timer() {

                final String oldVersion = managedResourceStore.getCurrentVersion();
                private String status = ".";
                
                @Override
                public void run() {
                    switch (managedResourceStore.getUpdateStatus()) {
                        case ManagedResourceStore.UPDATE_OK:
                            if (!managedResourceStore.getCurrentVersion().equals(oldVersion)) {
                                Window.alert("Download complete. Please refresh the page.");
                            }
                            Gears.setOnline(false);
                            LoadIndicator.stop();
                            break;
                        case ManagedResourceStore.UPDATE_CHECKING:
                        case ManagedResourceStore.UPDATE_DOWNLOADING:
                            LoadIndicator.start(message + status);
                            status += ".";
                            schedule(500);
                            break;
                        case ManagedResourceStore.UPDATE_FAILED:
                            Window.alert(managedResourceStore.getLastErrorMessage());
                            LoadIndicator.stop();
                            break;
                    }
                }
            }.schedule(500);

        } catch (GearsException e) {
            Window.alert(e.getMessage());
        }
    }
}
