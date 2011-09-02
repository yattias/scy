/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore;

import eu.scy.client.desktop.scydesktop.tooltips.impl.JavaBubble;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleStoreImpl {
   private final static Logger logger = Logger.getLogger(BubbleStoreImpl.class);

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
      for (BubbleLayer bubbleLayer: bubbleLayerMap.values()){
         bubbleLayer.removeBubbles(id);
      }
   }

   public JavaBubble getNextBubble(Object layerId)
   {
      BubbleLayer bubbleLayer = getBubbleLayer(layerId);
      return bubbleLayer.getNextBubble();
   }

}
