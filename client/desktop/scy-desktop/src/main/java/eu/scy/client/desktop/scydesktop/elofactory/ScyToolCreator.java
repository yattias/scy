/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory;

import javax.swing.JComponent;

/**
 * The interface for creating new instances of Java only tools in SCY-LAB.
 *
 * @author sikken
 */
public interface ScyToolCreator
{
   /**
    * Create a new JComponent to display/edit an existing ELO.
    *
    * The eloType and creatorId can be used to determine what JComponent to return, in case the creator is capable of returning multiple types.
    *
    * @param eloType - the technical type of the elo in the window
    * @param creatorId - the id of the registered creator
    * @param windowContent - true if the tool will be placed in the window, other is will be placed in a drawer
    * @return
    */
   public JComponent createScyToolComponent(String eloType, String creatorId, boolean windowContent);

   /**
    * Check if the creator supports the given type
    * 
    * @param type
    * @return true if the type is supported, otherwise false
    */
   public boolean supportType(String type);
}
