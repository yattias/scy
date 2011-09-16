/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore;

import eu.scy.client.desktop.scydesktop.tooltips.impl.JavaBubble;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleStoreImpl {
   private final static Logger logger = Logger.getLogger(BubbleStoreImpl.class);

   private Map<Object, BubbleLayerStore> bubbleLayerMap = new HashMap<Object, BubbleLayerStore>();

   private BubbleLayerStore getBubbleLayer(Object layerId)
   {
      BubbleLayerStore bubbleLayer = bubbleLayerMap.get(layerId);
      if (bubbleLayer == null)
      {
         bubbleLayer = new BubbleLayerStore();
         bubbleLayerMap.put(layerId, bubbleLayer);
      }
      return bubbleLayer;
   }

   public void addBubble(JavaBubble bubble)
   {
      BubbleLayerStore bubbleLayer = getBubbleLayer(bubble.getLayerId());
      bubbleLayer.addBubble(bubble);
   }

   public void removeBubble(JavaBubble bubble)
   {
      BubbleLayerStore bubbleLayer = getBubbleLayer(bubble.getLayerId());
      bubbleLayer.removeBubble(bubble);
   }

   public void removeBubbles(String id)
   {
      for (BubbleLayerStore bubbleLayer: bubbleLayerMap.values()){
         bubbleLayer.removeBubbles(id);
      }
   }

   public JavaBubble getNextBubble(BubbleLayer layerId)
   {
      BubbleLayerStore bubbleLayer = getBubbleLayer(layerId);
      return bubbleLayer.getNextBubble();
   }

}
