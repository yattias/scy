/*
 * Contact.fx
 *
 * Created on 22.03.2009, 19:18:20
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.awareness.IAwarenessUser;

/**
 * @author Sven
 */

public class Contact {

    public-init var awarenessUser:IAwarenessUser;
    public var name: String;
    public var onlineState: OnlineState;
    public var currentMission: String;
    public var progress: Number;
    public var imageURL: String;

    public override function toString(): String{
       "Contact\{name:{name},onlineState:{onlineState},status:{awarenessUser.getStatus()}\}"
    }
}
