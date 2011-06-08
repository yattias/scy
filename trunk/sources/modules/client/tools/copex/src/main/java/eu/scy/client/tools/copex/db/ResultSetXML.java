/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import java.util.ArrayList;

/**
 * result from the database
 * @author MBO
 */
public class ResultSetXML {
    /* list data columns */
    private ArrayList<ColumnData> listData;

    public ResultSetXML() {
        this.listData = new ArrayList();
    }

    public ResultSetXML(ArrayList<ColumnData> listData) {
        this.listData = listData;
    }
    
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

    