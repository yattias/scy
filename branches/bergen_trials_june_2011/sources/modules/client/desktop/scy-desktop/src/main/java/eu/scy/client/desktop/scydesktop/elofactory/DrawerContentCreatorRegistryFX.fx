/*
 * DrawerContentCreatorRegistryFX.fx
 *
 * Created on 22-sep-2009, 17:17:34
 */

package eu.scy.client.desktop.scydesktop.elofactory;

/**
 * @author sikkenj
 */

// place your code here

public mixin class DrawerContentCreatorRegistryFX extends DrawerContentCreatorRegistry{

   public abstract function registerDrawerContentCreatorFX(drawerContentCreator: DrawerContentCreatorFX, id: String):Void;

   public abstract function getDrawerContentCreatorFX(id:String):DrawerContentCreatorFX;

}
