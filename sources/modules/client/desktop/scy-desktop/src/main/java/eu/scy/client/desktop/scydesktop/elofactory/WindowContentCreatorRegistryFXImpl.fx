/*
 * WindowContentGeneratorRegistryImpl.fx
 *
 * Created on 3-jul-2009, 14:29:31
 */

package eu.scy.client.desktop.scydesktop.elofactory;



import org.apache.log4j.Logger;

/**
 * @author sikkenj
 */

def logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentCreatorRegistryFXImpl");

public class WindowContentCreatorRegistryFXImpl extends BasicContentCreatorRegistryFX, WindowContentCreatorRegistryFX {

   public override function registerWindowContentCreatorFX(windowContentCreator: WindowContentCreatorFX, id: String):Void{
      logger.info("registering WindowContentCreatorFX for id {id}, class {windowContentCreator.getClass()}");
      registerContentCreatorFX(windowContentCreator,id);
   }

   public override function registerWindowContentCreator( windowContentCreator: WindowContentCreator,  id: String): Void{
      registerWindowContentCreatorFX(WindowContentCreatorWrapper{windowContentCreator:windowContentCreator },id);
   }

   public override function getWindowContentCreatorFX(id:String):WindowContentCreatorFX{
      return getContentCreatorFX(id) as WindowContentCreatorFX
   }
}
