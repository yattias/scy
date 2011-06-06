/*
 * ContactAwarenessRosterListener.fx
 *
 * Created on 24.01.2010, 19:25:32
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;

/**
 * @author svenmaster
 */
 
public class ContactAwarenessPresenceListener extends IAwarenessPresenceListener {


       public-init var awarenessServiceWrapper:AwarenessServiceWrapper ;
        
        override public function handleAwarenessPresenceEvent(e:IAwarePresenceEvent):Void{
            FX.deferAction(function():Void {
                    delete awarenessServiceWrapper.contactlist.contacts;
                    awarenessServiceWrapper.contactlist.contacts = awarenessServiceWrapper.awarenessUsersToContacts();
                });
}

        postinit{}

}
