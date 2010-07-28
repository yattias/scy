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
import java.util.Locale;
import java.util.Map;

/**
 * @author sikken
 */

public class EloInfoControlImpl extends EloInfoControl {

   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
   public var extensionManager:IExtensionManager;

   def techniocalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());

   def defaultLanguageLocale = getLanguageLocale(Locale.getDefault());
   def englishLanguageLocale = getLanguageLocale(Locale.ENGLISH);

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
      var titles = metadata.getMetadataValueContainer(titleKey).getValuesI18n() as Map;
      var eloTitle = findTitle(titles);
      return eloTitle;
   }

   function findTitle(valuesI18n: Map):String{
      var title = valuesI18n.get(defaultLanguageLocale) as String;
      if (title!=null){
         return title;
      }
      title = valuesI18n.get(englishLanguageLocale) as String;
      if (title!=null){
         return title;
      }
      if (not valuesI18n.isEmpty()){
         var values = valuesI18n.values();
         var iterator = values.iterator();
         return iterator.next() as String;

      }
      return null;
   }

   function getLanguageLocale(locale: Locale):Locale
	{
		return new Locale(locale.getLanguage());
	}

}
