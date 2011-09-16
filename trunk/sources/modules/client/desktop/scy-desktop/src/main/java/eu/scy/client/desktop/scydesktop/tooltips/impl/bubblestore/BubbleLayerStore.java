/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore;

import eu.scy.client.desktop.scydesktop.tooltips.impl.JavaBubble;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleLayerStore
{

   private final static Logger logger = Logger.getLogger(BubbleLayerStore.class);
   private List<BubbleList> bubbleLists = new ArrayList<BubbleList>();
   private BubbleListComparator bubbleListComparator = new BubbleListComparator();

   private BubbleList getBubblelist(int priority)
   {
      for (BubbleList bubbleList : bubbleLists)
      {
         if (priority == bubbleList.getPriority())
         {
            return bubbleList;
         }
      }
      BubbleList bubbleList = new BubbleList(priority);
      bubbleLists.add(bubbleList);
      Collections.sort(bubbleLists, Collections.reverseOrder(bubbleListComparator));
      return bubbleList;
   }

   public void addBubble(JavaBubble bubble)
   {
      BubbleList bubbleList = getBubblelist(bubble.getPriority());
      bubbleList.addBubble(bubble);
   }

   public void removeBubble(JavaBubble bubble)
   {
      BubbleList bubbleList = getBubblelist(bubble.getPriority());
      if (!bubbleList.removeBubble(bubble))
      {
         logger.warn("trying to delete not stored bubble: " + bubble);
      }
      if (bubbleList.isEmpty())
      {
         bubbleLists.remove(bubbleList);
      }
   }

   public void removeBubbles(String id)
   {
      List<BubbleList> bubbleListsToDelete = new ArrayList<BubbleList>();
      for (BubbleList bubbleList : bubbleLists)
      {
         bubbleList.removeBubbles(id);
         if (bubbleList.isEmpty()){
            bubbleListsToDelete.add(bubbleList);
         }
      }
      for (BubbleList bubbleList: bubbleListsToDelete){
         bubbleLists.remove(bubbleList);
      }
   }

   public JavaBubble getNextBubble()
   {
      if (!bubbleLists.isEmpty())
      {
         for (BubbleList bubbleList : bubbleLists){
            JavaBubble nextBubble = bubbleList.getFirstUsableBubble();
            if (nextBubble!=null){
               return nextBubble;
            }
         }
      }
      return null;
   }
}
