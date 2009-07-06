/*
 * WindowContentCreatorRegistryFX.fx
 *
 * Created on 3-jul-2009, 12:28:40
 */

package eu.scy.client.desktop.scydesktop.elofactory;

/**
 * @author sikkenj
 */

public mixin class WindowContentCreatorRegistryFX extends WindowContentCreatorRegistry {

   public abstract function registerWindowContentCreatorFX(windowContentCreator: WindowContentCreatorFX, type: String):Void;

   public abstract function getWindowContentCreatorFX(type:String):WindowContentCreatorFX;
}
