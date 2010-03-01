/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.test;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URI;
import javax.swing.JComponent;

/**
 *
 * @author sikkenj
 */
public class SwingSizeTestPanelCreator implements WindowContentCreator {

   @Override
   public JComponent getScyWindowContent(URI eloUri)
   {
      return getScyWindowContentNew();
   }

   @Override
   public JComponent getScyWindowContentNew()
   {
      SwingSizeTestPanel swingSizeTestPanel = new SwingSizeTestPanel();
      swingSizeTestPanel.setBackground(Color.LIGHT_GRAY);
      swingSizeTestPanel.setForeground(Color.RED);
      //swingSizeTestPanel.setMinimumSize(new Dimension(50,50));
     //swingSizeTestPanel.setPreferredSize(new Dimension(250,250));
      return swingSizeTestPanel;
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }

}
