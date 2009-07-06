/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.test;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author sikken
 */
public class TestWindowContentCreator implements WindowContentCreator {

   @Override
   public JComponent getScyWindowContent(URI eloUri)
   {
      return new JButton(eloUri.toString());
   }

}
