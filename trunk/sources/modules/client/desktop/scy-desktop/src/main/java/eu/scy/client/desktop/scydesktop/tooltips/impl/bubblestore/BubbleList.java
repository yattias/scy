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
public class BubbleList
{

   private final static Logger logger = Logger.getLogger(BubbleList.class);
   private final int priority;
   private final List<JavaBubble> bubbleList = new ArrayList<JavaBubble>();

   public BubbleList(int priority)
   {
      this.priority = priority;
   }

   public int getPriority()
   {
      return priority;
   }

   public void addBubble(JavaBubble bubble)
   {
      bubbleList.add(bubble);
   }

   public boolean removeBubble(JavaBubble bubble)
   {
      return bubbleList.remove(bubble);
   }

   public void removeBubbles(String id)
   {
      List<Integer> bubblesToDelete = new ArrayList<Integer>();
      int i = 0;
      for (JavaBubble bubble : bubbleList)
      {
         if (bubble.getId().equals(id))
         {
            bubblesToDelete.add(i);
         }
         ++i;
      }
      Collections.reverse(bubblesToDelete);
      for (int index : bubblesToDelete)
      {
         bubbleList.remove(index);
      }
   }

   public boolean isEmpty()
   {
      return bubbleList.isEmpty();
   }

   public JavaBubble getFirstBubble()
   {
      return bubbleList.get(0);
   }
}
