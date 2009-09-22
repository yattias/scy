/*
 * DrawerContentCreatorRegistryFXImpl.fx
 *
 * Created on 22-sep-2009, 17:21:36
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import org.apache.log4j.Logger;

import java.util.HashMap;

import java.lang.IllegalArgumentException;

/**
 * @author sikkenj
 */

// place your code here

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.DrawerContentCreatorRegistryFXImpl");

public class DrawerContentCreatorRegistryFXImpl extends DrawerContentCreatorRegistryFX{

   var drawerContentCreatorsFXMap = new HashMap();

   public override function registerDrawerContentCreator(drawerContentCreator: DrawerContentCreator, id: String):Void{
      registerDrawerContentCreatorFX(DrawerContentCreatorWrapper{drawerContentCreator:drawerContentCreator },id);
   }

   public override function registerDrawerContentCreatorFX(drawerContentCreator: DrawerContentCreatorFX, id: String):Void{
      logger.info("registering DrawerContentCreatorFX with id {id}, class {drawerContentCreator.getClass()}");
      checkIfIdIsDefined(id);
      drawerContentCreatorsFXMap.put(id, drawerContentCreator);
   }

   function checkIfIdIsDefined(id:String){
      if (idUsed(id)){
         throw new IllegalArgumentException("id {id} is already defined");
      }
   }

   function idUsed(id:String):Boolean{
      return getDrawerContentCreatorFX(id)!=null;
   }

   public override function getDrawerContentCreatorFX(id:String):DrawerContentCreatorFX{
      return drawerContentCreatorsFXMap.get(id) as DrawerContentCreatorFX
   }


}
