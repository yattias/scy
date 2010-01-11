/*
 * DrawerContentCreatorRegistryFXImpl.fx
 *
 * Created on 22-sep-2009, 17:21:36
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.elofactory.impl.DrawerContentCreatorWrapper;



/**
 * @author sikkenj
 */

// place your code here

public class DrawerContentCreatorRegistryFXImpl extends BasicContentCreatorRegistryFX, DrawerContentCreatorRegistryFX{
   def logger = Logger.getLogger(this.getClass());

   public override function registerDrawerContentCreator(drawerContentCreator: DrawerContentCreator, id: String):Void{
      registerDrawerContentCreatorFX(DrawerContentCreatorWrapper{drawerContentCreator:drawerContentCreator },id);
   }

   public override function registerDrawerContentCreatorFX(drawerContentCreator: DrawerContentCreatorFX, id: String):Void{
      logger.info("registering DrawerContentCreatorFX with id {id}, class {drawerContentCreator.getClass()}");
      registerContentCreatorFX(drawerContentCreator,id);
   }

   public override function getDrawerContentCreatorFX(id:String):DrawerContentCreatorFX{
      return getContentCreatorFX(id) as DrawerContentCreatorFX
   }


}
