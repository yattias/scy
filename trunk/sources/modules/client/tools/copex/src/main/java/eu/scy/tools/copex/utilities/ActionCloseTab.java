/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.common.LearnerProcedure;

/**
 *
 * @author Marjolaine
 */
public interface ActionCloseTab {
    public void setSelectedTab(CloseTab closeTab);
    public void doubleClickTab(CloseTab closeTab);
    public void openDialogAddProc();
    public void openDialogCloseProc(LearnerProcedure proc);
}
