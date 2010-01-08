/*
 * DrawerContentFactory.fx
 *
 * Created on 23-sep-2009, 14:55:11
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

/**
 * @author sikkenj
 */

// place your code here

public class DrawerContentFactory extends ContentFactory{
   def logger = Logger.getLogger(this.getClass());

   public var drawerContentCreatorRegistryFX:DrawerContentCreatorRegistryFX;

   public function createDrawerTool(id:String,window:ScyWindow):Node{
      var drawerTool:Node = null;
      if (id!=null){
         var drawerContentCreator = drawerContentCreatorRegistryFX.getDrawerContentCreatorFX(id);
         if (drawerContentCreator!=null){
            checkIfServicesInjected(drawerContentCreator);
            drawerTool = drawerContentCreator.getDrawerContent(window.eloUri, window);
            checkIfServicesInjected(drawerTool);
         }
         else{
            logger.error("failed to find drawer content creator with id '{id}'");
         }
      }
      return drawerTool;
   }

}
