/*
 * RooloEloInfoControl.fx
 *
 * Created on 6-jul-2009, 13:57:50
 */

package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import java.net.URI;

import roolo.api.IExtensionManager;

import roolo.api.IRepository;

import roolo.elo.api.IMetadataKey;

/**
 * @author sikkenj
 */

public class RooloEloInfoControl extends EloInfoControl {

   public var extensionManager:IExtensionManager;
   public var repository:IRepository;
   public var titleKey: IMetadataKey;

   public override function getEloType(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      return extensionManager.getType(eloUri);
   }

   public override function getEloTitle(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      var metadata = repository.retrieveMetadata(eloUri);
      if (metadata==null){
         return null
      }
      return metadata.getMetadataValueContainer(titleKey).getValue() as String;
   }
}
