/*
 * ContactAwarenessRosterListener.fx
 *
 * Created on 24.01.2010, 19:25:32
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;

/**
 * @author svenmaster
 */
 
public class ContactAwarenessRosterListener extends IAwarenessRosterListener {

       public-init var awarenessServiceWrapper:AwarenessServiceWrapper ;
        
        override public function handleAwarenessRosterEvent(e:IAwarenessRosterEvent):Void{
            FX.deferAction(function():Void {
                    delete awarenessServiceWrapper.contactlist.contacts;
                    awarenessServiceWrapper.contactlist.contacts = awarenessServiceWrapper.awarenessUsersToContacts();
                });
            }

        postinit{}

}
