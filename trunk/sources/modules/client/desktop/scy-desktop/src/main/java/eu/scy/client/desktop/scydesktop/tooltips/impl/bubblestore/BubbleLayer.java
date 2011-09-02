/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore;

import eu.scy.client.desktop.scydesktop.tooltips.impl.JavaBubble;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleLayer
{

   private final static Logger logger = Logger.getLogger(BubbleLayer.class);
   private SortedSet<BubbleList> bubbleListSet = new TreeSet<BubbleList>(new BubbleListComparator());

   private BubbleList getBubblelist(int priority)
   {
      for (BubbleList bubbleList : bubbleListSet)
      {
         if (priority == bubbleList.getPriority())
         {
            return bubbleList;
         }
      }
      BubbleList bubbleList = new BubbleList(priority);
      bubbleListSet.add(bubbleList);
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
         bubbleListSet.remove(bubbleList);
      }
   }

   public void removeBubbles(String id)
   {
      List<BubbleList> bubbleListsToDelete = new ArrayList<BubbleList>();
      for (BubbleList bubbleList : bubbleListSet)
      {
         bubbleList.removeBubbles(id);
         if (bubbleList.isEmpty()){
            bubbleListsToDelete.add(bubbleList);
         }
      }
      for (BubbleList bubbleList: bubbleListsToDelete){
         bubbleListSet.remove(bubbleList);
      }
   }

   public JavaBubble getNextBubble()
   {
      if (!bubbleListSet.isEmpty())
      {
         BubbleList bubbleList = bubbleListSet.last();
         return bubbleList.getFirstBubble();
      }
      return null;
   }
}
