/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

/**
 * column data
 * @author 
 */
public class ColumnData {
    /*name*/
    private String colName;
    /* value */
    private String value;

    public ColumnData(String colName, String value) {
        this.colName = colName;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getColName() {
        return colName;
    }
     
    
}
