/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxflyingsaucer.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.tools.fxflyingsaucer.EloFlyingSaucerPanel;
import javax.swing.JComponent;

/**
 *
 * @author sikken
 */
public class FlyingSaucerCreator implements ScyToolCreator {

   @Override
   public JComponent createScyToolComponent()
   {
      return new EloFlyingSaucerPanel();
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }

}
