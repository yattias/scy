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
        
        public def IMAGE_BASE_DIR = "http://scy.googlecode.com/files/";

        def awarenessService:IAwarenessService = bind contactlist.scyDesktop.config.getToolBrokerAPI().getAwarenessService();

        init{
            awarenessService.addAwarenessRosterListener(ContactAwarenessRosterListener{awarenessServiceWrapper : this});
        }

        postinit{
            contactlist.contacts = awarenessUsersToContacts();
        }


        public function awarenessUsersToContacts():Contact[]{
            var contacts:Contact[];
            var offlineContacts:Contact[];
            def buddies = awarenessService.getBuddies();
            for (buddy in buddies){
                def awarenessUser:IAwarenessUser = buddy as IAwarenessUser;
                def presence = awarenessUser.getPresence();
                def contact:Contact = Contact{
                    currentMission: awarenessUser.getStatus();    //Not sure
                    name: awarenessUser.getNickName();
                    onlineState: if (presence.equals("unavailable")) OnlineState.OFFLINE else
                        (if(presence.equals("idle")) OnlineState.AWAY else OnlineState.ONLINE );
                    imageURL: if (presence.equals("unavailable")) "{IMAGE_BASE_DIR}buddyicon_offline.png" else
                        (if(presence.equals("idle")) "{IMAGE_BASE_DIR}buddyicon_idle.png" else "{IMAGE_BASE_DIR}buddyicon_online.png" );
                    }
                //XXX only insert online/idle contacts
                if (presence.equals("unavailable")){
                    insert contact into offlineContacts;
                } else {
                    insert contact into contacts;
                }
                
                }
                if (contactlist.showOfflineContacts){
                    insert offlineContacts into contacts;
                }

            return contacts;
        }



}
