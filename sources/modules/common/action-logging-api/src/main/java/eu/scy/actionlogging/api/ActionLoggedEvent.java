/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.actionlogging.api;

/**
 *
 * @author SikkenJ
 */
public class ActionLoggedEvent {

   private IAction action;

   public ActionLoggedEvent(IAction action)
   {
      this.action = action;
   }

   public IAction getAction()
   {
      return action;
   }
}
