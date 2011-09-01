/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author sikken
 */
public class BubbleStore
{

   private final static Logger logger = Logger.getLogger(BubbleStore.class);

   private class BubbleLayer
   {

      private SortedSet<List<JavaBubble>> bubbleListSet = new TreeSet<List<JavaBubble>>();

      private List<JavaBubble> getBubblelist(int priority)
      {
         for (List<JavaBubble> bubbleList : bubbleListSet)
         {
            if (priority == bubbleList.get(0).getPriority())
            {
               return bubbleList;
            }
         }
         List<JavaBubble> bubbleList = new ArrayList<JavaBubble>();
         return bubbleList;
      }

      public void addBubble(JavaBubble bubble)
      {
         List<JavaBubble> bubbleList = getBubblelist(bubble.getPriority());
         bubbleList.add(bubble);
      }

      public void removeBubble(JavaBubble bubble)
      {
         List<JavaBubble> bubbleList = getBubblelist(bubble.getPriority());
         if (!bubbleList.remove(bubble))
         {
            logger.warn("trying to delete not stored bubble: " + bubble);
         }
         if (bubbleList.isEmpty())
         {
            bubbleListSet.remove(bubbleList);
         }
      }

      public JavaBubble getNextBubble()
      {
         List<JavaBubble> bubbleList = bubbleListSet.first();
         if (bubbleList != null && !bubbleList.isEmpty())
         {
            return bubbleList.get(0);
         }
         return null;
      }
   }
   
   private Map<Object, BubbleLayer> bubbleLayerMap = new HashMap<Object, BubbleLayer>();

   private BubbleLayer getBubbleLayer(Object layerId)
   {
      BubbleLayer bubbleLayer = bubbleLayerMap.get(layerId);
      if (bubbleLayer == null)
      {
         bubbleLayer = new BubbleLayer();
         bubbleLayerMap.put(layerId, bubbleLayer);
      }
      return bubbleLayer;
   }

   public void addBubble(JavaBubble bubble)
   {
      BubbleLayer bubbleLayer = getBubbleLayer(bubble.getLayerId());
      bubbleLayer.addBubble(bubble);
   }

   public void removeBubble(JavaBubble bubble)
   {
      BubbleLayer bubbleLayer = getBubbleLayer(bubble.getLayerId());
      bubbleLayer.removeBubble(bubble);
   }

   public void removeBubbles(String id)
   {
   }

   public JavaBubble getNextBubble(Object layerId)
   {
      BubbleLayer bubbleLayer = getBubbleLayer(layerId);
      return bubbleLayer.getNextBubble();
   }
}
