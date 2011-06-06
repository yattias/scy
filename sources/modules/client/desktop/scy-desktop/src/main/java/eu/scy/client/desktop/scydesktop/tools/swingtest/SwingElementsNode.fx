/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.swingtest;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;

/**
 * @author SikkenJ
 */
public class SwingElementsNode extends CustomNode {

   def swingElements = new SwingElements();
   def wrappedSwingElements = ScySwingWrapper.wrap(swingElements);

   public override function create(): Node {
      wrappedSwingElements
   }

}
