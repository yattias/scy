/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;
import javax.swing.JComponent;

/**
 * Interface for creating a JComponent to display/edit an ELO.
 *
 * @author sikkenj
 */
public interface WindowContentCreator {

   /**
    * Create a new JComponent to display/edit an existing ELO
    *
    */
   public JComponent getScyWindowContent(URI eloUri);

   /**
    * Create a new JComponent to display/edit an new ELO
    *
    */
   public JComponent getScyWindowContentNew();
}
