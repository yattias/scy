/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.awareness.impl;

import eu.scy.client.desktop.scydesktop.awareness.BuddyManager;
import eu.scy.client.desktop.scydesktop.awareness.BuddyModel;
import java.util.HashMap;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author SikkenJ
 */
public class BuddyManagerImpl extends BuddyManager, IAwarenessPresenceListener {

   public var tbi: ToolBrokerAPI;
   def buddiesMap = new HashMap();

   public override function getBuddyModel(loginName: String): BuddyModel {
      var buddyModel = findBuddyModel(loginName);
      if (buddyModel == null) {
         def isMe = loginName == tbi.getLoginUserName();
         buddyModel = BuddyModel{
            loginName: loginName
            isMe: isMe
            name: loginName
            onlineState: if (isMe) OnlineState.ONLINE else OnlineState.OFFLINE
         }
         buddiesMap.put(loginName, buddyModel);
      }
      buddyModel
   }

   public override function handleAwarenessPresenceEvent(e: IAwarePresenceEvent): Void {
      def loginName = getLoginName(e);
      def buddyModel = findBuddyModel(loginName);
      if (buddyModel != null) {
         functionUpdateBuddyModel(buddyModel, e);
      }

   }

   function findBuddyModel(name: String): BuddyModel {
      buddiesMap.get(name) as BuddyModel;
   }

   function getLoginName(e: IAwarePresenceEvent): String {
      def fullUserName = e.getUser().getJid();

   }

   function functionUpdateBuddyModel(buddyModel: BuddyModel, e: IAwarePresenceEvent) {
      def presence = e.getPresence();
      buddyModel.onlineState = if (presence.equals("unavailable")) OnlineState.OFFLINE else
            (if (presence.equals("idle")) OnlineState.AWAY else OnlineState.ONLINE);
   }

}
