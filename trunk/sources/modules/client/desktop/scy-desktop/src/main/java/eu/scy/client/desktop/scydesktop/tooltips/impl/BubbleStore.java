/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

/**
 *
 * @author sikken
 */
public interface BubbleStore
{

   public void addBubble(JavaBubble bubble);

   public void removeBubble(JavaBubble bubble);

   public void removeBubbles(String id);

   public JavaBubble getNextBubble(Object layerId);
}
