/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dnd;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import java.io.Serializable;

/**
 * sous donnees qui sont a  coller
 * @author Marjolaine
 */
public class SubData extends DataTable implements Serializable{
    /* table mere*/
    private DataTable table;

    public SubData(DataProcessToolPanel owner,DataTable table,  Dataset ds) {
        super(owner, ds);
        this.table = table;
    }

    public DataTable getTable() {
        return table;
    }

   



}
