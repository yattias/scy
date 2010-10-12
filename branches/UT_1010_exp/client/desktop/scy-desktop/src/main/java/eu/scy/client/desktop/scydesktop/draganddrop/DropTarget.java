/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.draganddrop;

/**
 * Interface which nodes have to implement, in order to recieve drops. The node must be registered at the DragAndDropManager before it can recieve drops.
 *
 * @author SikkenJ
 */
public interface DropTarget {
   /**
    * this method is called when an object is dragged above the window, return true if the object can be accepted
    */
   public boolean canAcceptDrop(Object object);

   /**
    * this method is called when an object is dropped on the window. This method will only be called after canAcceptDrop has returned true
    */
   public void acceptDrop(Object object);

}
