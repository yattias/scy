/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import java.util.ArrayList;

/**
 * resultat de la base de donnees
 * @author MBO
 */
public class ResultSetXML {
    // ATTRIBUTS
    /* liste de resultats colonnes */
    private ArrayList<ColumnData> listData;

    // CONSTRUCTEURS
    public ResultSetXML() {
        this.listData = new ArrayList();
    }

    public ResultSetXML(ArrayList<ColumnData> listData) {
        this.listData = listData;
    }
    
    // METHODES
    public void addData(ColumnData data){
        this.listData.add(data);
    }
    
    public String getColumnData(String columnName){
        int nbD = listData.size();
        for(int i=0; i<nbD; i++){
            if (listData.get(i).getColName().equals("'"+columnName+"'")){
                return listData.get(i).getValue() ;
            }
        }
        
        return null;
    }
}

    