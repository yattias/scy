package eu.scy.client.tools.fxchattool.registration;

import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author giemza
 */

public class PresenceListener extends IAwarenessPresenceListener {

    public-init var ownershipManager : OwnershipManager;

    override public function handleAwarenessPresenceEvent (event : IAwarePresenceEvent) : Void {
        def user = event.getUser();
        def username = StringUtils.parseName(user.getJid());
        var presence = event.getPresence();
        if ("available".equals(presence)) {
            ownershipManager.joined(username);
        } else {
            ownershipManager.left(username);
        }
        ownershipManager.update();
    }

}
