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

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentFactory");

public class WindowContentFactory {
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;

   def defaultWindowContentCreator:WindowContentCreatorFX = DummyWindowContentCreator{};

   public function fillWindowContent(eloUri:URI,scyWindow:ScyWindow){
      var extention = UriUtils.getExtention(eloUri);
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(extention);
      if (windowContentCreator==null){
         logger.warn("couldn't find a WindowContentCreatorFX for extention {extention}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
      var contentNode = windowContentCreator.getScyWindowContent(eloUri, scyWindow);
      scyWindow.scyContent = contentNode;
   }

}
