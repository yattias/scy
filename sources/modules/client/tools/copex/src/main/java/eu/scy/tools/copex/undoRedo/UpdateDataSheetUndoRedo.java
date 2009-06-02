/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;

import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MyConstants;


/**
 * undo redo : modification d'un datasheet (nombre de colonnes et de lignes)
 * @author MBO
 */
public class UpdateDataSheetUndoRedo extends CopexUndoRedo {
    // ATTRIBUTS
    /* dataSheet */
    private DataSheet dataSheet ;
    /* ancien nombre de lignes */
    private int oldNbRows;
    /* ancien nombre de colonnes */
    private int oldNbCols;
    /* nouveau nombre de lignes */
    private int newNbRows;
    /* nouveau nombre de colonnes */
    private int newNbCols;

    // CONSTRUCTEURS
    public UpdateDataSheetUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, DataSheet dataSheet, int oldNbRows, int oldNbCols, int newNbRows, int newNbCols) {
        super(edP, controller, tree);
        this.dataSheet = dataSheet;
        this.oldNbRows = oldNbRows;
        this.oldNbCols = oldNbCols;
        this.newNbRows = newNbRows;
        this.newNbCols = newNbCols;
    }
    
    // METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        CopexReturn cr = this.controller.modifyDataSheet(getProc(), oldNbRows, oldNbCols, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        CopexReturn cr = this.controller.modifyDataSheet(getProc(), newNbRows, newNbCols, MyConstants.REDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    
    
}
