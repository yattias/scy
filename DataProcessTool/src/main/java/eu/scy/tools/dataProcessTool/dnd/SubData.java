/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dnd;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.dataTool.MainDataToolPanel;
import java.io.Serializable;

/**
 * sous données qui sont à coller
 * @author Marjolaine
 */
public class SubData extends DataTable implements Serializable{

    /* dataTable mere */
    private DataTable table;
    /* anciens numeros de colonnes*/
    private int[] noHeaders;

    public SubData(MainDataToolPanel owner, Dataset ds, DataTable table, int[] noHeaders) {
        super(owner, ds);
        this.table = table;
        this.noHeaders = noHeaders ;
    }

    public DataTable getTable() {
        return table;
    }

    public int[] getNoHeaders(){
        return this.noHeaders;
    }



}
