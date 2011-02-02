/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.draganddrop;

/**
 * Interface for showing a visual feedback in the node, when the mouse is above the node during a drop in progress.
 *
 * The DragAndDropManager will handle the visual feedback of the dragged node.
 *
 * @author SikkenJ
 */
public interface DropTarget2 extends DropTarget {

   /**
    * this method is called when the mouse enters the drop target.
    *
    * @param object - the drop object
    * @param canAccept - if the target can accepts the drop (result of canAcceptDrop() call)
    */
   public void dropEntered(Object object, boolean canAccept);

   /**
    * this method is called when the mouse leaves the drop target.
    *
    * @param object - the drop object
    */
   public void dropLeft(Object object);

}
