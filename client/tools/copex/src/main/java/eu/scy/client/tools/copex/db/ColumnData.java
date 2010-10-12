/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

/**
 * colonne
 * @author MBO
 */
public class ColumnData {
    // ATTRIBUTS
    /* nom de la colonne */
    private String colName;
    /* valeur */
    private String value;

    // CONSTRUCTEUR
    public ColumnData(String colName, String value) {
        this.colName = colName;
        this.value = value;
    }

    // GETTER
    public String getValue() {
        return value;
    }

    public String getColName() {
        return colName;
    }
     
    
}
