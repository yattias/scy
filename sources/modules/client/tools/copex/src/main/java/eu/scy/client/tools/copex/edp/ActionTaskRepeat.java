/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;


/**
 * action task repeat
 * @author Marjolaine
 */
public interface ActionTaskRepeat {
    /* panel resize */
    public void actionResize();
    /* modification of nb repeat */
    public void actionUpdateNbRepeat(int nbRepeat);
    /* parameters selection */
    public void actionSetSelected(Object oldSel, Object newSel);

}
