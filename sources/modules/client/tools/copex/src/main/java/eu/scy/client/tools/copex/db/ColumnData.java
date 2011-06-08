/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

/**
 * column data
 * @author MBO
 */

public class ColumnData {
    /* column name */
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
