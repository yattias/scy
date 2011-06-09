/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.awareness.event.IAwarenessInvitationListener;


/**
 * @author giemza
 */

public class ContactAwarenessInvitationListener extends IAwarenessInvitationListener {

    public-init var awarenessServiceWrapper:AwarenessServiceWrapper ;

    public override function handleInvitationEvent(inviter:String, room:String) : Void {
        var scyDesktop = awarenessServiceWrapper.contactlist.scyDesktop;
        var chatWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(room);
        if (chatWindow == null) {
            // create new chat window
            // tell window to join chat
        }


        scyDesktop.windows.addScyWindow(chatWindow);
    }


}
