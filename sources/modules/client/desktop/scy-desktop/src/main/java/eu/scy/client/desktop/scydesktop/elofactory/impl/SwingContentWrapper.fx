/*
 * SwingWindowContentWrapper.fx
 *
 * Created on 6-jul-2009, 11:01:54
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.CustomNode;
import javafx.scene.Node;

import javax.swing.JComponent;


import eu.scy.client.desktop.desktoputils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.config.Config;

import javafx.scene.layout.Resizable;

import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;

/**
 * @author sikken
 */

// place your code here

public class SwingContentWrapper extends CustomNode, Resizable, ScyToolGetter, NeedsServiceInjection {
   def logger = Logger.getLogger(this.getClass());

   public var swingContent: JComponent;
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var swingComponent:Node;

   public override function create(): Node {
      swingComponent = ScySwingWrapper.wrap(swingContent);
//      resizeContent();
      return swingComponent;
   }

   override public function injectServices(servicesInjector: ServicesInjector): Void{
      servicesInjector.injectServices(swingContent);
   }

   function resizeContent(){
      Container.resizeNode(swingComponent, width, height);
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

