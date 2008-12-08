/*
 * Roolo.fx
 *
 * Created on 4-sep-2008, 15:39:28
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.model.mapping.MappingEloFactory;
import java.io.FileNotFoundException;
import java.lang.*;
import java.security.AccessControlException;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import roolo.api.*;
import roolo.api.search.*;

/**
 * @author sikkenj
 */

public class Roolo {
   public var configFileLocation:String = "rooloConfig.xml";
   public var configClasspathLocation:String = "config/rooloConfig.xml";
   public var repository:IRepository;
   public var metadataTypeManager :IMetadataTypeManager;
   public var extensionManager: IExtensionManager;
   
   public var idKey:IMetadataKey;
   public var titleKey:IMetadataKey;
   public var descriptionKey:IMetadataKey;
   public var typeKey:IMetadataKey;
   public var sizeKey:IMetadataKey;
   
   public var mappingEloFactory:MappingEloFactory;
   
   // private attribute keys:IMetadataKey[];
   
   var springContext:ApplicationContext;
   var searchOperationNames:BasicSearchOperationNames;
   
   //  // def roolo:Roolo;

   // this should be a static function
   //   static public function getRoolo():Roolo
   //   {
   //      if (roolo==null)
   //      {
   //	 roolo = Roolo{};
   //	 roolo.initialize();
   //      }
   //      return roolo;
   //   }
   
   public function getKeys():IMetadataKey[] {
      var metadataKeys:IMetadataKey[];
      var iterator = metadataTypeManager.getMetadataKeys().iterator();
       while (
      iterator.hasNext()) {
	 insert
         iterator.next() as IMetadataKey into metadataKeys;
      }
      return metadataKeys;
   }
   
   public function getSearchOperations():String[] {
      var searchOperations:String[];
       
      var iterator = searchOperationNames.getNames().iterator();
       while (
      iterator.hasNext()) {
	 insert
         iterator.next() as String into searchOperations;
      }
      return searchOperations;
   }
   
   function initialize() {
      findRooloParts();
      findKeys();
      findEloBrowserParts();
   }
   
   //   function createRooloParts()
   //   {
   //      repository = new MockRepository();
   //      metadataTypeManager = new MockMetadataTypeManager();
   //      extensionManager = new MockExtensionManager();
   //   }
   
   function findRooloParts()
   {
      springContext = null;
      try {
         springContext = new FileSystemXmlApplicationContext(configFileLocation);
      } catch (e1:AccessControlException) {
         System.out.println("Could not access file {configFileLocation}, trying on class path");
      } catch (e2:BeanDefinitionStoreException) {
         if (not(
         e2.getRootCause() instanceof FileNotFoundException))
         {
	       throw e2;
         }
         System.out.println("Could not find file {configFileLocation}, trying on class path");
      }
      if (springContext == null)
      {
         springContext = new ClassPathXmlApplicationContext(configClasspathLocation);
      }
      if (springContext == null)
      {
	    throw
         new IllegalStateException("failed to find spring context");
      }
      repository =
      getSpringBean("repository") as IRepository;
      metadataTypeManager =
      getSpringBean("metadataTypeManager") as IMetadataTypeManager;
      extensionManager =
      getSpringBean("extensionManager") as IExtensionManager;
      searchOperationNames =
      getSpringBean("searchOperations") as BasicSearchOperationNames;
   }
   
   function getSpringBean(name:String):Object {
      try {
         return springContext.getBean(name);
      } catch (e:NoSuchBeanDefinitionException) {
         throw
         new IllegalStateException("failed to find bean {name}");
      }
   }
   
   function findKeys() {
      idKey = metadataTypeManager.getMetadataKey("uri");
      titleKey = metadataTypeManager.getMetadataKey("title");
      descriptionKey = metadataTypeManager.getMetadataKey("description");
      typeKey = metadataTypeManager.getMetadataKey("type");
      sizeKey = metadataTypeManager.getMetadataKey("size");
   }
   
   function findEloBrowserParts() {
      mappingEloFactory =
      getSpringBean("mappingEloFactory") as MappingEloFactory;
   }
   
   function createKeys() {
//      var count1:MetadataValueCount;
//      var count2:MetadataValueCount = MetadataValueCount.valueOf("SINGLE");
//      idKey = new UriMetadataKey(null as String,null as String,null as I18nType,null as MetadataValueCount,null as Validator);
//      idKey = new UriMetadataKey("id","/lom/general/identifier",null as I18nType,MetadataValueCount.SINGLE as MetadataValueCount,null as Validator);
      
//      idKey = new UriMetadataKey("id","/lom/general/identifier",I18nType.UNIVERSAL,MetadataValueCount.SINGLE,null as Validator);
//      insert idKey into keys;
//      titleKey = new StringMetadataKey("title","/lom/general/title",I18nType.SPECIFIC,MetadataValueCount.SINGLE,null);
//      insert titleKey into keys;
//      typeKey = new StringMetadataKey("type","/lom/technical/format",I18nType.UNIVERSAL,MetadataValueCount.SINGLE,null);
//      insert typeKey into keys;
//      sizeKey = new LongMetadataKey("size","/lom/technical/size",I18nType.UNIVERSAL,MetadataValueCount.SINGLE,null);
//      insert sizeKey into keys;
//
//      for (key in keys)
//	 metadataTypeManager.registerMetadataKey(key);

   }
   
}

var roolo:Roolo;;

public function getRoolo():Roolo {
   if (roolo == null)
   {
      try {
			DOMConfigurator.configure (new ClassPathResource ("config/elobrowser.log4j.xml").getURL ());
		} catch (e) {
			e.printStackTrace();
		}
      roolo = Roolo{};
      roolo.initialize();
   }
   return roolo;
}
