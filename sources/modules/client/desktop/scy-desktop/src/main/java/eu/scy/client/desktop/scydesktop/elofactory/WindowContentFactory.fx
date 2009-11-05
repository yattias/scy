/*
 * WindowContentFactory.fx
 *
 * Created on 3-jul-2009, 15:25:10
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import eu.scy.client.desktop.scydesktop.dummy.DummyWindowContentCreator;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.utils.UriUtils;

import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.tools.ScyTool;

import java.lang.IllegalStateException;

/**
 * @author sikkenj
 */

def logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentFactory");

public class WindowContentFactory extends ContentFactory {
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;

   def defaultWindowContentCreator:WindowContentCreatorFX = DummyWindowContentCreator{};

   public function fillWindowContent(eloUri:URI,scyWindow:ScyWindow,id:String){
     return fillWindowContent(eloUri,scyWindow,id,getType(eloUri));
   }

   public function fillWindowContent(scyWindow:ScyWindow,id:String){
      return fillWindowContent(null,scyWindow,id,scyWindow.eloType);
   }

   function fillWindowContent(eloUri:URI,scyWindow:ScyWindow,id:String, type:String){
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(id);
      if (windowContentCreator==null){
         logger.error("couldn't find a WindowContentCreatorFX for id {id}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
      checkIfServicesInjected(windowContentCreator);
      if (not windowContentCreator.supportType(type)){
         throw new IllegalStateException("windowContentCreator id:{id} does not supports content type:{type}");
      }
      var contentNode;
      if (eloUri!=null){
         contentNode = windowContentCreator.getScyWindowContent(eloUri, scyWindow);
      }
      else{
         contentNode = windowContentCreator.getScyWindowContentNew(scyWindow);
      }
       scyWindow.scyContent = contentNode;
      voidInspectContent(scyWindow);
   }

   function getType(eloUri:URI):String{
      if (config.getExtensionManager()!=null){
         return config.getExtensionManager().getType(eloUri);
      }
      return UriUtils.getExtension(eloUri);
   }

   function voidInspectContent(scyWindow:ScyWindow){
      var scyContent = scyWindow.scyContent;
      if (scyContent instanceof ScyTool){
         scyWindow.scyTool = scyWindow.scyContent as ScyTool;
      }
      checkIfServicesInjected(scyContent);
   }
}
