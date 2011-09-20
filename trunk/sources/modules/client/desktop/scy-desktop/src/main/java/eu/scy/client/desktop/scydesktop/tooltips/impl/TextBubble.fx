/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;

/**
 * @author sikken
 */
public class TextBubble extends AbstractBubble {

   public-init var bubbleText: String;

   public override function getBubbleNode(): Node {
      TextTooltip {
         content: bubbleText
         arcSize: 15.0
         windowColorScheme: windowColorScheme
      }
   }

}
