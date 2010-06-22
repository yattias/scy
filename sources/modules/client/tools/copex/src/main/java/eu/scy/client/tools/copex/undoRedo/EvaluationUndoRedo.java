/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;

/**
 * evaluation undo redo
 * @author Marjolaine
 */
public class EvaluationUndoRedo  extends CopexUndoRedo  {
    private ExperimentalProcedure proc;
    private Evaluation oldEvaluation;
    private Evaluation newEvaluation;

    public EvaluationUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, Evaluation oldEvaluation, Evaluation newEvaluation) {
        super(edP, controller, tree);
        this.proc = proc;
        this.oldEvaluation = oldEvaluation;
        this.newEvaluation = newEvaluation;
        if(oldEvaluation != null)
            this.oldEvaluation = (Evaluation)oldEvaluation.clone();
        if(newEvaluation != null)
            this.newEvaluation = (Evaluation)newEvaluation.clone();
    }

    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setEvaluation(proc, oldEvaluation, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        Evaluation evaluation = null;
        if(v.size() > 0)
            evaluation = (Evaluation)v.get(0);
        proc.setEvaluation(evaluation);
        edP.setEvaluation(evaluation);
    }

    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setEvaluation(proc, newEvaluation, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        Evaluation evaluation = null;
        if(v.size() > 0)
            evaluation = (Evaluation)v.get(0);
        proc.setEvaluation(evaluation);
        edP.setEvaluation(evaluation);
    }

}
