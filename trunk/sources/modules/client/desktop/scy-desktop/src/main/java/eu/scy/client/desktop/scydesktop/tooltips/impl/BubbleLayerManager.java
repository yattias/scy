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
         layerStack.add(layerId);
      }
      else
      {
         logger.warn("duplicate showLayer: " + layerId);
      }
   }

   public void hideLayer(BubbleLayer layerId)
   {
      if (layerId.equals(getTopLayer()))
      {
         layerStack.remove(layerStack.size() - 1);
      }
      else
      {
         logger.warn("hideLayer (" + layerId + "), but layer is not on top, top layer: " + getTopLayer());
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
}
