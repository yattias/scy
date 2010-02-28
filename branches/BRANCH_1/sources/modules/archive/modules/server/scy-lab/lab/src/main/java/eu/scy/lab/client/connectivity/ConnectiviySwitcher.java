package eu.scy.lab.client.connectivity;

import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.offline.client.Offline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

import eu.scy.global.client.LoadIndicator;

/**
 * Switches between online/offline mode
 */
public class ConnectiviySwitcher extends ButtonListenerAdapter {

    public void onClick(Button button, EventObject event) {
        if (Connectivity.checkForGears() == false) {
            return;
        }

        if (Connectivity.isOnline() == false) {
            //LoadIndicator.start("Switching to online mode.");
            Connectivity.setOnline(true);
            //LoadIndicator.stop();
            return;
        }
        
        final String message = "Preparing offline mode";
        //LoadIndicator.start(message);
        
        try {
            final ManagedResourceStore managedResourceStore = Offline.getManagedResourceStore();

            new Timer() {

                private String status = ".";
                
                @Override
                public void run() {
                    switch (managedResourceStore.getUpdateStatus()) {
                        case ManagedResourceStore.UPDATE_OK:
                            Connectivity.setOnline(false);
                            //LoadIndicator.stop();
                            break;
                        case ManagedResourceStore.UPDATE_CHECKING:
                        case ManagedResourceStore.UPDATE_DOWNLOADING:
                            //LoadIndicator.start(message + status);
                            status += ".";
                            schedule(500);
                            break;
                        case ManagedResourceStore.UPDATE_FAILED:
                            Window.alert(managedResourceStore.getLastErrorMessage());
                            //LoadIndicator.stop();
                            break;
                    }
                }
            }.schedule(500);

        } catch (GearsException e) {
            Window.alert(e.getMessage());
        }
    }
}
