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
    * @param type - the technical type for which the tool should be created
    * @param windowContent - true if the tool will be placed in the window, other is will be placed in a drawer
    * @return
    */
   public JComponent createScyToolComponent(String type, boolean windowContent);

   /**
    * Check if the creator supports the given type
    * 
    * @param type
    * @return true if the type is supported, otherwise false
    */
   public boolean supportType(String type);
}
