/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.common.ExperimentalProcedure;

/**
 *
 * @author Marjolaine
 */
public interface ActionCloseTab {
    public void setSelectedTab(CloseTab closeTab);
    public void doubleClickTab(CloseTab closeTab);
    public void openDialogAddProc();
    public void openDialogCloseProc(ExperimentalProcedure proc);
}
