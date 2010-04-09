/*
 * EloDisplayTypeControlImpl.fx
 *
 * Created on 17-mrt-2010, 11:35:30
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import java.net.URI;
import eu.scy.client.desktop.scydesktop.FunctionalTypes;
import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.scywindows.EloDisplayTypeControl;

/**
 * @author sikken
 */

public class EloDisplayTypeControlImpl extends EloDisplayTypeControl {

   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
   public var extensionManager:IExtensionManager;

   def techniocalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def functionalTypeKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId());
   def iconTypeNameKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ICON_TYPE.getId());

   override public function getEloType (eloUri : URI) : String {
      if (eloUri==null){
         return null;
      }
      var metadata = repository.retrieveMetadata(eloUri);
      if (metadata!=null){
         var iconTypeName = metadata.getMetadataValueContainer(iconTypeNameKey).getValue() as String;
         if (iconTypeName.length()>0){
            return iconTypeName;
         }
         var technicalFormat = metadata.getMetadataValueContainer(techniocalFormatKey).getValue() as String;
         var functionalType = metadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
         return getEloType(technicalFormat,functionalType);
      }
      extensionManager.getType(eloUri);
   }

   function getEloType(technicalFormat:String, functionalType:String):String{
      if (EloImageInformation.url.type==technicalFormat){
         if (FunctionalTypes.ASSIGMENT.equals(functionalType)){
            return EloImageInformation.assignment.type;
         }
      }
      return technicalFormat;
   }
}
