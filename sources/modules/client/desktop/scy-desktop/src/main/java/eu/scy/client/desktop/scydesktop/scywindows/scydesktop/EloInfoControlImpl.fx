/*
 * EloTypeControlImpl.fx
 *
 * Created on 17-mrt-2010, 10:17:38
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.api.IExtensionManager;

/**
 * @author sikken
 */

public class EloInfoControlImpl extends EloInfoControl {

   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
   public var extensionManager:IExtensionManager;

   def techniocalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());

   override public function getEloType (eloUri : URI) : String {
      if (eloUri==null){
         return null;
      }
      var metadata = repository.retrieveMetadata(eloUri);
      if (metadata!=null){
         var technicalFormat = metadata.getMetadataValueContainer(techniocalFormatKey).getValue() as String;
         return technicalFormat;
      }
      extensionManager.getType(eloUri);
   }

   public override function getEloTitle(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      var metadata = repository.retrieveMetadata(eloUri);
      if (metadata==null){
         return null
      }
      var eloTitle = metadata.getMetadataValueContainer(titleKey).getValue() as String;
      return eloTitle;
   }
}
