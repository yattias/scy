/*
 * SwingWindowContentWrapper.fx
 *
 * Created on 6-jul-2009, 11:01:54
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.CustomNode;
import javafx.scene.Node;

import javax.swing.JComponent;

import javafx.ext.swing.SwingComponent;

import org.apache.log4j.Logger;


import eu.scy.client.desktop.scydesktop.config.Config;

/**
 * @author sikken
 */

// place your code here
var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.SwingContentWrapper");

public class SwingContentWrapper extends CustomNode {
   public var swingContent: JComponent;
   public var config:Config on replace {injectServices()}

   public override function create(): Node {
      return SwingComponent.wrap(swingContent);
   }

   function injectServices(){
      var servicesInjector = ServicesInjector{
         config:config;
      }
      servicesInjector.injectServices(swingContent);
   }
}

