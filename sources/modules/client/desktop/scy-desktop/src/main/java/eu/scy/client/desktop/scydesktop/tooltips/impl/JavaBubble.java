/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;

/**
 *
 * @author sikken
 */
public interface JavaBubble
{

   public String getId();

   public BubbleLayer getLayerId();

   public Integer getPriority();

   public boolean isVisible();

   public boolean canBeUsedNow();
}
