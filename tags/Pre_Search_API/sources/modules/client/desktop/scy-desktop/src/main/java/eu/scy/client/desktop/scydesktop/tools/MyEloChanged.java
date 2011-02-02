/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools;

import eu.scy.common.scyelo.ScyElo;

/**
 * This interface is there for the tool to report that THE elo, which the tool is displaying, has been loaded or saved. For any loading and saving organized by the ScyTool or EloSaver interfaces, you do not have to report a change of elo.
 * @author sikken
 */
public interface MyEloChanged {

   /**
    * report THE elo displayed in your tool has changed, by a load or save action. You should only use this method if the tool organizes its own loading or saving of the elo and not is initiated by the ScyTool or EloSaver interfaces.
    * @param elo
    */
   public void myEloChanged(ScyElo elo);
}
