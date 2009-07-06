/*
 * WindowContentGeneratorRegistryImpl.fx
 *
 * Created on 3-jul-2009, 14:29:31
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.util.HashMap;

import java.lang.IllegalArgumentException;

import org.apache.log4j.Logger;

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentCreatorRegistryFXImpl");

public class WindowContentCreatorRegistryFXImpl extends WindowContentCreatorRegistryFX {

   var windowContentCreatorsFXMap = new HashMap();

   public override function registerWindowContentCreatorFX(windowContentCreator: WindowContentCreatorFX, type: String):Void{
      logger.info("registering WindowContentCreatorFX for type {type}");
      checkIfTypeIsDefined(type);
      windowContentCreatorsFXMap.put(type, windowContentCreator);
   }

   public override function registerWindowContentCreator( windowContentCreator: WindowContentCreator,  type: String): Void{
      registerWindowContentCreatorFX(WindowContentCreatorWrapper{windowContentCreator:windowContentCreator },type);
   }

   function checkIfTypeIsDefined(type:String){
      if (typeUsed(type)){
         throw new IllegalArgumentException("type {type} is allready defined");
      }
   }

   function typeUsed(type:String):Boolean{
      return getWindowContentCreatorFX(type)!=null;
   }

   public override function getWindowContentCreatorFX(type:String):WindowContentCreatorFX{
      return windowContentCreatorsFXMap.get(type) as WindowContentCreatorFX
   }


}
