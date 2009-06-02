/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * represente la case d'un tableau 
 * @author MBO
 */
public class DataXML {
    // ATTRIBUTS
    /* identifiant */
    private long dbKey;
    /* donnée */
    private String data;
    /* numero de ligne */
    private int noRow;
    /* numero de colonne */
    private int noCol;

    // CONSTRUCTEURS
    public DataXML(long dbKey, String data, int noRow, int noCol) {
        this.dbKey = dbKey;
        this.data = data;
        this.noRow = noRow;
        this.noCol = noCol;
    }
    
    // METHODES
    /* identifiant */
    public String getDbKeyToXML(){
        return "        <id_data>"+this.dbKey+"</id_data>\n";
    }
    
    /* numero ligne */
    public String getNoRowToXML(){
        return "        <no_row>"+this.noRow+"</no_row>\n";
    }
    
     /* numero colonne */
    public String getNoColToXML(){
        return "        <no_col>"+this.noCol+"</no_col>\n";
    }
    
    /* data */
    public String getDataToXML(){
        return "        <data>"+this.data+"</data>\n";
    }
    
    /* ajout des parametres */
    public void addParameters(ArrayList<ParameterUserAction> listParameters){
        ParameterUserAction p  = new ParameterUserAction("id_data", ""+this.dbKey);
        listParameters.add(p);
        p  = new ParameterUserAction("no_row", ""+this.noRow);
        //listParameters.add(p);
         p  = new ParameterUserAction("no_col", ""+this.noCol);
        //listParameters.add(p);
         p  = new ParameterUserAction("data", ""+this.data);
        //listParameters.add(p);
    }
}
