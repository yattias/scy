/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;

/**
 *
 * @author sikken
 */
public class DummyAwarenessService implements IAwarenessService  {

   @Override
   public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener)
   {
   }

   @Override
   public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener)
   {
   }

   @Override
   public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener)
   {
   }

   @Override
   public void addBuddy(String buddy) throws AwarenessServiceException
   {
   }

   @Override
   public void addChatToolListener(IChatPresenceToolListener listener)
   {
   }

   @Override
   public void addPresenceToolListener(IChatPresenceToolListener listener)
   {
   }

   @Override
   public void disconnect()
   {
   }

   @Override
   public List<IAwarenessUser> getBuddies() throws AwarenessServiceException
   {
      return new ArrayList<IAwarenessUser>();
   }

   @Override
   public void init(XMPPConnection connection)
   {
   }

   @Override
   public boolean isConnected()
   {
      return false;
   }

   @Override
   public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener)
   {
   }

   @Override
   public void removeBuddy(String buddy) throws AwarenessServiceException
   {
   }

   @Override
   public void sendMessage(String recipient, String message) throws AwarenessServiceException
   {
   }

   @Override
   public void setPresence(String presence) throws AwarenessServiceException
   {
   }

   @Override
   public void setStatus(String status) throws AwarenessServiceException
   {
   }

   @Override
   public void updateChatTool(List<IAwarenessUser> users)
   {
   }

   @Override
   public void updatePresenceTool(List<IAwarenessUser> users)
   {
   }

}
