package eu.scy.client.desktop.scydesktop.owner;

import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.window.TitleBarBuddies;
import eu.scy.client.common.datasync.CollaboratorStatusListener;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import javafx.util.Sequences;

/**
 * @author weinbrenner
 */
public class OwnershipManager extends CollaboratorStatusListener {

    public-init var tbi : ToolBrokerAPI;
    public var elo: ScyElo on replace {
                update();
            };
    public var titleBarBuddies: TitleBarBuddies;
    var owners: Contact[];
    var authorKey;

    // workaround for not ordered receiption of joined and wentOnline events
    var buddyOnlineYetNotJoined: String[];

    postinit {
        authorKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
    }

    public function update(): Void {
        if (elo != null) {
            delete  owners;
            def myName = tbi.getLoginUserName();
            for (authorName in elo.getAuthors()) {
                insert Contact {
                    name: authorName
                    onlineState: if (authorName == myName) OnlineState.IS_ME else OnlineState.OFFLINE
                } into owners;
            }
            titleBarBuddies.buddiesChanged();
        }
    }

    public function getOwners(): Contact[] {
        return owners;
    }

    public function isOwner(name: String): Boolean {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public function addOwner(name: String, persist: Boolean): Void {
        var found = false;
        // either update a previous owner
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name)) {
                found = true;
            }
        }
        if (not found) {
            def myName = tbi.getLoginUserName();
            // or insert a new one
            insert Contact {
                name: name
                onlineState: if (name.equalsIgnoreCase(myName)) OnlineState.IS_ME else OnlineState.OFFLINE
            } into owners;
        }
        if (persist) {
            var metadata = tbi.getELOFactory().createMetadata();
            var mvc = metadata.getMetadataValueContainer(authorKey);
            for (author in elo.getAuthors()) {
                mvc.addValue(new Contribute(author, java.lang.System.currentTimeMillis()));
            }
            mvc.addValue(new Contribute(name, java.lang.System.currentTimeMillis()));
            tbi.getRepository().addMetadata(elo.getUri(), metadata);
        }
        elo.addAuthor(name);

        titleBarBuddies.buddiesChanged();
    }

    public function removeOwner(name: String, persist: Boolean): Void {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name)) {
                delete c from owners;
                if (persist) {
                    var metadata = tbi.getELOFactory().createMetadata();
                    var mvc = metadata.getMetadataValueContainer(authorKey);
                    for (author in elo.getAuthors()) {
                        if (not author.equalsIgnoreCase(name)) {
                            // XXXXXXXXXXXXXXXXXXXXXXXXxx
                            mvc.addValue(author); // TODO mit Contributes arbeiten
                        }
                    }
                    tbi.getRepository().addMetadata(elo.getUri(), metadata);
                } else {
                    elo.removeAuthor(name);
                }

            }
        }
        titleBarBuddies.buddiesChanged();
    }

    public function addPendingOwner(name: String): Void {
        insert Contact {
            name: name
            onlineState: OnlineState.PENDING
        } into owners;
        titleBarBuddies.buddiesChanged();
    }

    public override function joined(name: String): Void {
        var found = false;
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name)) {
                found = true;
            }
        }
        if (not found) {
            def myName = tbi.getLoginUserName();
            // buddy is online if his name has already been added to the list
            // i.e., the buddy online event came before the buddy joined event
            def onlineState = if (Sequences.indexOf(buddyOnlineYetNotJoined, name) > 0) OnlineState.ONLINE else OnlineState.OFFLINE;
            insert Contact {
                name: name
                onlineState: if (name.equalsIgnoreCase(myName)) OnlineState.IS_ME else onlineState
            } into owners;
            titleBarBuddies.buddiesChanged();
            delete name from buddyOnlineYetNotJoined;
        }
    }

    public override function left(name: String): Void {
        wentOffline(name);
    }

    public override function wentOnline(name: String): Void {
        insert name into buddyOnlineYetNotJoined;
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name) and c.onlineState != OnlineState.IS_ME) {
                c.onlineState = OnlineState.ONLINE;
                titleBarBuddies.buddiesChanged();
                delete name from buddyOnlineYetNotJoined;
                return;
            }
        }
    }

    public override function wentOffline(name: String): Void {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name.equalsIgnoreCase(name)) {
                c.onlineState = OnlineState.OFFLINE;
                titleBarBuddies.buddiesChanged();
                return;
            }
        }
    }

}