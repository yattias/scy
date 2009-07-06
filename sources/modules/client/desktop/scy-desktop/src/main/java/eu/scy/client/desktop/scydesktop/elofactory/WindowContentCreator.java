/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;
import javax.swing.JComponent;

/**
 *
 * @author sikkenj
 */
public interface WindowContentCreator {

   public JComponent getScyWindowContent(URI eloUri);
}
