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
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * undo redo : creation d'une feuille de données
 * @author MBO
 */
public class CreateDataSheetUndoRedo extends CopexUndoRedo {
    // ATTRIBUTS
    /* nombre de lignes */
    private int nbRows;
    /* nombre de colonnes */
    private int nbCols;
    /* Datasheet */
    private DataSheet dataSheet;

    // CONSTRUCTEURS
    public CreateDataSheetUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, int nbRows, int nbCols, DataSheet dataSheet) {
        super(edP, controller, tree);
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.dataSheet = dataSheet;
    }

   

    // METHODES
    
    /* undo*/
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        CopexReturn cr = controller.deleteDataSheet(getProc(), dataSheet.getDbKey(), MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }
   
    
    /* redo*/
     @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = controller.createDataSheet(getProc(), nbRows, nbCols, MyConstants.REDO, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // mise à jour de l'id
        long newDbKey = ((DataSheet)v.get(0)).getDbKey();
        dataSheet.setDbKey(newDbKey);
    }
    
    
    
}
