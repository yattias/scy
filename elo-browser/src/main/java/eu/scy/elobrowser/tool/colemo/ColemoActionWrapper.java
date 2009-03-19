/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.colemo;

import eu.scy.colemo.client.*;

import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;

/**
 *
 * @author Henrik
 */
public class ColemoActionWrapper {

    private ColemoPanel colemoPanel;
    private ActionController actionController;

    ColemoActionWrapper(ColemoPanel colemoPanel) {
        this.colemoPanel = colemoPanel;
    }

    public void connect(){
        System.out.println("Connecting!");
    }

    public void cleanUp() {
        colemoPanel.cleanUp();
    }

    public void createNewConcept() {
        colemoPanel.addNewConcept(null, "c");
    }

   
}
