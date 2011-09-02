/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore;

import java.util.Comparator;

/**
 *
 * @author SikkenJ
 */
public class BubbleListComparator implements Comparator<BubbleList> {

   @Override
   public int compare(BubbleList bubbleList1, BubbleList bubbleList2)
   {
      return bubbleList1.getPriority()-bubbleList2.getPriority();
   }

}
