/*
 * Roolo.fx
 *
 * Created on 4-sep-2008, 15:39:28
 */

package eu.scy.elobrowser.main;

import eu.scy.client.tools.drawing.MyPropertyPlaceholderConfigurer;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.model.mapping.MappingEloFactory;
import eu.scy.elobrowser.model.mapping.QueryToElosDisplay;
import eu.scy.elobrowser.tbi_hack.AddGeneralMetadataRepositoryWrapper;
import eu.scy.elobrowser.tool.pictureviewer.PictureImporter;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.System;
import java.net.URL;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.cms.repository.search.BasicSearchOperationNames;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;





/**
 * @author sikkenj
 */

public class Roolo {
   public var configFileLocation:String = "rooloConfig.xml";
   public var configClasspathLocation:String = "/config/rooloConfig.xml";
   public var repository:IRepository;
   public var metadataTypeManager :IMetadataTypeManager;
   public var extensionManager: IExtensionManager;
   public var eloFactory: IELOFactory;
   public var metadataAddingRepository:AddGeneralMetadataRepositoryWrapper;
   
   public var idKey:IMetadataKey;
   public var titleKey:IMetadataKey;
   public var descriptionKey:IMetadataKey;
   public var typeKey:IMetadataKey;
   public var sizeKey:IMetadataKey;
   
   public var mappingEloFactory:MappingEloFactory;
   public var queryToElosDisplay:QueryToElosDisplay;

   public var pictureImporter:PictureImporter;
   public var meloImporter:PictureImporter;
   
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
   
   function findRooloParts() {
      setupServerAddress();
      springContext = null;
		//      try {
		//         springContext = new FileSystemXmlApplicationContext(configFileLocation);
		//      } catch (e1:AccessControlException) {
		//         System.out.println("Could not access file {configFileLocation}, trying on class path");
		//      } catch (e2:BeanDefinitionStoreException) {
		//         if (not(
		//         e2.getRootCause() instanceof FileNotFoundException))
		//         {
		//	       throw e2;
		//         }
		//         System.out.println("Could not find file {configFileLocation}, trying on class path");
		//      }
      if (springContext == null)
      {
         System.out.println("trying to load spring config from class path {configClasspathLocation}");
         springContext = new ClassPathXmlApplicationContext(configClasspathLocation);
      }
      if (springContext == null)
      {
	    throw
         new IllegalStateException("failed to find spring context");
      }
      repository =
      getSpringBean("repository") as IRepository;
      metadataAddingRepository =
      getSpringBean("metadataAddingRepository") as AddGeneralMetadataRepositoryWrapper;
      metadataTypeManager =
      getSpringBean("metadataTypeManager") as IMetadataTypeManager;
      extensionManager =
      getSpringBean("extensionManager") as IExtensionManager;
      eloFactory =
      getSpringBean("eloFactory") as IELOFactory;
      searchOperationNames =
      getSpringBean("searchOperations") as BasicSearchOperationNames;
   }

   function setupServerAddress(){
      try{
         var basicService =
			ServiceManager.lookup("BasicService") as BasicService;
         if (basicService != null){
            var codeBase = basicService.getCodeBase();
            var properties = MyPropertyPlaceholderConfigurer.properties;
            properties.put("serverName", codeBase.getHost());
            properties.put("httpPort", "{codeBase.getPort()}");
            properties.put("contextPath", getContextPath(codeBase));
         }
      } catch (exception){
          System.out.println("Failed to get info about jnlp codeBase, {exception}");
      }
   }

   function getContextPath(codeBase:URL) : String{
      var path = codeBase.getPath();
		var contextPath = path;
		var startSearchPos = 0;
		if (path.length() > 0 and path.charAt(0) == '/'){
         startSearchPos = 1;
      }
		var slashPos = path.indexOf('/',startSearchPos);
		if (slashPos >= 0){
         contextPath = path.substring(0, slashPos);
      }
		return contextPath;
   }
   
   public function getSpringBean(name:String):Object {
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
      queryToElosDisplay =
      getSpringBean("queryToElosDisplay") as QueryToElosDisplay;
      pictureImporter =
      getSpringBean("pictureImporter") as PictureImporter;
      meloImporter =
      getSpringBean("meloImporter") as PictureImporter;
   }
   
   
}

var roolo:Roolo;;

public function getRoolo():Roolo {
	if (roolo == null)
	{
		try {
            DOMConfigurator.configure (
			new ClassPathResource ("config/elobrowser.log4j.xml").getURL ());
		} catch (e) {
         System.out.println("Problems with loading log4j config");
			e.printStackTrace();
		}
		roolo = Roolo{};
      roolo.initialize();
	}
	return roolo;
}
