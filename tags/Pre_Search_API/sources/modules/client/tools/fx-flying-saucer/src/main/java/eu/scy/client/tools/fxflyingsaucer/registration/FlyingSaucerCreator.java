/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxflyingsaucer.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.tools.fxflyingsaucer.EloFlyingSaucerPanel;
import eu.scy.client.tools.fxflyingsaucer.UrlSource;
import javax.swing.JComponent;

/**
 *
 * @author sikken
 */
public class FlyingSaucerCreator implements ScyToolCreator {
   private UrlSource urlSource;

   public FlyingSaucerCreator()
   {
      this(UrlSource.ELO);
   }

   public FlyingSaucerCreator(UrlSource urlSource)
   {
      this.urlSource = urlSource;
   }

   @Override
   public JComponent createScyToolComponent(String eloType, String creatorId, boolean windowContent)
   {
      JComponent flyingSaucer = new EloFlyingSaucerPanel(urlSource);
      return flyingSaucer;
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }

}
