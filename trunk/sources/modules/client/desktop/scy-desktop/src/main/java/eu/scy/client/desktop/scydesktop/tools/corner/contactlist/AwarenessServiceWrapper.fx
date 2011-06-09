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
import eu.scy.common.configuration.Configuration;

/**
 * @author svenmaster
 */


public class AwarenessServiceWrapper {

        public-init var contactlist:ContactList;
        
        def config:Configuration=Configuration.getInstance();
        def filestreamerServer:String = config.getFilestreamerServer();
        def filestreamerContext:String = config.getFilestreamerContext();
        def filestreamerPort:String = config.getFilestreamerPort();
        //this should look like:
        //"http://scy.collide.info:8080/webapp/common/filestreamer.html";
        public def IMAGE_BASE_DIR = "http://{filestreamerServer}:{filestreamerPort}/{filestreamerContext}";

        def awarenessService:IAwarenessService = bind contactlist.scyDesktop.config.getToolBrokerAPI().getAwarenessService();

        init{
            awarenessService.addAwarenessRosterListener(ContactAwarenessRosterListener{awarenessServiceWrapper : this});
            awarenessService.addAwarenessPresenceListener(ContactAwarenessPresenceListener{awarenessServiceWrapper : this});
            awarenessService.addInvitationListener(ContactAwarenessRosterListener{awarenessServiceWrapper : this});
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
                    awarenessUser: awarenessUser;
                    currentMission: awarenessUser.getStatus();    //Not sure
                    name: awarenessUser.getNickName();
                    onlineState: if (presence.equals("unavailable")) OnlineState.OFFLINE else
                        (if(presence.equals("idle")) OnlineState.AWAY else OnlineState.ONLINE );
                    imageURL: "{IMAGE_BASE_DIR}?username={awarenessUser.getNickName()}";
                    }
                //filter names "" and " "
                if (not(contact.name.equals("")) and not(contact.name.equals(" ")))
                {
                    if (presence.equals("unavailable")){
                        insert contact into offlineContacts;
                    } else {
                        insert contact into contacts;
                    }
                }
                }
                if (contactlist.showOfflineContacts){
                    insert offlineContacts into contacts;
                }

            return contacts;
        }



}
