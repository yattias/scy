/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.sql.Date;

/**
 * protocole de l'eleve
 * @author MBO
 */
public class LearnerProcedure extends ExperimentalProcedure{
    /*protocole initial */
    private InitialProcedure initialProc;
    /* mission */
    protected CopexMission mission;

    // CONSTRUCTEURS
    public LearnerProcedure(String name, CopexMission mission, Date dateLastModification, InitialProcedure initialProc) {
        super(name, dateLastModification);
        this.initialProc = initialProc ;
        this.mission = mission;
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

    public CopexMission getMission() {
        return mission;
    }
    public void setMission(CopexMission mission) {
        this.mission = mission;
    }

    /* retourne true si le protocole est actif pour sa mission */
    public boolean isActiv(CopexMission m){
        return (m.getDbKey() == this.mission.getDbKey() && activ);
    }
    
    // OVERRIDE
    @Override
    public Object clone() {
        LearnerProcedure p = (LearnerProcedure) super.clone() ;
        CopexMission missionC = null;
            if (mission != null)
                missionC  =(CopexMission)this.mission.clone();
        if (this.initialProc == null)
            p.setInitialProc(null);
        else
            p.setInitialProc((InitialProcedure)initialProc.clone());
        p.setMission(missionC);
        return p;
    }
}
