package eu.scy.client.tools.dataProcessTool.utilities;

import eu.scy.client.tools.dataProcessTool.common.Dataset;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public interface ActionCloseTab {
    public void setSelectedTab(CloseTab closeTab);
    public void doubleClickTab(CloseTab closeTab);
    public void openDialogAddDataset();
    public void openDialogCloseDataset(Dataset ds);
}
