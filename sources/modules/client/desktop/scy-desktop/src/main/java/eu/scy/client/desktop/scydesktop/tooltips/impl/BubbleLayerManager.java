/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleLayerManager
{

   private final static Logger logger = Logger.getLogger(BubbleLayerManager.class);
   private List<BubbleLayer> layerStack = new ArrayList<BubbleLayer>();

   public void showLayer(BubbleLayer layerId)
   {
      if (!layerId.equals(getTopLayer()))
      {
         if (!layerStack.contains(layerId))
         {
            layerStack.add(layerId);
         }
         else
         {
            logger.warn("showLayer (" + layerId + "), layer is already present, but not on top: " + layerStack);
         }
      }
      else
      {
         logger.warn("showLayer (" + layerId + "), layer is allready on top: " + layerStack);
      }
   }

   public void hideLayer(BubbleLayer layerId)
   {
      if (layerId.equals(getTopLayer()))
      {
         layerStack.remove(layerStack.size() - 1);
      }
      else if (layerStack.contains(layerId))
      {
         layerStack.remove(layerId);
         logger.warn("hideLayer (" + layerId + "), but layer is not on top: " + layerStack);
      }
      else
      {
         logger.warn("hideLayer (" + layerId + "), but layer is not visible, " + layerStack);
      }
   }

   public BubbleLayer getTopLayer()
   {
      if (layerStack.isEmpty())
      {
         return null;
      }
      return layerStack.get(layerStack.size() - 1);
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      for (BubbleLayer layer : layerStack)
      {
         builder.append(layer);
         builder.append(",");
      }
      return "BubbleLayerManager{" + "layerStack=[" + builder.toString() + "]}";
   }
}
