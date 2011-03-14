package eu.scy.client.desktop.scydesktop.owner;

import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.window.TitleBarBuddies;
import eu.scy.client.common.datasync.CollaboratorStatusListener;

/**
 * @author weinbrenner
 */
public class OwnershipManager extends CollaboratorStatusListener {

    public-init var myName : String;
    public var elo: ScyElo on replace {
                update();
            };
    public var titleBarBuddies: TitleBarBuddies;
    var owners: Contact[];

    public function update(): Void {
        if (elo != null) {
            delete  owners;
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
            if (c.name == name) {
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
            if (c.name == name) {
                found = true;
            }
        }
        if (not found) {
            // or insert a new one
            insert Contact {
                name: name
                onlineState: if (name == myName) OnlineState.IS_ME else OnlineState.OFFLINE
            } into owners;
        }
        if (persist) {
            elo.addAuthor(name);
        }
        titleBarBuddies.buddiesChanged();
    }

    public function removeOwner(name: String, persist: Boolean): Void {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name == name) {
                delete c from owners;
                if (persist) {
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
            if (c.name == name) {
                found = true;
            }
        }
        if (not found) {
            insert Contact {
                name: name
                onlineState: if (name == myName) OnlineState.IS_ME else OnlineState.OFFLINE
            } into owners;
            titleBarBuddies.buddiesChanged();
        }
    }

    public override function left(name: String): Void {
        wentOffline(name);
    }

    public override function wentOnline(name: String): Void {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name == name and c.onlineState != OnlineState.IS_ME) {
                c.onlineState = OnlineState.ONLINE;
                titleBarBuddies.buddiesChanged();
                return;
            }
        }
    }

    public override function wentOffline(name: String): Void {
        for (contact in owners) {
            def c = contact as Contact;
            if (c.name == name) {
                c.onlineState = OnlineState.OFFLINE;
                titleBarBuddies.buddiesChanged();
                return;
            }
        }
    }

}