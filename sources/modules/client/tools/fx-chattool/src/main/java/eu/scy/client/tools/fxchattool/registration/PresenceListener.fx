package eu.scy.client.tools.fxchattool.registration;

import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;

/**
 * @author giemza
 */

public class PresenceListener extends IAwarenessPresenceListener {

    public-init var ownershipManager : OwnershipManager;

    override public function handleAwarenessPresenceEvent (event : IAwarePresenceEvent) : Void {
        var user = event.getUser();
        var status = event.getStatus();
        ownershipManager.update();
    }

}
