/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;

/**
 * hypothesis undo redo
 * @author Marjolaine
 */
public class HypothesisUndoRedo  extends CopexUndoRedo{
    private ExperimentalProcedure proc;
    private Hypothesis oldHypothesis;
    private Hypothesis newHypothesis;

    public HypothesisUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, Hypothesis oldHypothesis, Hypothesis newHypothesis) {
        super(edP, controller, tree);
        this.proc = proc;
        this.oldHypothesis = oldHypothesis;
        this.newHypothesis = newHypothesis;
        if(oldHypothesis != null)
            this.oldHypothesis = (Hypothesis)oldHypothesis.clone();
        if(newHypothesis != null)
            this.newHypothesis = (Hypothesis)newHypothesis.clone();
    }


    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setHypothesis(proc, oldHypothesis, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        Hypothesis hypothesis = null;
        if(v.size() > 0)
            hypothesis = (Hypothesis)v.get(0);
        proc.setHypothesis(hypothesis);
        edP.setHypothesis(hypothesis);
    }

    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setHypothesis(proc, newHypothesis, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        Hypothesis hypothesis = null;
        if(v.size() > 0)
            hypothesis = (Hypothesis)v.get(0);
        proc.setHypothesis(hypothesis);
        edP.setHypothesis(hypothesis);
    }

}
