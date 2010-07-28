/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.test;

import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistry;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

/**
 *
 * @author sikken
 */
public class TestRegisterContentCreators implements RegisterContentCreators {

   @Override
   public void registerWindowContentCreators(WindowContentCreatorRegistry windowContentCreatorRegistry)
   {
      windowContentCreatorRegistry.registerWindowContentCreator(new TestWindowContentCreator(), "test");
   }

   @Override
   public void registerNewEloCreation(NewEloCreationRegistry newEloCreationRegistry)
   {
      newEloCreationRegistry.registerEloCreation("scy/test", "test elo");
      newEloCreationRegistry.registerEloCreation("scy/tst", "tst elo");
   }

   @Override
   public void registerDrawerContentCreators(DrawerContentCreatorRegistry drawerContentCreatorRegistry)
   {
//      drawerContentCreatorRegistry.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");
   }

}
