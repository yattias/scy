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

   public override function registerWindowContentCreatorFX(windowContentCreator: WindowContentCreatorFX, extention: String):Void{
      logger.info("registering WindowContentCreatorFX for extention {extention}");
      checkIfExtentionUsed(extention);
      windowContentCreatorsFXMap.put(extention, windowContentCreator);
   }

   public override function registerWindowContentCreator( windowContentCreator: WindowContentCreator,  extention: String): Void{
      registerWindowContentCreatorFX(WindowContentCreatorWrapper{windowContentCreator:windowContentCreator },extention);
   }

   function checkIfExtentionUsed(extention:String){
      if (extentionUsed(extention)){
         throw new IllegalArgumentException("extention {extention} is allready defined");
      }
   }

   function extentionUsed(extention:String):Boolean{
      return getWindowContentCreatorFX(extention)!=null;
   }

   public override function getWindowContentCreatorFX(extention:String):WindowContentCreatorFX{
      return windowContentCreatorsFXMap.get(extention) as WindowContentCreatorFX
   }


}
