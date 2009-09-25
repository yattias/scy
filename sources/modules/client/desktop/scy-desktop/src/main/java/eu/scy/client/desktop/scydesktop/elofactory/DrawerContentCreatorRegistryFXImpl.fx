/*
 * DrawerContentCreatorRegistryFXImpl.fx
 *
 * Created on 22-sep-2009, 17:21:36
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import org.apache.log4j.Logger;



/**
 * @author sikkenj
 */

// place your code here

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.DrawerContentCreatorRegistryFXImpl");

public class DrawerContentCreatorRegistryFXImpl extends BasicContentCreatorRegistryFX, DrawerContentCreatorRegistryFX{

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
