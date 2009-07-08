/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.test;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.RegisterWindowContentCreators;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistry;

/**
 *
 * @author sikken
 */
public class TestRegisterWindowContentCreators implements RegisterWindowContentCreators {

   @Override
   public void registerWindowContentCreators(WindowContentCreatorRegistry windowContentCreatorRegistry)
   {
      windowContentCreatorRegistry.registerWindowContentCreator(new TestWindowContentCreator(), "test");
   }

   @Override
   public void registerNewEloCreation(NewEloCreationRegistry newEloCreationRegistry)
   {
      newEloCreationRegistry.registerEloCreation("test", "test elo");
      newEloCreationRegistry.registerEloCreation("tst", "tst elo");
   }

}
