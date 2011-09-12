/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author giemza
 */
public class ChatManager {

    private Map<String, URI> userUriMap;

    public ChatManager() {
        userUriMap = new HashMap<String, URI>();
    }

    public void addChatMapping(String user, URI uri) {
        userUriMap.put(user, uri);
    }

    public URI getChatMappingForUser(String user) {
        return userUriMap.get(user);
    }

}
