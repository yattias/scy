/*
 * SwingWindowContentWrapper.fx
 *
 * Created on 6-jul-2009, 11:01:54
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.CustomNode;
import javafx.scene.Node;

import javax.swing.JComponent;

import javafx.ext.swing.SwingComponent;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.config.Config;

import javafx.scene.layout.Resizable;

import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;

/**
 * @author sikken
 */

// place your code here

public class SwingContentWrapper extends CustomNode, Resizable, ScyToolGetter {
   def logger = Logger.getLogger(this.getClass());

   public var swingContent: JComponent;
   public var config:Config on replace {injectServices()}
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var swingComponent:SwingComponent;

   public override function create(): Node {
      injectServices();
      swingComponent = SwingComponent.wrap(swingContent);
//      resizeContent();
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
      swingContent.setPreferredSize(newSize);
   }

   public override function getMaxHeight() : Number{
      return swingContent.getMaximumSize().getHeight();
   }

   public override function getMaxWidth() : Number{
      return swingContent.getMaximumSize().getWidth();
   }

   public override function getMinHeight() : Number{
      return swingContent.getMinimumSize().getHeight();
   }

   public override function getMinWidth() : Number{
      return swingContent.getMinimumSize().getWidth();
   }

   public override function getPrefHeight(width: Number) : Number{
      return swingContent.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return swingContent.getPreferredSize().getWidth();
   }

   public override function getScyTool():ScyTool{
//      logger.debug("checking swingContent {swingContent.getClass()}");
      if (swingContent instanceof ScyTool){
         return swingContent as ScyTool;
      }
      else if (swingContent instanceof ScyToolGetter){
         return (swingContent as ScyToolGetter).getScyTool();
      }
      return null;
   }

}

