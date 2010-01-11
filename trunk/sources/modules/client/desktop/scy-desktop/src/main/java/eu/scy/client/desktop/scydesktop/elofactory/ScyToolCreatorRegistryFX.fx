/*
 * ScyToolCreatorRegistryFX.fx
 *
 * Created on 11-jan-2010, 12:17:07
 */

package eu.scy.client.desktop.scydesktop.elofactory;

/**
 * @author sikken
 */

public mixin class ScyToolCreatorRegistryFX extends ScyToolCreatorRegistry {

   public abstract function registerScyToolCreatorFX(scyToolCreatorFX: ScyToolCreatorFX, id: String):Void;

   public abstract function getScyToolCreatorFX(id:String):ScyToolCreatorFX;

}
