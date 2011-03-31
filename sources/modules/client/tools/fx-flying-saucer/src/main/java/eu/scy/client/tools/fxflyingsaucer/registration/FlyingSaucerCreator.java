/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.tools.fxflyingsaucer.EloFlyingSaucerPanel;
import eu.scy.client.tools.fxflyingsaucer.UrlSource;
import javax.swing.JComponent;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

/**
 *
 * @author sikken
 */
public class FlyingSaucerCreator implements ScyToolCreator
{

   private UrlSource urlSource;
   private DrawerUIIndicator drawerUIIndicator = null;

   public FlyingSaucerCreator()
   {
      this(UrlSource.ELO, null);
   }

   public FlyingSaucerCreator(UrlSource urlSource, DrawerUIIndicator drawerUIIndicator)
   {
      this.urlSource = urlSource;
      this.drawerUIIndicator = drawerUIIndicator;
   }

   @Override
   public JComponent createScyToolComponent(String eloType, String creatorId, boolean windowContent)
   {
      JComponent flyingSaucer = new EloFlyingSaucerPanel(urlSource, drawerUIIndicator);
      return flyingSaucer;
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }
}
