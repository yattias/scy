/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.Mission;
import java.io.File;


/**
 * open action dialog
 * @author Marjolaine
 */
public interface OpenDataAction {
    /* open an elo */
    public void openELO(File file);
    public void openDataset(Mission mission, Dataset dataset);
    /* import a csv file */
    public void importELO(File file, boolean createNew);
    /* merge a ds into the current ds */
    public void mergeELO(File file );
    public void mergeDataset(Mission mission, Dataset dataset);
    /* new */
    public void newElo(String dsName);
}
