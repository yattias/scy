/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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

   public override function getBubbleContent(): Node {
      Text {
         font: Font {
            size: 14
         }
         x: 0, y: 0
         content: bubbleText
      }
   }

}
