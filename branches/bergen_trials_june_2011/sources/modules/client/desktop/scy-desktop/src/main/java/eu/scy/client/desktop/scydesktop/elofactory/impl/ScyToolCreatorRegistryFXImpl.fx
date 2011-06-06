/*
 * ScyToolCreatorRegistryFXImpl.fx
 *
 * Created on 11-jan-2010, 12:22:35
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;

/**
 * @author sikken
 */
public class ScyToolCreatorRegistryFXImpl extends BasicContentCreatorRegistryFX,
   ScyToolCreatorRegistryFX, DrawerContentCreatorRegistryFX, WindowContentCreatorRegistryFX {

   def logger = Logger.getLogger(this.getClass());
   public var config: Config;

   override public function registerScyToolCreator(scyToolCreator: ScyToolCreator, id: String): Void {
      registerScyToolCreatorFX(ScyToolCreatorWrapper {scyToolCreator: scyToolCreator}, id);
   }

   override public function registerScyToolCreatorFX(scyToolCreatorFX: ScyToolCreatorFX, id: String): Void {
      logger.info("registering ScyToolCreatorFX with id {id}, class {scyToolCreatorFX.getClass()}");
      registerContentCreatorFX(scyToolCreatorFX, id);
   }

   override public function getScyToolCreatorFX(id: String): ScyToolCreatorFX {
      var creator = getContentCreatorFX(id);
      if (creator instanceof ScyToolCreatorFX) {
         return getContentCreatorFX(id) as ScyToolCreatorFX
      }
      return null;
   }

   public override function registerDrawerContentCreator(drawerContentCreator: DrawerContentCreator, id: String): Void {
      registerDrawerContentCreatorFX(DrawerContentCreatorWrapper {drawerContentCreator: drawerContentCreator}, id);
   }

   public override function registerDrawerContentCreatorFX(drawerContentCreator: DrawerContentCreatorFX, id: String): Void {
      logger.info("registering DrawerContentCreatorFX with id {id}, class {drawerContentCreator.getClass()}");
      registerContentCreatorFX(drawerContentCreator, id);
   }

   public override function getDrawerContentCreatorFX(id: String): DrawerContentCreatorFX {
      var creator = getContentCreatorFX(id);
      if (creator instanceof DrawerContentCreatorFX) {
         return creator as DrawerContentCreatorFX
      }
      return null;
   }

   public override function registerWindowContentCreatorFX(windowContentCreator: WindowContentCreatorFX, id: String):Void{
      logger.info("registering WindowContentCreatorFX for id {id}, class {windowContentCreator.getClass()}");
      registerContentCreatorFX(windowContentCreator,id);
   }

   public override function registerWindowContentCreator( windowContentCreator: WindowContentCreator,  id: String): Void{
      registerWindowContentCreatorFX(WindowContentCreatorWrapper{windowContentCreator:windowContentCreator },id);
   }

   public override function getWindowContentCreatorFX(id:String):WindowContentCreatorFX{
      var creator = getContentCreatorFX(id);
      if (creator instanceof WindowContentCreatorFX) {
         return creator as WindowContentCreatorFX
      }
      return null;
   }
}
