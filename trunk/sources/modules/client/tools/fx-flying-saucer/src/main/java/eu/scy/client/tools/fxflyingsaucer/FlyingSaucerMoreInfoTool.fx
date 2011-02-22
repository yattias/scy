/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import java.net.URL;
import javafx.scene.layout.Container;
import javafx.scene.layout.Resizable;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;

/**
 * @author SikkenJ
 */
public class FlyingSaucerMoreInfoTool extends CustomNode, Resizable, ShowInfoUrl {

   public override var width on replace { resizeContent() };
   public override var height on replace { resizeContent() };
   public var showMoreInfo: ShowMoreInfo;
   def flyingSaucerPanel = new FlyingSaucerPanel(false);
   def swingComponent = ScySwingWrapper.wrap(flyingSaucerPanel);

   public override function create(): Node {
      flyingSaucerPanel.setShowMoreInfo(showMoreInfo);
      swingComponent
   }

   override public function showInfoUrl(url: URL): Void {
      if (url != null) {
         flyingSaucerPanel.clearHistory();
         flyingSaucerPanel.loadUrl(url.toString());
      }
   }

   function resizeContent(): Void {
      Container.resizeNode(swingComponent, width, height);
   }

   public override function getPrefHeight(number: Number): Number {
      Math.max(0, flyingSaucerPanel.getPreferredSize().height);
   }

   public override function getPrefWidth(number: Number): Number {
      Math.max(0, flyingSaucerPanel.getPreferredSize().width);
   }

}
