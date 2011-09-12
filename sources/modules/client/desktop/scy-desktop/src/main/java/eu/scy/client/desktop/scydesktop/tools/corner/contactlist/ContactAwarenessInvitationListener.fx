/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.awareness.event.IAwarenessInvitationListener;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


/**
 * @author giemza
 */

public class ContactAwarenessInvitationListener extends IAwarenessInvitationListener {

    public-init var awarenessServiceWrapper:AwarenessServiceWrapper ;

    public override function handleInvitationEvent(inviter:String, room:String) : Void {
        var contactList = awarenessServiceWrapper.contactlist;
        contactList.openChat(inviter);
    }
}