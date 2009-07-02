/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.missionmap;

import java.util.EventObject;

/**
 *
 * @author sikkenj
 */
public class ActiveAnchorChangedEvent  extends EventObject {

   private Anchor oldActiveAnchor;
   private Anchor newActiveAnchor;

   public ActiveAnchorChangedEvent(Object source, Anchor oldActiveAnchor, Anchor newActiveAnchor)
   {
      super(source);
      this.oldActiveAnchor = oldActiveAnchor;
      this.newActiveAnchor = newActiveAnchor;
   }

   public Anchor getNewActiveAnchor()
   {
      return newActiveAnchor;
   }

   public Anchor getOldActiveAnchor()
   {
      return oldActiveAnchor;
   }

   
}
