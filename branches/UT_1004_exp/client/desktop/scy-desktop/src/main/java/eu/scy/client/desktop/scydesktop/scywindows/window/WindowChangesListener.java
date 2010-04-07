/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

/**
 * This interface provides the ability to receive notifications on changes in ScyWindow.
 * Classes implementing this interface need to be added to ScyWindow as ScyWindowChangeListeners
 * {@link eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow#addChangesListener}.
 *
 *
 * @author pg
 */
public interface WindowChangesListener {

    public void resizeStarted();
    public void resizeFinished();

    public void draggingStarted();
    public void draggingFinished();

    //currenty no support for rotations - feel free to add them ;o)
    //public void rotationStarted();
    //public void rotationFinished();
    

}
