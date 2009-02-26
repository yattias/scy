/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.dnd;

import com.scy.scyDataTool.common.Dataset;
import com.scy.scyDataTool.dataTool.DataTable;
import com.scy.scyDataTool.dataTool.MainDataToolPanel;
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
