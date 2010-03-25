/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.speedtest;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import java.awt.Dimension;
import javax.swing.JComponent;

/**
 *
 * @author sikken
 */
public class SpeedTestPanelCreator implements ScyToolCreator {

   @Override
   public JComponent createScyToolComponent(String eloType, String creatorId, boolean windowContent)
   {
      SpeedTestPanel speedTestPanel = new SpeedTestPanel();
      speedTestPanel.setPreferredSize(new Dimension(200,150));
      return speedTestPanel;
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }

}
