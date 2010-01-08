/*
 * WindowContentGeneratorRegistryImpl.fx
 *
 * Created on 3-jul-2009, 14:29:31
 */

package eu.scy.client.desktop.scydesktop.elofactory;



import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

/**
 * @author sikkenj
 */

public class WindowContentCreatorRegistryFXImpl extends BasicContentCreatorRegistryFX, WindowContentCreatorRegistryFX {
   def logger = Logger.getLogger(this.getClass());

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
