/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * options for a mission
 * @author Marjolaine
 */
public class OptionMission implements Cloneable {

    private boolean canAddProc;
    private boolean canUseDataSheet;
    private boolean trace;

    public OptionMission(boolean canAddProc, boolean canUseDataSheet, boolean trace) {
        this.canAddProc = canAddProc;
        this.canUseDataSheet = canUseDataSheet;
        this.trace = trace;
    }

    /**
     * Get the value of trace
     *
     * @return the value of trace
     */
    public boolean isTrace() {
        return trace;
    }

    /**
     * Set the value of trace
     *
     * @param trace new value of trace
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }


    /**
     * Get the value of canUseDataSheet
     *
     * @return the value of canUseDataSheet
     */
    public boolean isCanUseDataSheet() {
        return canUseDataSheet;
    }

    /**
     * Set the value of canUseDataSheet
     *
     * @param canUseDataSheet new value of canUseDataSheet
     */
    public void setCanUseDataSheet(boolean canUseDataSheet) {
        this.canUseDataSheet = canUseDataSheet;
    }

    /**
     * Get the value of canAddProc
     *
     * @return the value of canAddProc
     */
    public boolean isCanAddProc() {
        return canAddProc;
    }

    /**
     * Set the value of canAddProc
     *
     * @param canAddProc new value of canAddProc
     */
    public void setCanAddProc(boolean canAddProc) {
        this.canAddProc = canAddProc;
    }

     @Override
    public Object clone()  {
        try {
            OptionMission o = (OptionMission) super.clone() ;
            o.setCanAddProc(new Boolean(this.canAddProc));
            o.setCanUseDataSheet(new Boolean(this.canUseDataSheet));
            o.setTrace(new Boolean(this.trace));
            return o;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}
