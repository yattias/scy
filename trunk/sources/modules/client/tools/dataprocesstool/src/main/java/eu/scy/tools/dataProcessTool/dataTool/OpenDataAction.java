/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

/**
 * open action dialog
 * @author Marjolaine
 */
public interface OpenDataAction {
    /* open an elo */
    public void openELO();
    /* import a csv file */
    public void importCSVFile();
    /* merge a ds into the current ds */
    public void mergeDataset();
}
