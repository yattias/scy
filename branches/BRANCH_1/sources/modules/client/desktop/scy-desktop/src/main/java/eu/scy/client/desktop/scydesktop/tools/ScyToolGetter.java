/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools;

/**
 * This interface can be used to specify an other object as ScyTool instead of the Node or JComponent
 * @author sikken
 */
public interface ScyToolGetter {

   /**
    * Return the object implementing the ScyTool interface
    * 
    * @return
    */
   public ScyTool getScyTool();
}
