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

import javafx.scene.layout.Resizable;

import java.awt.Dimension;

/**
 * @author sikken
 */

// place your code here
var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.SwingContentWrapper");

public class SwingContentWrapper extends CustomNode, Resizable {
   public var swingContent: JComponent;
   public var config:Config on replace {injectServices()}
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var swingComponent:SwingComponent;

   public override function create(): Node {
      injectServices();
      swingComponent = SwingComponent.wrap(swingContent);
      return swingComponent;
   }

   function injectServices(){
      var servicesInjector = ServicesInjector{
         config:config;
      }
      servicesInjector.injectServices(swingContent);
   }

   function resizeContent(){
      swingComponent.width = width;
      swingComponent.height = height;
      // changing the size of wrapped component, does not result in a size change of the swing component
      // although it is visual not needed
      // set the size of the swing component, so it can adjust its content
      var newSize = new Dimension(width,height);
      swingContent.setSize(newSize);
   }

   public override function getPrefHeight(width: Number) : Number{
      return swingContent.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return swingContent.getPreferredSize().getWidth();
   }

}

