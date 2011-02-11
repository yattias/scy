/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.awareness;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;

/**
 * @author SikkenJ
 */

public class BuddyModel extends Contact {

   public-init var loginName: String;
   public-init var isMe:Boolean;

   public override function toString():String{
      "\{loginName:{loginName},onlineState:{onlineState},isMe:{isMe}\}"
   }

}
