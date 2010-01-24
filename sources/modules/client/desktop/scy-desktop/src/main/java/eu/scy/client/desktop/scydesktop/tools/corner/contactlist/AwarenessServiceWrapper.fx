/*
 * AwarenessServiceWrapper.fx
 *
 * Created on 24.01.2010, 18:23:58
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactList;


import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;


import eu.scy.awareness.IAwarenessService;


import eu.scy.awareness.IAwarenessUser;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;

/**
 * @author svenmaster
 */

public class AwarenessServiceWrapper {

        public-init var contactlist:ContactList;

        def awarenessService:IAwarenessService = bind contactlist.scyDesktop.config.getToolBrokerAPI().getAwarenessService();

        init{
            awarenessService.addAwarenessRosterListener(ContactAwarenessRosterListener{awarenessServiceWrapper : this});
        }

        postinit{
            contactlist.contacts = awarenessUsersToContacts();
        }


        public function awarenessUsersToContacts():Contact[]{
            var contacts:Contact[];
            def buddies = awarenessService.getBuddies();
            for (buddy in buddies){
                def awarenessUser:IAwarenessUser = buddy as IAwarenessUser;
                def presence = awarenessUser.getPresence();
                def contact:Contact = Contact{
                    currentMission: awarenessUser.getStatus();    //Not sure
                    name: awarenessUser.getNickName();
                    onlineState: if (presence.equals("unavailable")) OnlineState.OFFLINE else
                        (if(presence.equals("idle")) OnlineState.AWAY else OnlineState.ONLINE );
                    imageURL: if (presence.equals("unavailable")) "img/buddyicon_offline.png" else
                        (if(presence.equals("idle")) "img/buddyicon_idle.png" else "img/buddyicon_online.png" );
                    }
                //XXX only insert online/idle contacts
                if (not presence.equals("unavailable")){
                    insert contact into contacts;
                }
                }
            return contacts;
        }



}
