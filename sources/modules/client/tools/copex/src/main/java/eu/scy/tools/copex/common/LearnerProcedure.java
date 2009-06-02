/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.sql.Date;

/**
 * protocole de l'élève
 * @author MBO
 */
public class LearnerProcedure extends ExperimentalProcedure{
    /*protocole initial */
    private InitialProcedure initialProc;

    // CONSTRUCTEURS
    public LearnerProcedure(String name, CopexMission mission, Date dateLastModification, InitialProcedure initialProc) {
        super(name, mission, dateLastModification);
        this.initialProc = initialProc ;
    }

    public LearnerProcedure(long dbKey, String name, Date dateLastModification, boolean isActiv, char right, InitialProcedure initialProc) {
        super(dbKey, name, dateLastModification, isActiv, right);
        this.initialProc = initialProc ;
    }

    public LearnerProcedure(ExperimentalProcedure proc, InitialProcedure initialProc) {
        super(proc);
        this.initialProc = initialProc;
    }

    public InitialProcedure getInitialProc() {
        return initialProc;
    }

    public void setInitialProc(InitialProcedure initialProc) {
        this.initialProc = initialProc;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        LearnerProcedure p = (LearnerProcedure) super.clone() ;
        if (this.initialProc == null)
            p.setInitialProc(null);
        else
            p.setInitialProc((InitialProcedure)initialProc.clone());
        return p;
    }
}
