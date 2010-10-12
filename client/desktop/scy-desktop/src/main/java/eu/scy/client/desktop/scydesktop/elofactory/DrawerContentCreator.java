/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;
import javax.swing.JComponent;

/**
 * Interface for creating a JComponent to be placed in the a drawer on ELO window.
 *
 * @author sikkenj
 */
public interface DrawerContentCreator extends ContentCreator
{

   /**
    * Create a new JComponent to be placed in a drawer of a scy window, with an existing ELO
    *
    */
   public JComponent getDrawerContent(URI eloUri);
}
