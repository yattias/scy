package eu.scy.client.tools.fxchattool.registration;

import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;
import org.jivesoftware.smack.util.StringUtils;
import java.util.HashMap;

/**
 * @author giemza
 */

public class PresenceListener extends IAwarenessPresenceListener {

    public-init var ownershipManager : OwnershipManager;

    public-init var chattool : ChatterNode;

    var onlineMap: HashMap = new HashMap();

    override public function handleAwarenessPresenceEvent (event : IAwarePresenceEvent) : Void {
        def user = event.getUser();
        def username = StringUtils.parseName(user.getJid());
        var presence = event.getPresence();
        var wasOnline = false;
        if (onlineMap.containsKey(username)) {
            wasOnline = onlineMap.get(username) as Boolean;
        }
        if ("available".equals(presence)) {
            ownershipManager.wentOnline(username);
            if (not wasOnline) {
                chattool.addMessage(null, "{username}", "{username} just went online", true);
                onlineMap.put(username, true);
            }
        } else {
            ownershipManager.wentOffline(username);
            if (wasOnline) {
                chattool.addMessage(null, "{username}", "{username} just went offline", true);
                onlineMap.put(username, false);
            }
        }
    }

}
