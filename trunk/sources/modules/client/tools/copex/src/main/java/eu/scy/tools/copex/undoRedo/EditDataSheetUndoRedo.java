/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;

import eu.scy.tools.copex.common.CopexData;
import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo : edition d'une case de la feuille de données
 * @author MBO
 */
public class EditDataSheetUndoRedo extends CopexUndoRedo {
    // ATTRIBUTS
    /* dataSheet */
    private DataSheet dataSheet ;
    /* ancienne valeur */
    private String oldData;
    /* nouvelle valeur */
    private String newData;
    /* numero de ligne */
    private int noRow;
    /* numero de colonnne */
    private int noCol;

     // CONSTRUCTEURS
    public EditDataSheetUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, DataSheet dataSheet, String oldData, String newData, int noRow, int noCol) {
        super(edP, controller, tree);
        this.dataSheet = dataSheet;
        this.oldData = oldData;
        this.newData = newData;
        this.noRow = noRow;
        this.noCol = noCol;
    }

   
    
    
    // METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataSheet(getProc(),oldData, noRow, noCol,v, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        CopexData data = (CopexData)v.get(0);
        edP.editDataSheet(dataSheet, data,noRow, noCol);
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        System.out.println("****REDO : newData : "+newData);
        CopexReturn cr = this.controller.updateDataSheet(getProc(),newData, noRow, noCol,v, MyConstants.REDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
         CopexData data = (CopexData)v.get(0);
        edP.editDataSheet(dataSheet, data,noRow, noCol);
    }
}
